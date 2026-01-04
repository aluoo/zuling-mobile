package com.zxtx.hummer.exchange.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeEmployeeMapper;
import com.zxtx.hummer.exchange.domain.MbExchangeEmployee;
import com.zxtx.hummer.exchange.domain.MbExchangeInstall;
import com.zxtx.hummer.exchange.domain.MbExchangePhone;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangePhoneMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.exchange.domain.MbInstall;
import com.zxtx.hummer.exchange.domain.dto.ExchangeMemberAddDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangePhoneAddDTO;
import com.zxtx.hummer.exchange.domain.dto.InstallEditStatusDTO;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import com.zxtx.hummer.exchange.vo.ExchangePhoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 拉新换机包 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
public class MbExchangePhoneService extends ServiceImpl<MbExchangePhoneMapper, MbExchangePhone>{

    @Autowired
    MbExchangeInstallService mbExchangeInstallService;
    @Autowired
    MbExchangeEmployeeMapper mbExchangeEmployeeMapper;

    public List<ExchangePhoneVO> selectPage(ExchangeInstallReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<ExchangePhoneVO> resultList = this.getBaseMapper().selectByParam(req);
        if (CollUtil.isNotEmpty(resultList)) {
            for (ExchangePhoneVO applyVo : resultList) {
                applyVo.setInstallPackage(mbExchangeInstallService.getInstallNameById(applyVo.getId()));
            }
        }
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(ExchangePhoneAddDTO dto){
        MbExchangePhone mbExchangePhone = new MbExchangePhone();
        mbExchangePhone.setName(dto.getName());
        mbExchangePhone.setType(dto.getType());
        mbExchangePhone.setChannelNo(dto.getChannelNo());
        mbExchangePhone.setRemark(dto.getRemark());
        mbExchangePhone.setStatus(1);
        this.save(mbExchangePhone);

        List<MbExchangeInstall> exchangeInstallList = new ArrayList<>();
        dto.getInstallIds().stream().forEach(e -> {
            MbExchangeInstall mbExchangeInstall = new MbExchangeInstall();
            mbExchangeInstall.setExchangePhoneId(mbExchangePhone.getId());
            mbExchangeInstall.setInstallId(e);
            exchangeInstallList.add(mbExchangeInstall);
        });
        mbExchangeInstallService.saveBatch(exchangeInstallList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ExchangePhoneAddDTO dto){
        MbExchangePhone mbExchangePhone = this.getById(dto.getId());
        BeanUtil.copyProperties(dto,mbExchangePhone);
        this.updateById(mbExchangePhone);

        //删除旧的包和安装包关联
        mbExchangeInstallService.remove(Wrappers.lambdaQuery(MbExchangeInstall.class)
                .eq(MbExchangeInstall::getExchangePhoneId,dto.getId()));

        //新的关联建立
        List<MbExchangeInstall> exchangeInstallList = new ArrayList<>();
        dto.getInstallIds().stream().forEach(e -> {
            MbExchangeInstall mbExchangeInstall = new MbExchangeInstall();
            mbExchangeInstall.setExchangePhoneId(mbExchangePhone.getId());
            mbExchangeInstall.setInstallId(e);
            exchangeInstallList.add(mbExchangeInstall);
        });
        mbExchangeInstallService.saveBatch(exchangeInstallList);
    }

    public ExchangePhoneAddDTO view(Long id){
        ExchangePhoneAddDTO resultVo = new ExchangePhoneAddDTO();
        MbExchangePhone mbExchangePhone = this.getById(id);
        BeanUtil.copyProperties(mbExchangePhone,resultVo);
        List<Long> installIds = mbExchangeInstallService.getInstallById(id);

        List<Long> employeeIds = mbExchangeEmployeeMapper.selectList(Wrappers.lambdaQuery(MbExchangeEmployee.class).eq(MbExchangeEmployee::getExchangePhoneId,id))
                .stream().map(MbExchangeEmployee::getEmployeeId).collect(Collectors.toList());

        resultVo.setInstallIds(installIds);
        resultVo.setEmployeeIds(employeeIds);
        return resultVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void editStatus(InstallEditStatusDTO dto){
        MbExchangePhone mbExchangePhone = this.getById(dto.getId());
        mbExchangePhone.setStatus(dto.getStatus());
        this.updateById(mbExchangePhone);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removePackage(Long id){
        this.removeById(id);
        mbExchangeInstallService.remove(Wrappers.lambdaQuery(MbExchangeInstall.class).eq(MbExchangeInstall::getExchangePhoneId,id));
        mbExchangeEmployeeMapper.delete(Wrappers.lambdaQuery(MbExchangeEmployee.class).eq(MbExchangeEmployee::getExchangePhoneId,id));
    }


}
