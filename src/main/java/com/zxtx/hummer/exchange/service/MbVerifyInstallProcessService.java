package com.zxtx.hummer.exchange.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.company.dao.mapper.CompanyMapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.enums.CompanyStatus;
import com.zxtx.hummer.company.req.CompanyExchangeDTO;
import com.zxtx.hummer.company.service.CompanyChainInfoService;
import com.zxtx.hummer.company.vo.CompanyChainInfoDTO;
import com.zxtx.hummer.company.vo.CompanyExchangeVo;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.CompanyType;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.exchange.domain.MbExchangeEmployee;
import com.zxtx.hummer.exchange.domain.MbExchangeVerifyEmployee;
import com.zxtx.hummer.exchange.domain.MbVerifyInstall;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.req.ExchangeVerifyCompanyReq;
import com.zxtx.hummer.exchange.req.ExchangeVerifyDTO;
import com.zxtx.hummer.exchange.vo.ExchangeCompanyVO;
import com.zxtx.hummer.exchange.vo.ExchangeVerifyInstallVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 拉新安装包 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
public class MbVerifyInstallProcessService {

    @Autowired
    private MbVerifyInstallService verifyInstallService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private MbExchangeVerifyEmployeeService verifyEmployeeService;
    @Autowired
    private MbExchangeEmployeeService mbExchangeEmployeeService;
    @Autowired
    private MbExchangePhoneService exchangePhoneService;
    @Autowired
    private CompanyChainInfoService companyChainInfoService;

    public PageUtils<ExchangeVerifyInstallVO> selectPage(ExchangeInstallReq req) {
        List<ExchangeVerifyInstallVO> resultList = new ArrayList<>();
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<MbVerifyInstall> list = verifyInstallService.lambdaQuery()
                .like(StrUtil.isNotBlank(req.getName()), MbVerifyInstall::getName, req.getName())
                .like(StrUtil.isNotBlank(req.getChannelNo()), MbVerifyInstall::getChannelNo, req.getChannelNo())
                .eq(StrUtil.isNotBlank(req.getType()), MbVerifyInstall::getTypeCode, req.getType())
                .eq(req.getStatus()!=null, MbVerifyInstall::getStatus, req.getStatus())
                .orderByDesc(MbVerifyInstall::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if(CollUtil.isEmpty(list)) return  PageUtils.emptyPage();;

        resultList = BeanUtil.copyToList(list, ExchangeVerifyInstallVO.class);
        for(ExchangeVerifyInstallVO vo:resultList){
            //关联门店数目
            int num = verifyEmployeeService.getByStallId(vo.getId(),vo.getTypeCode());
            vo.setCompanyNum(num);
        }
        return new PageUtils(resultList, (int) page.getTotal());
    }


    /**
     * 代理底下的团队门店
     * @param req
     * @return
     */
    public PageUtils<ExchangeCompanyVO> companyList(ExchangeVerifyCompanyReq req) {
        List<ExchangeCompanyVO> resultList = new ArrayList<>();
        Employee employee = employeeMapper.selectById(req.getEmployeeId());
        List<Employee> employeeList = employeeMapper.selectList(Wrappers.lambdaQuery(Employee.class)
                .likeRight(Employee::getAncestors, employee.getAncestors())
                .eq(Employee::getDeptType, 1)
                .eq(Employee::getType, 1)
                .in(Employee::getCompanyType, Arrays.asList(CompanyType.STORE.getCode(), CompanyType.CHAIN.getCode()))
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));

        if(CollUtil.isEmpty(employeeList)) return PageUtils.emptyPage();

        List<Long> emIds = employeeList.stream().map(Employee::getId).collect(Collectors.toList());
        Map<Long, Employee> employeeMap =  employeeList.stream()
                .collect(Collectors.toMap(Employee::getId, Function.identity()));

        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Company> companyList = companyMapper.selectList(Wrappers.lambdaQuery(Company.class)
                .in(Company::getEmployeeId,emIds)
                .eq(Company::getStatus,CompanyStatus.NORMAL.getCode())
                .in(Company::getType,Arrays.asList(CompanyType.STORE.getCode(),CompanyType.CHAIN.getCode())));

        if(CollUtil.isEmpty(companyList)) return PageUtils.emptyPage();

        Map<Long, CompanyChainInfoDTO> companyChainInfoMap = companyChainInfoService.buildCompanyChainInfo(companyList);

        for (Company company : companyList) {
            ExchangeCompanyVO companyVO = new ExchangeCompanyVO();
            companyVO.setEmployeeId(company.getEmployeeId());
            companyVO.setCompanyId(company.getId());
            companyVO.setCompanyName(company.getName());
            companyVO.setContact(company.getContact());
            companyVO.setContactMobile(company.getContactMobile());
            Optional<CompanyChainInfoDTO> chainInfo = Optional.ofNullable(companyChainInfoMap.get(company.getEmployeeId()));
            companyVO.setBdId(chainInfo.map(CompanyChainInfoDTO::getBdId).orElse(null));
            companyVO.setAreaId(chainInfo.map(CompanyChainInfoDTO::getAreaId).orElse(null));
            companyVO.setAgentId(company.getEmployeeId());
            companyVO.setBdName(chainInfo.map(CompanyChainInfoDTO::getBdName).orElse(null));
            companyVO.setAreaName(chainInfo.map(CompanyChainInfoDTO::getAreaName).orElse(null));
            companyVO.setAgentName(chainInfo.map(CompanyChainInfoDTO::getAgentName).orElse(null));

            if(req.getVerifyFlag()){
                MbExchangeVerifyEmployee verifyEmployee = verifyEmployeeService.getByEmployeeId(company.getEmployeeId(),req.getTypeCode());
                companyVO.setVerifyFlag(ObjectUtil.isNotNull(verifyEmployee)?true:false);
                companyVO.setVerifyInstallName(ObjectUtil.isNotNull(verifyEmployee)?verifyEmployee.getExchangeVerifyName():"");
                companyVO.setVerifyInstallId(ObjectUtil.isNotNull(verifyEmployee)?verifyEmployee.getExchangeVerifyId():null);
            }else{
                MbExchangeEmployee verifyEmployee = mbExchangeEmployeeService.getByEmployeeId(company.getEmployeeId());
                companyVO.setVerifyFlag(ObjectUtil.isNotNull(verifyEmployee)?true:false);
                companyVO.setVerifyInstallName(ObjectUtil.isNotNull(verifyEmployee)?verifyEmployee.getExchangePhoneName():"");
                companyVO.setVerifyInstallId(ObjectUtil.isNotNull(verifyEmployee)?verifyEmployee.getExchangePhoneId():null);
            }

            resultList.add(companyVO);
        }

        return new PageUtils(resultList, (int) page.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    public void companySave(ExchangeVerifyDTO dto){

        if(CollUtil.isEmpty(dto.getCompanyList())){
            //删除旧的安装包的配置
            verifyEmployeeService.remove(Wrappers.lambdaQuery(MbExchangeVerifyEmployee.class)
                    .eq(MbExchangeVerifyEmployee::getExchangeVerifyId,dto.getExchangeVerifyId()));
            return;
        }

        List<Long> companyIds = dto.getCompanyList().stream().map(ExchangeVerifyDTO.VerifyCompany::getCompanyId).collect(Collectors.toList());

        //删除旧的安装包的配置
        verifyEmployeeService.remove(Wrappers.lambdaQuery(MbExchangeVerifyEmployee.class)
                .eq(MbExchangeVerifyEmployee::getExchangeVerifyId,dto.getExchangeVerifyId()));


        List<MbExchangeVerifyEmployee> saveList = new ArrayList<>();

        dto.getCompanyList().forEach(e->{
            MbExchangeVerifyEmployee verifyEmployee = new MbExchangeVerifyEmployee();
            BeanUtil.copyProperties(e,verifyEmployee);
            verifyEmployee.setVerifyTypeCode(dto.getTypeCode());
            verifyEmployee.setExchangeVerifyId(dto.getExchangeVerifyId());
            verifyEmployee.setExchangeVerifyName(dto.getExchangeVerifyName());
            saveList.add(verifyEmployee);
        });

        verifyEmployeeService.saveBatch(saveList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeVerify(Long verifyId){
        //删除验新包人员
        verifyEmployeeService.remove(Wrappers.lambdaQuery(MbExchangeVerifyEmployee.class)
                .eq(MbExchangeVerifyEmployee::getExchangeVerifyId,verifyId));
        //删除验新包
        verifyInstallService.removeById(verifyId);
    }

    public CompanyExchangeVo companyExchangePackage (Long id){
        CompanyExchangeVo resultVo = new CompanyExchangeVo();
        List<MbExchangeVerifyEmployee> verifyEmployee = verifyEmployeeService.lambdaQuery().eq(MbExchangeVerifyEmployee::getCompanyId,id).list();
        List<MbExchangeEmployee> exchangeEmployee = mbExchangeEmployeeService.lambdaQuery().eq(MbExchangeEmployee::getCompanyId,id).list();
        resultVo.setExchangePhoneId(exchangeEmployee.stream().map(MbExchangeEmployee::getExchangePhoneId).collect(Collectors.toList()));
        resultVo.setExchangeVerifyId(verifyEmployee.stream().map(MbExchangeVerifyEmployee::getExchangeVerifyId).collect(Collectors.toList()));
        return resultVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void companyExchangePackageSave(CompanyExchangeDTO dto){
        Company company = companyMapper.selectById(dto.getCompanyId());

        ExchangeCompanyVO companyVO = new ExchangeCompanyVO();
        companyVO.setEmployeeId(company.getEmployeeId());
        companyVO.setCompanyId(company.getId());
        companyVO.setCompanyName(company.getName());
        companyVO.setContact(company.getContact());
        companyVO.setContactMobile(company.getContactMobile());
        String [] levels = employeeMapper.selectById(company.getEmployeeId()).getAncestors().split(",");
        Long bdId = Long.valueOf(levels[1]);
        Long areaId = Long.valueOf(levels[2]);
        companyVO.setBdId(bdId);
        companyVO.setAreaId(areaId);
        companyVO.setAgentId(company.getEmployeeId());
        companyVO.setBdName(employeeMapper.selectById(companyVO.getBdId()).getName());
        companyVO.setAgentName(employeeMapper.selectById(companyVO.getAgentId()).getName());
        companyVO.setAreaName(employeeMapper.selectById(companyVO.getAreaId()).getName());

        //换机包替换
        if(CollUtil.isNotEmpty(dto.getExchangePhoneId())){
            //删除旧的关联
            mbExchangeEmployeeService.remove(Wrappers.lambdaQuery(MbExchangeEmployee.class)
                    .eq(MbExchangeEmployee::getCompanyId,dto.getCompanyId()));
            List<MbExchangeEmployee> saveList = new ArrayList<>();
            dto.getExchangePhoneId().forEach(e ->{
                MbExchangeEmployee exchangeEmployee = new MbExchangeEmployee();
                BeanUtil.copyProperties(companyVO,exchangeEmployee);
                exchangeEmployee.setExchangePhoneId(e);
                exchangeEmployee.setExchangePhoneName(exchangePhoneService.getById(e).getName());
                saveList.add(exchangeEmployee);
            });

            mbExchangeEmployeeService.saveBatch(saveList);
        }

        //验新包替换
        if(CollUtil.isNotEmpty(dto.getExchangeVerifyId())){
            //删除旧的关联
            verifyEmployeeService.remove(Wrappers.lambdaQuery(MbExchangeVerifyEmployee.class)
                    .eq(MbExchangeVerifyEmployee::getCompanyId,dto.getCompanyId()));

            List<MbExchangeVerifyEmployee> saveList = new ArrayList<>();
            dto.getExchangeVerifyId().forEach(e ->{
                MbExchangeVerifyEmployee exchangeEmployee = new MbExchangeVerifyEmployee();
                BeanUtil.copyProperties(companyVO,exchangeEmployee);
                exchangeEmployee.setExchangeVerifyId(e);
                exchangeEmployee.setExchangeVerifyName(verifyInstallService.getById(e).getName());
                exchangeEmployee.setVerifyTypeCode(verifyInstallService.getById(e).getTypeCode());
                saveList.add(exchangeEmployee);
            });
            verifyEmployeeService.saveBatch(saveList);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void exchangeCompanySave(ExchangeVerifyDTO dto){

        if(CollUtil.isEmpty(dto.getCompanyList())){
            //删除旧的安装包的配置
            mbExchangeEmployeeService.remove(Wrappers.lambdaQuery(MbExchangeEmployee.class)
                    .eq(MbExchangeEmployee::getExchangePhoneId,dto.getExchangeVerifyId()));
            return;
        }

        //删除旧的安装包的配置
        mbExchangeEmployeeService.remove(Wrappers.lambdaQuery(MbExchangeEmployee.class)
                .eq(MbExchangeEmployee::getExchangePhoneId,dto.getExchangeVerifyId()));


        List<MbExchangeEmployee> saveList = new ArrayList<>();

        dto.getCompanyList().forEach(e->{
            MbExchangeEmployee verifyEmployee = new MbExchangeEmployee();
            BeanUtil.copyProperties(e,verifyEmployee);
            verifyEmployee.setExchangePhoneId(dto.getExchangeVerifyId());
            verifyEmployee.setExchangePhoneName(dto.getExchangeVerifyName());
            saveList.add(verifyEmployee);
        });

        mbExchangeEmployeeService.saveBatch(saveList);
    }

    public List<ExchangeCompanyVO> packageCompanyList(ExchangeVerifyCompanyReq req) {
        List<ExchangeCompanyVO> resultList = new ArrayList<>();
        List<Long> cmpIds = new ArrayList<>();
        if(req.getVerifyFlag()){
            List<MbExchangeVerifyEmployee> packageList = verifyEmployeeService.getByInstallId(req.getPackageId(),req.getTypeCode());
            if(CollUtil.isEmpty(packageList)) return resultList;
            cmpIds = packageList.stream().map(MbExchangeVerifyEmployee::getCompanyId).collect(Collectors.toList());
        }else{
            List<MbExchangeEmployee> packageList = mbExchangeEmployeeService.getByInstallId(req.getPackageId());
            if(CollUtil.isEmpty(packageList)) return resultList;
            cmpIds = packageList.stream().map(MbExchangeEmployee::getCompanyId).collect(Collectors.toList());
        }

        List<Company> companyList = companyMapper.selectList(Wrappers.lambdaQuery(Company.class)
                .in(Company::getId,cmpIds)
                .eq(Company::getStatus,CompanyStatus.NORMAL.getCode())
                .in(Company::getType,Arrays.asList(CompanyType.STORE.getCode(),CompanyType.CHAIN.getCode())));

        if(CollUtil.isEmpty(companyList)) return resultList;

        Map<Long, CompanyChainInfoDTO> companyChainInfoMap = companyChainInfoService.buildCompanyChainInfo(companyList);

        for (Company company : companyList) {
            ExchangeCompanyVO companyVO = new ExchangeCompanyVO();
            companyVO.setEmployeeId(company.getEmployeeId());
            companyVO.setCompanyId(company.getId());
            companyVO.setCompanyName(company.getName());
            companyVO.setContact(company.getContact());
            companyVO.setContactMobile(company.getContactMobile());
            Optional<CompanyChainInfoDTO> chainInfo = Optional.ofNullable(companyChainInfoMap.get(company.getEmployeeId()));
            companyVO.setBdId(chainInfo.map(CompanyChainInfoDTO::getBdId).orElse(null));
            companyVO.setAreaId(chainInfo.map(CompanyChainInfoDTO::getAreaId).orElse(null));
            companyVO.setAgentId(company.getEmployeeId());
            companyVO.setBdName(chainInfo.map(CompanyChainInfoDTO::getBdName).orElse(null));
            companyVO.setAreaName(chainInfo.map(CompanyChainInfoDTO::getAreaName).orElse(null));
            companyVO.setAgentName(chainInfo.map(CompanyChainInfoDTO::getAgentName).orElse(null));
            resultList.add(companyVO);
        }

        return resultList;
    }




}