package com.zxtx.hummer.company.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zxtx.hummer.company.dao.mapper.CompanyMapper;
import com.zxtx.hummer.company.dao.mapper.ExtCompanyMapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.domain.CompanyExample;
import com.zxtx.hummer.company.enums.CompanyStatus;
import com.zxtx.hummer.company.vo.ComListRes;
import com.zxtx.hummer.company.vo.CompanyListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class CompanyDao {
    @Autowired
    private CompanyMapper mapper;
    @Autowired
    private ExtCompanyMapper extCompanyMapper;

    public List<Company> selectAll() {
        CompanyExample example = new CompanyExample();
        example.createCriteria().andStatusEqualTo(CompanyStatus.NORMAL.getCode());
        return mapper.selectByExample(example);
    }

    public List<ComListRes> listByCondition(CompanyListReq req) {
        return extCompanyMapper.listByCondition(req);
    }

    public int updateStatus(Company company, String updator, CompanyStatus status) {
        LambdaUpdateWrapper<Company> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.set(Company::getStatus, status.getCode());
        updateWrapper.set(Company::getUpdator, updator);
        updateWrapper.set(Company::getUpdateTime, new Date());
        updateWrapper.eq(Company::getId, company.getId());
        updateWrapper.eq(Company::getStatus, CompanyStatus.TO_AUDIT.getCode());
        return mapper.update(null, updateWrapper);
    }


    public Company getByName(String name) {
        CompanyExample example = new CompanyExample();
        example.createCriteria().andNameEqualTo(name).andStatusEqualTo(CompanyStatus.NORMAL.getCode());
        List<Company> companies = mapper.selectByExample(example);
        if (companies.size() > 0) {
            return companies.get(0);
        }
        return null;
    }


    public Company getByName(String name, boolean hadOffline) {
        CompanyExample example = new CompanyExample();
        if (hadOffline) {
            example.createCriteria().andNameEqualTo(name).andStatusIn(Arrays.asList(new Byte[]{CompanyStatus.NORMAL.getCode(),
                    CompanyStatus.OFFLINE.getCode()}));
        } else {
            example.createCriteria().andNameEqualTo(name).andStatusEqualTo(CompanyStatus.NORMAL.getCode());
        }
        List<Company> companies = mapper.selectByExample(example);
        if (companies.size() > 0) {
            return companies.get(0);
        }
        return null;
    }

    public void offlineChannel(String code, String updator) {
        Company company = new Company();
        company.setStatus(CompanyStatus.OFFLINE.getCode());
        company.setUpdator(updator);
        company.setUpdateTime(new Date());
        extCompanyMapper.changeChannelStatus(code, company, CompanyStatus.NORMAL.getCode());
    }

    public void cancelChannel(String code, String updator) {
        Company company = new Company();
        company.setStatus(CompanyStatus.CANCEL.getCode());
        company.setUpdator(updator);
        company.setUpdateTime(new Date());
        extCompanyMapper.changeChannelStatus(code, company, CompanyStatus.NORMAL.getCode());
    }


    public void onlineChannel(String code, String updator) {
        Company company = new Company();
        company.setStatus(CompanyStatus.NORMAL.getCode());
        company.setUpdator(updator);
        company.setUpdateTime(new Date());
        extCompanyMapper.changeChannelStatus(code, company, CompanyStatus.OFFLINE.getCode());
    }

}
