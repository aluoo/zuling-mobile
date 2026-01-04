package com.zxtx.hummer.exchange.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeCustomMapper;
import com.zxtx.hummer.exchange.domain.MbExchangeCustom;
import com.zxtx.hummer.exchange.req.ExchangeOrderReq;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客户安装换机包 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
public class MbExchangeCustomService extends ServiceImpl<MbExchangeCustomMapper, MbExchangeCustom>{

    /**
     * 安装包下载次数
     * @param installId
     * @return
     */
    public Integer sumByInstallId(Long installId){
        List<MbExchangeCustom> reusltList = this.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getInstallId,installId));

        if(CollUtil.isEmpty(reusltList)) return 0;
        return reusltList.size();
    }

    public PageUtils<MbExchangeCustom> selectInstallPage(ExchangeOrderReq req){
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<MbExchangeCustom> list = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(req.getCustomPhone()), MbExchangeCustom::getCustomPhone, req.getCustomPhone())
                .eq(StrUtil.isNotBlank(req.getEmployeePhone()), MbExchangeCustom::getEmployeePhone, req.getEmployeePhone())
                .eq(StrUtil.isNotBlank(req.getOrderSn()), MbExchangeCustom::getOrderSn, req.getOrderSn())
                .eq(StrUtil.isNotBlank(req.getOaid()), MbExchangeCustom::getOaid, req.getOaid())
                .orderByDesc(MbExchangeCustom::getCreateTime)
                .list();
        if(CollUtil.isEmpty(list)) return  PageUtils.emptyPage();;
        return new PageUtils(list, (int) page.getTotal());
    }


}
