package com.zxtx.hummer.insurance.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.insurance.dao.mapper.DiInsuranceOptionMapper;
import com.zxtx.hummer.insurance.domain.DiInsuranceOption;
import com.zxtx.hummer.insurance.domain.DiOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 数保产品关联选项表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiInsuranceOptionService extends ServiceImpl<DiInsuranceOptionMapper, DiInsuranceOption> {
    @Autowired
    DiOptionService diOptionService;

    public List<Long> getOptionIdsByProductId(Long insuranceId) {
        if (insuranceId == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<DiInsuranceOption> qw = new LambdaQueryWrapper<DiInsuranceOption>().eq(DiInsuranceOption::getInsuranceId, insuranceId);
        return this.list(qw).stream().map(DiInsuranceOption::getOptionId).collect(Collectors.toList());
    }

    public void add(List<Long> optionIds,Long insuranceId){
        List<DiInsuranceOption> insuranceOptionList = new ArrayList<>();
        optionIds.stream().forEach(e ->{
                    DiInsuranceOption insuranceOption = new DiInsuranceOption();
                    insuranceOption.setInsuranceId(insuranceId);
                    insuranceOption.setOptionId(e);
                    insuranceOptionList.add(insuranceOption);});
        this.saveBatch(insuranceOptionList);
    }

    public void removeByInsuranceId(Long insuranceId){
        this.remove(new LambdaQueryWrapper<DiInsuranceOption>()
                .eq(DiInsuranceOption::getInsuranceId, insuranceId));
    }

    /**
     * 查找下级配置选项
     * @param pid
     * @return
     */
    public List<DiOption> getOptionIdsByPid(Long pid) {
        return  diOptionService.lambdaQuery().eq(DiOption::getParentId,pid).list();
    }

    /**
     * 获取理赔项目某项的资料示例图
     * @return
     */
    public List<DiOption> getCodeImages(String ancestors,String code) {
        return  diOptionService.lambdaQuery()
                .eq(DiOption::getCode,code)
                .eq(AbstractBaseEntity::getDeleted, false)
                .likeRight(DiOption::getAncestors,ancestors)
                .list();
    }
}