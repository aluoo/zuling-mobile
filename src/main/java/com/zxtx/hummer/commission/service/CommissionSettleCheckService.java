package com.zxtx.hummer.commission.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxtx.hummer.commission.dao.mapper.CommissionSettleCheckMapper;
import com.zxtx.hummer.commission.dao.mapper.CommissionSettleMapper;
import com.zxtx.hummer.commission.domain.CommissionSettle;
import com.zxtx.hummer.commission.domain.CommissionSettleCheck;
import com.zxtx.hummer.commission.dto.CommissionSettleCheckSumVO;
import com.zxtx.hummer.commission.dto.CommissionSettleCheckVO;
import com.zxtx.hummer.commission.req.CommissionSettleCheckReq;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.CompanyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
@Slf4j
public class CommissionSettleCheckService extends ServiceImpl<CommissionSettleCheckMapper, CommissionSettleCheck> {

    @Autowired
    private CommissionSettleMapper commissionSettleMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    public List<CommissionSettleCheckVO> listOrder(CommissionSettleCheckReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<CommissionSettleCheckVO> resultList = this.baseMapper.selectByParam(req);
        if(CollUtil.isNotEmpty(resultList)){
            resultList.forEach(e ->{
                if(e.getCreateTimeOne()!=null){
                    e.setCreateTime(e.getCreateTimeOne());
                }
                if(e.getCreateTimeTwo()!=null){
                    e.setCreateTime(e.getCreateTimeTwo());
                }
                if(e.getCreateTimeThree()!=null){
                    e.setCreateTime(e.getCreateTimeThree());
                }
                e.setAllAmountStr(StringUtils.fenToYuan(e.getAllAmount()));
                e.setPayAmountStr(StringUtils.fenToYuan(e.getPayAmount()));
                e.setSettleBalanceStr(StringUtils.fenToYuan(e.getSettleBalance()));
            });
        }
        return resultList;
    }

    public CommissionSettleCheckSumVO SumByParam(CommissionSettleCheckReq req) {
        CommissionSettleCheckSumVO resultVo = this.baseMapper.SumByParam(req);
        if(ObjectUtil.isNull(resultVo)){
            return CommissionSettleCheckSumVO.builder().appTotal(0L).insuranceTotal(0L).mobileTotal(0L).build();
        }
        return resultVo;
    }


    public void fix(CommissionSettleCheckReq req) {
        List<CommissionSettleCheckVO> resultList = this.baseMapper.fixByParam(req);
        if(CollUtil.isEmpty(resultList)) return;

        for(CommissionSettleCheckVO vo : resultList){
            List<CommissionSettle> settleList = commissionSettleMapper.selectList(Wrappers.lambdaQuery(CommissionSettle.class)
                    .eq(CommissionSettle::getCorrelationId,Long.valueOf(vo.getCorrelationId())));
            //总得分佣金额
            Integer allAmount = settleList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
            //最后层级
            CommissionSettle maxEmployee = settleList.stream()
                    .max(Comparator.comparingInt(CommissionSettle::getLevel)).get();
            String [] levels = maxEmployee.getAncestors().split(",");
            Employee region = null;
            if(levels.length>2){
                Long regionId = Long.valueOf(levels[2]);
                region = employeeMapper.selectById(regionId);
            }
            Integer regionAmount = 0;
            //合伙人直营门店的
            if(ObjectUtil.isNotNull(region) && CompanyType.COMPANY.getCode()!=region.getCompanyType().intValue()){
                List<CommissionSettle> regionList =  settleList.stream().filter(e->e.getLevel()>=2).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(regionList)){
                    regionAmount = regionList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
                }
            }else{
                List<CommissionSettle> regionList =  settleList.stream().filter(e->e.getLevel()>=3).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(regionList)){
                    regionAmount = regionList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
                }
            }
            Integer realAmount = allAmount-regionAmount;
            this.lambdaUpdate().set(CommissionSettleCheck::getSettleBalance,realAmount).eq(CommissionSettleCheck::getId,vo.getId()).update();
        }

    }

    /**
     * 更新合伙人账单金额
     */
    public void fixAll() {
        List<CommissionSettleCheck> resultList = this.list();
        if(CollUtil.isEmpty(resultList)) return;

        for(CommissionSettleCheck vo : resultList){
            List<CommissionSettle> settleList = commissionSettleMapper.selectList(Wrappers.lambdaQuery(CommissionSettle.class)
                    .eq(CommissionSettle::getCorrelationId,Long.valueOf(vo.getCorrelationId())));
            //总得分佣金额
            Integer allAmount = settleList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
            vo.setAllAmount(allAmount);
            vo.setPayAmount(allAmount-vo.getSettleBalance());
            this.updateById(vo);
        }

    }


}