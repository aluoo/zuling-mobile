package com.zxtx.hummer.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.insurance.domain.DiInsurance;
import com.zxtx.hummer.insurance.domain.DiProductInsurance;
import com.zxtx.hummer.insurance.domain.DiSkuInsurance;
import com.zxtx.hummer.insurance.req.DiInsuranceSkuDTO;
import com.zxtx.hummer.insurance.req.DiProductInsuranceDTO;
import com.zxtx.hummer.insurance.req.DiProductQueryReq;
import com.zxtx.hummer.insurance.vo.DiInsuranceSkuVO;
import com.zxtx.hummer.insurance.vo.DiInsuranceViewVO;
import com.zxtx.hummer.insurance.vo.DiMobileInsuranceViewVO;
import com.zxtx.hummer.insurance.vo.DiProductVO;
import com.zxtx.hummer.product.domain.Product;
import com.zxtx.hummer.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 手机-数保产品关联表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
@Slf4j
public class DiProductManageService  {
    @Autowired
    private ProductService productService;
    @Autowired
    private DiProductInsuranceService diProductInsuranceService;
    @Autowired
    private DiInsuranceService diInsuranceService;
    @Autowired
    private DiTypeService diTypeService;
    @Autowired
    private DiPackageService diPackageService;
    @Autowired
    private DiSkuInsuranceService skuInsuranceService;

    public PageUtils<DiProductVO> listProduct(DiProductQueryReq req) {

        //有关联保险产品手机集合
        List<Long> insuranceProductIds = diProductInsuranceService.list(Wrappers.lambdaQuery(DiProductInsurance.class)
                .eq(DiProductInsurance::getDeleted,false)).stream().map(DiProductInsurance::getProductId).distinct().collect(Collectors.toList());

        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        LambdaQueryWrapper<Product> qw = new LambdaQueryWrapper<Product>()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Product::getDigitalInsuranceAble, true)
                .like(StrUtil.isNotBlank(req.getName()), Product::getName, req.getName())
                .in(null!=req.getProductContact() && req.getProductContact() && CollUtil.isNotEmpty(insuranceProductIds) ,Product::getId,insuranceProductIds)
                .notIn(null!=req.getProductContact() && !req.getProductContact() && CollUtil.isNotEmpty(insuranceProductIds),Product::getId,insuranceProductIds)
                .orderByDesc(Product::getActivated)
                .orderByAsc(Product::getSort)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime);
        List<Product> list = productService.list(qw);

        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }

        List<DiProductVO> resp = BeanUtil.copyToList(list, DiProductVO.class);

        resp.forEach(vo -> {
            vo.setProductContact(insuranceProductIds.contains(vo.getId()));
        });
        return new PageUtils<>(resp, page.getTotal());
    }


    public void saveProductInsurance(DiProductInsuranceDTO dto){

        List<DiProductInsurance> resultList = new ArrayList<>();

        //删除已经关联
        diProductInsuranceService.remove(Wrappers.lambdaQuery(DiProductInsurance.class)
                .eq(DiProductInsurance::getProductId,dto.getProductId()));

        if (CollUtil.isEmpty(dto.getInsuranceIds())) {
            return;
        }

        for (Long insuranceId : dto.getInsuranceIds()) {
            DiProductInsurance productInsurance = new DiProductInsurance();
            productInsurance.setProductId(dto.getProductId());
            productInsurance.setInsuranceId(insuranceId);
            productInsurance.setActivated(true);
            productInsurance.setDeleted(false);
            resultList.add(productInsurance);
        }

        diProductInsuranceService.saveOrUpdateBatch(resultList);
    }


    public DiMobileInsuranceViewVO view (Long productId){
        DiMobileInsuranceViewVO resultVO = new DiMobileInsuranceViewVO();
        //保险产品
        List<DiInsuranceViewVO> insuranceList = new ArrayList<>();

        List<DiProductInsurance> productList = diProductInsuranceService.list(
                Wrappers.lambdaQuery(DiProductInsurance.class).eq(DiProductInsurance::getProductId,productId));

        if(CollUtil.isNotEmpty(productList)){
            productList.forEach(e -> {
                DiInsuranceViewVO insuranceViewVO = new DiInsuranceViewVO();
                DiInsurance insurance = diInsuranceService.getById(e.getInsuranceId());
                insuranceViewVO.setName(insurance.getName());
                insuranceViewVO.setDescription(insurance.getDescription());
                insuranceViewVO.setPeriod(insurance.getPeriod());
                insuranceViewVO.setId(insurance.getId());
                insuranceList.add(insuranceViewVO);
            });
        }

        resultVO.setProductList(insuranceList);
        return resultVO;
    }


    public List<DiInsuranceSkuVO> skuDetail(Long skuId){

        List<DiInsuranceSkuVO> resultVo = new ArrayList<>();

        List<DiSkuInsurance> resultList = skuInsuranceService.list(
                Wrappers.lambdaQuery(DiSkuInsurance.class).eq(DiSkuInsurance::getSkuId,skuId));

        if(CollUtil.isEmpty(resultList)) return resultVo;

        for(DiSkuInsurance skuInsurance:resultList){
            DiInsuranceSkuVO vo = new DiInsuranceSkuVO();
            DiInsurance insurance = diInsuranceService.getById(skuInsurance.getInsuranceId());
            vo.setSkuId(skuInsurance.getSkuId());
            vo.setInsuranceId(insurance.getId());
            vo.setInsuranceName(insurance.getName());
            vo.setInsurancePeriod(insurance.getPeriod());
            vo.setDownPrice(skuInsurance.getDownPrice());
            resultVo.add(vo);
        }

        return resultVo;
    }


    @Transactional(rollbackFor = Exception.class)
    public void skuSave(DiInsuranceSkuDTO req){

        if(CollUtil.isEmpty(req.getPriseList())){
            throw new BaseException(99999,"产品底价设置区间不能为空");
        }

        //删除旧的配置
        skuInsuranceService.remove(
                Wrappers.lambdaQuery(DiSkuInsurance.class).eq(DiSkuInsurance::getSkuId,req.getSkuId()));

        List<DiSkuInsurance> saveList = new ArrayList<>();

        req.getPriseList().forEach(e -> {
            DiSkuInsurance skuInsurance = new DiSkuInsurance();
            skuInsurance.setSkuId(req.getSkuId());
            skuInsurance.setInsuranceId(e.getInsuranceId());
            skuInsurance.setDownPrice(e.getDownPrice());
            saveList.add(skuInsurance);
        });

        skuInsuranceService.saveBatch(saveList);

    }


}