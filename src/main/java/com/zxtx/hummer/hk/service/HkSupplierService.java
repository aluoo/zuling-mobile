package com.zxtx.hummer.hk.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.hk.domain.HkSupplier;
import com.zxtx.hummer.hk.domain.enums.HkOperationEnum;
import com.zxtx.hummer.hk.domain.request.HkSupplierAddReq;
import com.zxtx.hummer.hk.domain.request.HkSupplierEditReq;
import com.zxtx.hummer.hk.domain.request.HkSupplierQueryReq;
import com.zxtx.hummer.hk.domain.response.HkSupplierVO;
import com.zxtx.hummer.product.service.OrderLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/4
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class HkSupplierService {
    @Autowired
    private HkSupplierRepository hkSupplierRepository;
    @Autowired
    private RedisLockService redisLockService;
    @Autowired
    private OrderLogService orderLogService;

    public PageUtils<HkSupplierVO> list(HkSupplierQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<HkSupplier> list = hkSupplierRepository.lambdaQuery()
                .eq(req.getStatus() != null, HkSupplier::getStatus, req.getStatus())
                .like(StrUtil.isNotBlank(req.getName()), HkSupplier::getName, req.getName())
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByDesc(HkSupplier::getSort)
                .orderByDesc(HkSupplier::getCreateTime)
                .orderByDesc(HkSupplier::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        List<HkSupplierVO> resp = BeanUtil.copyToList(list, HkSupplierVO.class);

        return new PageUtils<>(resp, page.getTotal());
    }
    public HkSupplierVO detail(Long id) {
        HkSupplier bean = Optional.ofNullable(hkSupplierRepository.lambdaQuery()
                .eq(HkSupplier::getId, id)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(-1, "数据不存在"));
        return BeanUtil.copyProperties(bean, HkSupplierVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(HkSupplierAddReq req) {
        HkSupplier bean = BeanUtil.copyProperties(req, HkSupplier.class);
        bean.setId(SnowflakeIdWorker.nextID());

        boolean success = hkSupplierRepository.save(bean);

        if (success) {
            orderLogService.addLog(ShiroUtils.getUserId(), bean.getId(), HkOperationEnum.CREATE.getCode(), HkOperationEnum.CREATE.getCode(), HkOperationEnum.CREATE.getDesc(), HkOperationEnum.CREATE.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(HkSupplierEditReq req) {
        redisLockService.redisLock("pc_hk_supplier_edit_lk", req.getId());
        Optional.ofNullable(hkSupplierRepository.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(HkSupplier::getId, req.getId())
                .one()).orElseThrow(() -> new BaseException(-1, "数据不存在"));
        HkSupplier bean = BeanUtil.copyProperties(req, HkSupplier.class);

        boolean success = hkSupplierRepository.updateById(bean);

        if (success) {
            orderLogService.addLog(ShiroUtils.getUserId(), bean.getId(), HkOperationEnum.EDIT.getCode(), HkOperationEnum.EDIT.getCode(), HkOperationEnum.EDIT.getDesc(), HkOperationEnum.EDIT.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void switchStatus(Long id) {
        redisLockService.redisLock("pc_hk_supplier_switch_status_lk", id);
        HkSupplier bean = Optional.ofNullable(hkSupplierRepository.lambdaQuery()
                .eq(HkSupplier::getId, id)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(-1, "数据不存在"));
        int newStatus = bean.getStatus() == 1 ? 0 : 1;
        boolean success = hkSupplierRepository.lambdaUpdate()
                .set(HkSupplier::getStatus, newStatus)
                .eq(HkSupplier::getId, id)
                .eq(HkSupplier::getStatus, bean.getStatus())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new HkSupplier());
        if (success) {
            HkOperationEnum op = newStatus == 1 ? HkOperationEnum.ONLINE : HkOperationEnum.OFFLINE;
            orderLogService.addLog(ShiroUtils.getUserId(), id, op.getCode(), op.getCode(), op.getDesc(), op.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        redisLockService.redisLock("pc_hk_supplier_remove_lk", id);
        Long userId = ShiroUtils.getUserId();
        boolean success = hkSupplierRepository.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(HkSupplier::getId, id)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new HkSupplier());
        if (success) {
            orderLogService.addLog(userId, id, HkOperationEnum.DELETE.getCode(), HkOperationEnum.DELETE.getCode(), HkOperationEnum.DELETE.getDesc(), HkOperationEnum.DELETE.getRemark());
        }
    }
}