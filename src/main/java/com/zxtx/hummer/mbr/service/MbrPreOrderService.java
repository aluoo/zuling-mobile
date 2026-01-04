package com.zxtx.hummer.mbr.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.mbr.dao.MbrPreOrderMapper;
import com.zxtx.hummer.mbr.domain.MbrPreOrder;
import com.zxtx.hummer.mbr.domain.MbrPreOrderQuoteLog;
import com.zxtx.hummer.mbr.domain.enums.MbrQuoteLogStatusEnum;
import com.zxtx.hummer.mbr.req.MbrPreOrderReq;
import com.zxtx.hummer.mbr.response.MbrPreOrderVO;
import com.zxtx.hummer.product.domain.request.OrderLogQueryReq;
import com.zxtx.hummer.product.service.OrderLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MbrPreOrderService extends ServiceImpl<MbrPreOrderMapper, MbrPreOrder> {

    @Autowired
    MbrPreOrderQuoteLogService preOrderQuoteLogService;
    @Autowired
    CompanyService companyService;
    @Autowired
    OrderLogService orderLogService;

    public List<MbrPreOrderVO> settlePageList(MbrPreOrderReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<MbrPreOrderVO> resultList = this.baseMapper.selectBySearch(req);
        PageHelper.clearPage();
        return resultList;
    }

    public MbrPreOrderVO preOrderDetail(Long id){
        MbrPreOrderVO vo = new MbrPreOrderVO();
        MbrPreOrder preOrder = this.getById(id);
        BeanUtil.copyProperties(preOrder, vo);

        List<MbrPreOrderQuoteLog> quoteLogList = preOrderQuoteLogService.lambdaQuery()
                .eq(MbrPreOrderQuoteLog::getOrderId,id)
                .eq(MbrPreOrderQuoteLog::getDeleted,0)
                .list();
        List<MbrPreOrderVO.QuoteLogVO> quoteLogVOList = new ArrayList<>();
        //租机商进件单审核记录
        if(CollUtil.isNotEmpty(quoteLogList)){
            List<Long> companyIds = quoteLogList.stream().map(MbrPreOrderQuoteLog::getRecyclerCompanyId)
                    .collect(Collectors.toList());
            Map<Long, Company> companyMap =  companyService.getCompanyInfoMap(companyIds);
            quoteLogList.forEach(quoteLog -> {
                quoteLogVOList.add(MbrPreOrderVO.QuoteLogVO.builder()
                        .recyclerCompanyName(companyMap.get(quoteLog.getRecyclerCompanyId()).getName())
                        .statusName(EnumUtil.getBy(MbrQuoteLogStatusEnum::getCode,quoteLog.getStatus()).getDesc())
                        .creteTime(quoteLog.getCreateTime()).build());
            });
        }
        //日志
        vo.setOrderLogList(orderLogService.listAllLogsByOrderId(OrderLogQueryReq.builder().orderId(id).build()));
        vo.setQuoteLogVOList(quoteLogVOList);
        return vo;
    }

}