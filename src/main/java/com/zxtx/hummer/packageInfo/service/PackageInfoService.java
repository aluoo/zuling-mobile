package com.zxtx.hummer.packageInfo.service;

import cn.hutool.core.bean.BeanUtil;
import com.zxtx.hummer.packageInfo.domain.PackageInfo;
import com.zxtx.hummer.packageInfo.dao.PackageInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.packageInfo.domain.PackageInfoDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 套餐信息 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-02-26
 */
@Service
public class PackageInfoService extends ServiceImpl<PackageInfoMapper, PackageInfo> {
    @Autowired
    PackageInfoDefaultService packageInfoDefaultService;

    @Transactional(rollbackFor = Exception.class)
    public void saveDefault(Long companyId) {
        //默认套餐
        List<PackageInfoDefault> defaultList = packageInfoDefaultService.list();
        for (PackageInfoDefault packageInfoDefault : defaultList) {
            PackageInfo packageInfo = new PackageInfo();
            BeanUtil.copyProperties(packageInfoDefault, packageInfo, "id");
            packageInfo.setCompanyId(companyId);
            packageInfo.setRealCommissionFee(packageInfo.getPlatCommissionFee());
            packageInfo.setRealCommissionScale(packageInfo.getPlatCommissionScale());
            this.save(packageInfo);
        }

    }

}
