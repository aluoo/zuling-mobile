package com.zxtx.hummer.exchange.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeInstallMapper;
import com.zxtx.hummer.exchange.domain.MbExchangeInstall;
import com.zxtx.hummer.exchange.domain.MbInstall;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 拉新换机安装包关联表 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
public class MbExchangeInstallService extends ServiceImpl<MbExchangeInstallMapper, MbExchangeInstall>{

    @Autowired
    private MbExchangeCustomService mbExchangeCustomService;
    @Autowired
    private MbInstallService mbInstallService;


    public List<ExchangeInstallVO> selectPage(ExchangeInstallReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<ExchangeInstallVO> resultList = this.getBaseMapper().selectByParam(req);
        if (CollUtil.isNotEmpty(resultList)) {
            for (ExchangeInstallVO applyVo : resultList) {
                //下载次数
                mbExchangeCustomService.sumByInstallId(applyVo.getId());
            }
        }
        return resultList;
    }

    public List<Long> getInstallById(Long exchangePhoneId){

        List<MbExchangeInstall> resultList = this.list(Wrappers.lambdaQuery(MbExchangeInstall.class)
                .eq(MbExchangeInstall::getExchangePhoneId,exchangePhoneId));

        if(CollUtil.isEmpty(resultList)) return null;

        List<Long> installIds = resultList.stream().map(MbExchangeInstall::getInstallId).collect(Collectors.toList());

        return installIds;

    }

    public List<MbInstall> packageInstall(Long exchangePhoneId){

        List<MbExchangeInstall> resultList = this.list(Wrappers.lambdaQuery(MbExchangeInstall.class)
                .eq(MbExchangeInstall::getExchangePhoneId,exchangePhoneId));

        if(CollUtil.isEmpty(resultList)) return Collections.emptyList();

        List<Long> installIds = resultList.stream().map(MbExchangeInstall::getInstallId).collect(Collectors.toList());

        List<MbInstall> installList = mbInstallService.lambdaQuery().in(MbInstall::getId,installIds).eq(MbInstall::getStatus,1).list();

        return installList;

    }

    public String getInstallNameById(Long exchangePhoneId){
        List<MbExchangeInstall> resultList = this.list(Wrappers.lambdaQuery(MbExchangeInstall.class)
                .eq(MbExchangeInstall::getExchangePhoneId,exchangePhoneId));
        if(CollUtil.isEmpty(resultList)) return null;
        List<Long> installIds = resultList.stream().map(MbExchangeInstall::getInstallId).collect(Collectors.toList());
        List<MbInstall> installList = mbInstallService.list(Wrappers.lambdaQuery(MbInstall.class)
                .in(MbInstall::getId,installIds));
        List<String> nameList = installList.stream().map(MbInstall::getName).collect(Collectors.toList());
        String name = StrUtil.join(",",nameList);
        return name;
    }


}
