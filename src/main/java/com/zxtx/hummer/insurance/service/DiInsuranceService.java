package com.zxtx.hummer.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.insurance.dao.mapper.DiInsuranceMapper;
import com.zxtx.hummer.insurance.domain.DiInsurance;
import com.zxtx.hummer.insurance.domain.DiProductInsurance;
import com.zxtx.hummer.insurance.domain.DiProductInsurancePrice;
import com.zxtx.hummer.insurance.req.DiInsurancePriceDTO;
import com.zxtx.hummer.insurance.req.DiInsuranceReq;
import com.zxtx.hummer.insurance.req.DiProductOperatorDTO;
import com.zxtx.hummer.insurance.req.DiUpdateStatusDTO;
import com.zxtx.hummer.insurance.vo.DiInsuranceVO;
import com.zxtx.hummer.insurance.vo.DiInsuranceViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 保险产品表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiInsuranceService extends ServiceImpl<DiInsuranceMapper, DiInsurance> {

    @Autowired
    DiInsuranceOptionService diInsuranceOptionService;
    @Autowired
    DiOptionService diOptionService;
    @Autowired
    DiProductInsuranceService diProductInsuranceService;
    @Autowired
    DiProductInsurancePriceService productInsurancePriceService;

    public List<DiInsuranceVO> selectPage(DiInsuranceReq req) {
        //SKU底价配置只展示有关联的产品
        if(req.getProductId()!=null){
            List<DiProductInsurance> insuranceList = diProductInsuranceService.lambdaQuery().eq(DiProductInsurance::getProductId,req.getProductId()).list();
            if(CollUtil.isNotEmpty(insuranceList)){
                req.setInsuranceIds(insuranceList.stream().map(DiProductInsurance::getInsuranceId).collect(Collectors.toList()));
            }else{
                req.setInsuranceIds(Arrays.asList(0L));
            }
        }
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiInsuranceVO> resultList = this.getBaseMapper().selectByParam(req);
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(DiProductOperatorDTO req) {
        DiInsurance insurance = new DiInsurance();
        BeanUtil.copyProperties(req,insurance);
        insurance.setStatus(1);
        this.save(insurance);

        if(CollUtil.isNotEmpty(req.getOptionIds())){
            diInsuranceOptionService.add(req.getOptionIds(),insurance.getId());
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(DiProductOperatorDTO req) {
        DiInsurance insurance = BeanUtil.copyProperties(req,DiInsurance.class);
        insurance.setStatus(1);
        this.updateById(insurance);
        diInsuranceOptionService.removeByInsuranceId(insurance.getId());
        if(CollUtil.isNotEmpty(req.getOptionIds())){
            diInsuranceOptionService.add(req.getOptionIds(),insurance.getId());
        }
    }

    public DiInsuranceViewVO detailById(Long productId) {
        DiInsurance bean = this.getById(productId);
        if (bean == null) {
            throw new BaseException(-1, "商品不存在");
        }
        DiInsuranceViewVO vo = BeanUtil.copyProperties(bean, DiInsuranceViewVO.class);

        List<Long> optionIds = diInsuranceOptionService.getOptionIdsByProductId(productId);
        vo.setOptionIds(optionIds);

        // 构建选择树，然后赋值商品选中信息
        List<Tree<Long>> trees = diOptionService.buildOptionTree(optionIds);
        vo.setOptions(trees);

        //价格区间
        vo.setPriceList(productInsurancePriceService.ListByInsurance(productId));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void switchProductActivatedStatus(DiUpdateStatusDTO req) {
        //上线
        if(req.getStatus()==1){
          this.lambdaUpdate().set(DiInsurance::getStatus,req.getStatus()).eq(DiInsurance::getId, req.getId())
                  .eq(DiInsurance::getDeleted, false).update(new DiInsurance());

            diProductInsuranceService.lambdaUpdate().set(DiProductInsurance::getActivated,1)
                    .eq(DiProductInsurance::getInsuranceId,req.getId())
                    .update(new DiProductInsurance());
        }
        //下线
        if(req.getStatus()==2){
            this.lambdaUpdate().set(DiInsurance::getStatus,req.getStatus()).eq(DiInsurance::getId, req.getId())
                    .eq(DiInsurance::getDeleted, false).update(new DiInsurance());

            diProductInsuranceService.lambdaUpdate().set(DiProductInsurance::getActivated,0)
                    .eq(DiProductInsurance::getInsuranceId,req.getId())
                    .update(new DiProductInsurance());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void priceSave(DiInsurancePriceDTO dto){

        if(CollUtil.isEmpty(dto.getPriseList())){
            throw new BaseException(99999,"价格区间不能为空");
        }

        productInsurancePriceService.remove(Wrappers.lambdaQuery(DiProductInsurancePrice.class)
                .eq(DiProductInsurancePrice::getInsuranceId,dto.getInsuranceId()));

        List<DiProductInsurancePrice> resultList = new ArrayList<>();

        for(DiInsurancePriceDTO.priceDTO detailDto:dto.getPriseList()){
            detailDto.setId(null);
            DiProductInsurancePrice price = new DiProductInsurancePrice();
            BeanUtil.copyProperties(detailDto,price,CopyOptions.create().ignoreNullValue());
            price.setInsuranceId(dto.getInsuranceId());
            resultList.add(price);
        }

        productInsurancePriceService.saveBatch(resultList);

    }
}