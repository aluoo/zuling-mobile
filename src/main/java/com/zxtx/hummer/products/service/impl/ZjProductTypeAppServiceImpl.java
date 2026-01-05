package com.zxtx.hummer.products.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.products.domain.ZjProductTypeApp;
import com.zxtx.hummer.products.dao.mapper.ZjProductTypeAppMapper;
import com.zxtx.hummer.products.domain.request.ZjProductTypeAppOperatorReq;
import com.zxtx.hummer.products.domain.request.ZjProductTypeAppQueryReq;
import com.zxtx.hummer.products.domain.response.ZjProductTypeAppVO;
import com.zxtx.hummer.products.service.IZjProductTypeAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 小程序产品种类 服务实现类
 * </p>
 *
 * @author L
 * @since 2026-01-05
 */
@Service
public class ZjProductTypeAppServiceImpl extends ServiceImpl<ZjProductTypeAppMapper, ZjProductTypeApp> implements IZjProductTypeAppService {

    @Override
    public PageUtils<ZjProductTypeAppVO> listWithPage(ZjProductTypeAppQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());

        List<ZjProductTypeApp> list = this.lambdaQuery()
                .like(StrUtil.isNotBlank(req.getProductCategory()), ZjProductTypeApp::getProductCategory, req.getProductCategory())
                .orderByAsc(ZjProductTypeApp::getSort)
                .orderByAsc(ZjProductTypeApp::getId)
                .list();

        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        //或许根据商品ids获取商品列表

        List<ZjProductTypeAppVO> voList = BeanUtil.copyToList(list, ZjProductTypeAppVO.class);
        return new PageUtils<>(voList, page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ZjProductTypeAppOperatorReq req) {
        ZjProductTypeApp entity = new ZjProductTypeApp();
        BeanUtil.copyProperties(req, entity);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ZjProductTypeAppOperatorReq req) {
        ZjProductTypeApp entity = new ZjProductTypeApp();
        BeanUtil.copyProperties(req, entity);
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.removeById(id);
    }
}
