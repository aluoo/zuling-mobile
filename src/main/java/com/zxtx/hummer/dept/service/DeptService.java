package com.zxtx.hummer.dept.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.dept.dao.DeptDao;
import com.zxtx.hummer.dept.dao.RealDeptDao;
import com.zxtx.hummer.dept.dao.mapper.DeptMapper;
import com.zxtx.hummer.dept.domain.Dept;
import com.zxtx.hummer.dept.enums.DeptStatus;
import com.zxtx.hummer.dept.enums.DeptType;
import com.zxtx.hummer.dept.vo.CreateDeptReq;
import com.zxtx.hummer.dept.vo.DeptListRs;
import com.zxtx.hummer.dept.vo.DeptRes;
import com.zxtx.hummer.dept.vo.UpdateDeptReq;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.em.enums.EmType;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.em.vo.CheckEmDataRs;
import com.zxtx.hummer.system.shiro.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class DeptService {

    @Autowired
    private RealDeptDao realDeptDao;
    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private EmService emService;


    @Autowired
    private DeptDao deptDao;


    private static int retrySaveDetpTimes = 3;

    private static final Logger logger = LoggerFactory.getLogger(DeptService.class);


    @Transactional
    public Dept createDeptOfChannel(Company data, Employee creator) {
        Dept dept = buildDeptOfChannel(data, creator);
        int times = retrySaveDetpTimes;
        Dept pDept = getDeptById(data.getPDeptId());
        doSave(dept, pDept.getId(), pDept.getCode(), times);
        return dept;
    }


    private Dept buildDeptOfChannel(Company data, Employee employee) {
        Dept toSave = new Dept();
        toSave.setCreateTime(new Date());
        toSave.setCompanyId(data.getId());
        toSave.setCompanyType(data.getType());
        toSave.setCreator(employee.getName());
        toSave.setId(SnowflakeIdWorker.nextID());
        toSave.setName(data.getName() + "-管理部");
        /*toSave.setpDeptId(employee.getDeptId());*/
        //渠道的归属部门
        toSave.setpDeptId(data.getPDeptId());
        toSave.setType(DeptType.MANGER.getCode());
        toSave.setUpdateTime(new Date());
        toSave.setUpdator(employee.getName());
        toSave.setStatus(DeptStatus.NORMAL.getCode());
        return toSave;
    }

    private void doSave(Dept dept, Long parentId, String parentCode, int retryTimes) {
        try {
            dept.setCode(generateCode(parentId, parentCode));
            deptMapper.insert(dept);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            retryTimes--;
            if (retryTimes == 0) {
                throw new BusinessException(BizError.CREATE_DEPT_ERROR);
            }
            //一旦出现bug就会死循环，需要设置一个重试的最大次数
            doSave(dept, parentId, parentCode, retryTimes);
        }
    }

    public String generateCode(Long pdeptId, String pdeptCode) {
        Integer newCode = realDeptDao.selectMaxCode(pdeptId);
        newCode++;
        return pdeptCode + "-" + newCode;
    }


    public void update(Dept dept, String updator) {
        dept.setUpdateTime(new Date());
        dept.setUpdator(updator);
        deptMapper.updateByPrimaryKeySelective(dept);
    }

   /*
    public Dept getManagerDept(Long companyId) {
        return deptDao.getManagerDept(companyId);
    }
    public Dept getManagerDept(Long companyId,int status) {
        return deptDao.getManagerDept(companyId,status);
    }*/


    public List<DeptListRs> getChildDepts(Long companyId, Long deptId) {
        return deptDao.getChildDepts(companyId, deptId);
    }

    public List<DeptListRs> getAllChildDepts(Long companyId, String deptCode) {
        return deptDao.getAllChildDepts(companyId, deptCode + "-");
    }


    public List<DeptListRs> getAllStatusChildDepts(Long companyId, String deptCode) {
        return deptDao.getAllStatusChildDepts(companyId, deptCode);
    }


    public Dept getManagerDept(Long companyId, List<Integer> lsStatus) {
        return deptDao.getManagerDept(companyId, lsStatus);
    }

    public Dept getDeptById(Long deptId) {
        return deptMapper.selectByPrimaryKey(deptId);
    }


    public void offlineChannelDept(String code, LoginUser user) {
        deptDao.offlineChannelDept(code, user);
    }

    public void cancelChannelDept(String code, LoginUser user) {
        deptDao.cancelChannelDept(code, user);
    }


    public void onlineChannelDept(String code, LoginUser user) {
        deptDao.onlineChannelDept(code, user);
    }


    public boolean deptExist(Long companyId, String name) {
        return selectByName(companyId, name) != null;
    }


    public Dept selectByName(Long companyId, String name) {
        return deptDao.getByName(companyId, name);
    }

    public List<Long> queryCmpIdByCode(String code) {
        return deptDao.queryCmpIdByCode(code);
    }

    public Dept getById(Long deptId) {
        return deptMapper.selectByPrimaryKey(deptId);
    }


    @Transactional
    public void updateChildsCode(String newPcode, String oldPcode, List cmpIds, Long newCmpId, Integer newCompanyType) {

        deptDao.updateChildsCode(newPcode, oldPcode, cmpIds, new Date(), ShiroUtils.getUser().getName(), newCmpId, newCompanyType);
    }


    private Dept checkChgCmpAndRetNewCmpDept(Dept dept, Long newPDeptId) {
        boolean chgToNewCmpFlag = false;
        Dept newPDept = null;
        if (newPDeptId != null) {
            if (!newPDeptId.equals(dept.getpDeptId())) {
                newPDept = getById(newPDeptId);
                chgToNewCmpFlag = !newPDept.getCompanyId().equals(dept.getCompanyId());
            }
        }
        if (chgToNewCmpFlag) { //切换渠道
            //1.老部门下不能有渠道
            List<Long> cmpIds = queryCmpIdByCode(dept.getCode());


            if (cmpIds.size() > 0) {
                cmpIds.forEach(cmpId -> {
                    if (!cmpId.equals(dept.getCompanyId())) {
                        throw new BaseException(BizError.DPT_SUBCMP_EXIST);
                    }
                });

            }
            return newPDept;
        }
        return null;
    }


    @Transactional
    public DeptRes updateDept(UpdateDeptReq req) {

        Dept dept = getById(req.getDeptId());

        //LoginUser user = LoginUserContext.getUser();
        DeptRes deptRes = new DeptRes();
        if (dept == null || !dept.getStatus().equals(DeptStatus.NORMAL.getCode())) {
            throw new BusinessException(BizError.DEPT_NOT_EXIST);
        }
        if (dept.getType() == DeptType.MANGER.getCode()) {
            throw new BusinessException(BizError.DEPT_MAGER_CANNOT_MOV);
        }
        if (StringUtils.hasLength(req.getMobile()) && StringUtils.isEmpty(req.getManagerName())) {
            throw new BusinessException(BizError.Name_empty);
        }
        if (StringUtils.hasLength(req.getManagerName()) && StringUtils.isEmpty(req.getMobile())) {
            throw new BusinessException(BizError.Mobile_EMPTY);
        }
        //切换了渠道
        Dept chgToNewCmpDept = null;
        Long newPDeptId = req.getPdeptId();
        if (newPDeptId != null) { //切换渠道检查
            chgToNewCmpDept = checkChgCmpAndRetNewCmpDept(dept, newPDeptId);
        }
        boolean chgCmpFlag = (chgToNewCmpDept != null);
        Long chgToCmpId = chgToNewCmpDept != null ? chgToNewCmpDept.getCompanyId() : dept.getCompanyId();
        Integer chgToCmpType = chgToNewCmpDept != null ? chgToNewCmpDept.getCompanyType() : dept.getCompanyType();

        if (!StringUtils.isEmpty(req.getName())) {
            if (!dept.getName().equals(req.getName())) {
                if (deptExist(chgToCmpId, req.getName())) {
                    throw new BusinessException(BizError.DEPT_EXIST);
                }
                dept.setName(req.getName());
            }
        }
        //更新部门
        if (!dept.getpDeptId().equals(req.getPdeptId()) && req.getPdeptId() != null) {
            Dept pdept = getById(req.getPdeptId());
            updatePdeptOfDept(dept, pdept, chgToCmpId, chgToCmpType);
        }

        //原管理员--
        Employee oldManager = emService.getManager(dept);
        LoginUser user = ShiroUtils.getUser();
        //变更了管理员
        if ((oldManager == null && StringUtils.hasLength(req.getMobile()))
                || (oldManager != null && StringUtils.hasLength(req.getMobile())
                && ((!oldManager.getMobileNumber().equals(req.getMobile())) || !oldManager.getName().equals(req.getManagerName()))
        )
        ) {
            CheckEmDataRs checkEmDataRs = new CheckEmDataRs();
            if (StringUtils.hasLength(req.getMobile())) {
                checkEmDataRs = checkEmData(req);
            }
            //将原有管理员换成普通员工

            if (oldManager != null && !oldManager.getMobileNumber().equals(req.getMobile())) {
                doChangeEmRole(dept, user, EmType.CM_EM, oldManager);
                //要求重新登录
                emService.logout(oldManager.getId());
            }
            // 创建员工
            if (checkEmDataRs.isCreateEm()) {
                emService.save(buildEm(dept, user.getName(), req.getManagerName(), req.getMobile(), EmType.CM_MANGER.getCode()));
            }
            // 修改员工角色
            if (checkEmDataRs.isChangeEmRole()) {
                doChangeEmRole(dept, user, EmType.CM_MANGER, checkEmDataRs.getEmployee());
                //要求重新登录
                emService.logout(checkEmDataRs.getEmployee().getId());
            }
        }
        dept.setUpdator(ShiroUtils.getUser().getName());
        update(dept, ShiroUtils.getUser().getName());
        return deptRes;
    }


    //dept 部门  ,pdept父部门  newCmpId 新渠道
    private void updatePdeptOfDept(Dept dept, Dept pdept, Long newCmpId, Integer newCompanyType) {
        //不能将自己设置为自己的所属部门
        if (dept.getCode().equals(pdept.getCode())) {
            throw new BusinessException(BizError.ERROR_PARENT_DEPT);
        }

        // LoginUser loginUser = LoginUserContext.getUser();

        String oldCode = dept.getCode();
        //部门下的所有渠道
        List<Long> cmpIds = queryCmpIdByCode(oldCode);
        cmpIds.add(dept.getCompanyId());
        String newCode = generateCode(pdept.getId(), pdept.getCode());
        dept.setpDeptId(pdept.getId());
        dept.setCode(newCode);
        dept.setCompanyId(newCmpId);
        dept.setCompanyType(newCompanyType);

        update(dept, ShiroUtils.getUser().getName());


        //修改所有子部门
        updateChildsCode(newCode, oldCode, cmpIds, newCmpId, newCompanyType);

        //修改员工code
        emService.updateDeptCode(cmpIds, oldCode, new Date(), ShiroUtils.getUser().getName(), newCmpId, newCompanyType);

      /*  //刷新员工redis 中token对应的数据
        emService.updateRedisEm(newCode);*/
        //重新登录
        emService.logoutEmpsByDeptCode(newCode);
    }


    private void doChangeEmRole(Dept dept, LoginUser user, EmType type, Employee employee) {
        if (employee == null) {
            return;
        }
        employee.setDeptId(dept.getId());
        employee.setUpdateTime(new Date());
        employee.setUpdator(user.getName());
        employee.setType(type.getCode());
        employee.setDeptType(dept.getType());
        employee.setDeptCode(dept.getCode());
        emService.updateEm(employee);
    }


    private Employee buildEm(Dept dept, String creator, String name, String mobile, int type) {
        Employee data = new Employee();
        data.setId(SnowflakeIdWorker.nextID());
        data.setCompanyId(dept.getCompanyId());
        data.setCompanyType(dept.getCompanyType());
        data.setCreateTime(new Date());
        data.setCreator(creator);
        data.setDeptCode(dept.getCode());
        data.setDeptType(dept.getType());
        data.setType(type);
        data.setUpdator(creator);
        data.setStatus(EmStatus.NORMAL.getCode());
        data.setUpdateTime(new Date());
        data.setMobileNumber(mobile);
        data.setName(name);
        data.setDeptId(dept.getId());
        return data;
    }


    private CheckEmDataRs checkEmData(CreateDeptReq req) {
        CheckEmDataRs checkEmDataRs = new CheckEmDataRs();
        Employee employee = emService.getByMobile(req.getMobile(), Arrays.asList(EmStatus.NORMAL.getCode(),
                EmStatus.OFFLINE.getCode()));
        checkEmDataRs.setEmployee(employee);
        //管理员是已存在的员工
        if (employee != null && employee.getStatus() == EmStatus.NORMAL.getCode()) {
            // 员工电话，姓名是否匹配
            if (!employee.getName().equals(req.getManagerName())) {
                throw new BusinessException(BizError.ID_NAME_NOT_MATCH);
            }
            // 是否是其他部门管理员
            if ((employee.getType().equals(EmType.CM_MANGER.getCode()) || employee.getType().equals(EmType.MANGER_MANGER.getCode()))) {
                //暂时不支持将其他部门管理员设置为新的管理员
                throw new BusinessException(BizError.REPLACE_MANAGER);
            } else {
                checkEmDataRs.setChangeEmRole(true);
            }
        } else if (employee != null && employee.getStatus() == EmStatus.OFFLINE.getCode()) {
            throw new BusinessException(BizError.EM_FREEZE);
        } else {
            checkEmDataRs.setCreateEm(true);
        }
        return checkEmDataRs;
    }


    public Dept getDeptByCompanyIdAndCode(Long companyId, String deptCode) {

        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<Dept>()
                .eq(Dept::getCompanyId, companyId)
                .eq(Dept::getCode, deptCode);

        Dept dept = deptMapper.selectOne(queryWrapper);
        return dept;
    }

    public Dept getCompanyManagerDept(Long companyId) {

        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<Dept>()
                .eq(Dept::getCompanyId, companyId)
                .eq(Dept::getType, DeptType.MANGER.getCode());

        Dept dept = deptMapper.selectOne(queryWrapper);
        return dept;
    }
}
