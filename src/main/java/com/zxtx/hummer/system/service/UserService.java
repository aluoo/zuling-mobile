package com.zxtx.hummer.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.common.config.BootdoConfig;
import com.zxtx.hummer.common.domain.FileDO;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.service.FileService;
import com.zxtx.hummer.common.service.SysAuditService;
import com.zxtx.hummer.common.utils.*;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.system.dao.exmapper.SysUserExMapper;
import com.zxtx.hummer.system.dao.mapper.UserRoleMapper;
import com.zxtx.hummer.system.domain.UserDO;
import com.zxtx.hummer.system.domain.UserRoleDO;
import com.zxtx.hummer.system.shiro.LoginUser;
import com.zxtx.hummer.system.vo.UserFullInfVo;
import com.zxtx.hummer.system.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService {

    @Autowired
    private SysUserExMapper sysUserExMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    private FileService sysFileService;
    @Autowired
    private BootdoConfig bootdoConfig;

//    @Autowired
//    private SmsSender smsSender;

    @Autowired
    private SysAuditService sysAuditService;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RedisLockService redisLockService;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Employee getEmployee(UserDO user) {
        if (user == null || user.getEmployeeId() == null) {
            return null;
        }
        return employeeMapper.selectById(user.getEmployeeId());
    }

    public Employee getEmployeeByMobileNumber(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return null;
        }
        return employeeMapper.selectOne(new LambdaQueryWrapper<Employee>().eq(Employee::getMobileNumber, mobile).eq(Employee::getStatus, EmStatus.NORMAL.getCode()));
    }

    //    @Cacheable(value = "user",key = "#id")
    public UserDO get(Long id) {
        List<Long> roleIds = userRoleMapper.listRoleId(id);
        UserDO user = sysUserExMapper.get(id);
        /*   user.setDeptName(deptMapper.get(user.getDeptId()).getName());*/
        user.setRoleIds(roleIds);
        return user;
    }

    public long getUserIdByEmployeeId(Long employeeId) {

        return sysUserExMapper.getUserIdByEmployeeId(employeeId);
    }


    public List<UserFullInfVo> lsUserFullInfo(Map<String, Object> map) {
        List<UserFullInfVo> list = sysUserExMapper.lsUserFullInfo(map);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<Long> userIds = list.stream().map(UserFullInfVo::getUserId).collect(Collectors.toList());
        List<Long> subAdminIds = userRoleMapper.listUserIdByRoleSign(userIds, "subAdmin");
        list.forEach(o -> {
            o.setHasSubAdminRole(false);
            if (CollUtil.isNotEmpty(subAdminIds) && subAdminIds.contains(o.getUserId())) {
                o.setHasSubAdminRole(true);
            }
        });
        return list;
    }

    public List<UserDO> list(Map<String, Object> map) {
        String deptId = (String) map.get("deptId");
        if (StringUtils.isNotBlank(deptId)) {
            Long deptIdl = Long.valueOf(deptId);
            //TODO
            //  List<Long> childIds = deptService.listChildrenIds(deptIdl);
            /*childIds.add(deptIdl);
            map.put("deptId", null);
            map.put("deptIds",childIds);*/
        }
        return sysUserExMapper.list(map);
    }

    public int count(Map<String, Object> map) {
        return sysUserExMapper.count(map);
    }

    private void checkExist(UserDO user) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 1);
        map.put("username", user.getUsername());
        map.put("mobile", user.getMobile());
        if (user.getUserId() != null) {
            map.put("oldUserId", user.getUserId());
        }
        int count = sysUserExMapper.count(map);
        if (count > 0) {
            throw new BaseException(-1, "手机号已存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int save(UserDO user) {
        redisLockService.redisLock("pc_save_sys_user", Long.valueOf(user.getMobile()));
        user.setUsername(user.getMobile());
        String pwd = user.getPassword();
        user.setPassword(MD5Utils.encrypt(pwd));
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        user.setStatus(1);
        user.setEmployeeId(user.getEmployeeId());

        checkExist(user);

        int count = sysUserExMapper.save(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRoleDO ur = new UserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            userRoleMapper.batchSave(list);
        }
//        SmsMessage smsMessage = SmsMessage.create().
//                addParam("account", user.getUsername())
//                .addParam("pwd", pwd)
//                .mobile(user.getMobile())
//                .templateCode(SMSEnum.sys_user_add.getTemplateCode());
//        smsSender.send(smsMessage);
        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(UserDO user) {
        redisLockService.redisLock("pc_update_sys_user", Long.valueOf(user.getMobile()));
        user.setUsername(user.getMobile());
        Long userId = user.getUserId();
        UserDO oldUser = sysUserExMapper.get(userId);
        boolean userNameChangeFlag = false;
        if (!oldUser.getUsername().equals(user.getUsername())) {
            userNameChangeFlag = true;
        }
        user.setEmployeeId(user.getEmployeeId());

        checkExist(user);

        int r = sysUserExMapper.update(user);
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRoleDO ur = new UserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            userRoleMapper.batchSave(list);
        }
        if (userNameChangeFlag) {
            //写审核日志
            sysAuditService.addSysAudit("修改用户名", oldUser.getUsername(), user.getUsername());
        }
        return r;
    }


    //    @CacheEvict(value = "user")
    public int remove(Long userId) {
        userRoleMapper.removeByUserId(userId);
        return sysUserExMapper.remove(userId);
    }

    public int removeByEmployeeId(Long employeeId) {
        Long userId = sysUserExMapper.getUserIdByEmployeeId(employeeId);
        userRoleMapper.removeByUserId(userId);
        return sysUserExMapper.removeByEmployeeId(employeeId);
    }

    public boolean exit(Map<String, Object> params) {
        boolean exit;
        exit = sysUserExMapper.list(params).size() > 0;
        return exit;
    }


    public int resetPwd(UserVO userVO, LoginUser loginUser) throws Exception {
        if (Objects.equals(userVO.getUserDO().getUserId(), loginUser.getUserId())) {
            if (Objects.equals(MD5Utils.encrypt(userVO.getPwdOld()), loginUser.getPassword())) {
                loginUser.setPassword(MD5Utils.encrypt(userVO.getPwdNew()));
                UserDO userDO = new UserDO();
                userDO.setPassword(loginUser.getPassword());
                userDO.setUserId(loginUser.getUserId());
                return sysUserExMapper.update(userDO);
            } else {
                throw new Exception("输入的旧密码有误！");
            }
        } else {
            throw new Exception("你修改的不是你登录的账号！");
        }
    }

    public int adminResetPwd(UserVO userVO) throws Exception {
        UserDO userDO = get(userVO.getUserDO().getUserId());
        if ("admin".equals(userDO.getUsername())) {
            throw new Exception("超级管理员的账号不允许直接重置！");
        }
        userDO.setPassword(MD5Utils.encrypt(userVO.getPwdNew()));
//        SmsMessage smsMessage = SmsMessage.create().
//                addParam("account", userDO.getUsername())
//                .addParam("pwd", userVO.getPwdNew())
//                .mobile(userDO.getMobile())
//                .templateCode(SMSEnum.sys_user_pwd_reset.getTemplateCode());
//        smsSender.send(smsMessage);

        return sysUserExMapper.update(userDO);
    }


    @Transactional
    public int batchremove(Long[] userIds) {
        int count = sysUserExMapper.batchRemove(userIds);
        userRoleMapper.batchRemoveByUserId(userIds);
        return count;
    }

    public int updatePersonal(UserDO userDO) {
        return sysUserExMapper.update(userDO);
    }

    public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception {
        String fileName = file.getOriginalFilename();
        fileName = FileUtil.renameToUUID(fileName);
        FileDO sysFile = new FileDO(FileType.fileType(fileName), "/files/" + fileName, new Date());
        //获取图片后缀
        String prefix = fileName.substring((fileName.lastIndexOf(".") + 1));
        String[] str = avatar_data.split(",");
        //获取截取的x坐标
        int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
        //获取截取的y坐标
        int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
        //获取截取的高度
        int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
        //获取截取的宽度
        int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
        //获取旋转的角度
        int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
        try {
            BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
            BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(rotateImage, prefix, out);
            //转换后存入数据库
            byte[] b = out.toByteArray();
            FileUtil.uploadFile(b, bootdoConfig.getUploadPath(), fileName);
        } catch (Exception e) {
            throw new Exception("图片裁剪错误！！");
        }
        Map<String, Object> result = new HashMap<>();
        if (sysFileService.save(sysFile) > 0) {
            UserDO userDO = new UserDO();
            userDO.setUserId(userId);
            userDO.setPicId(sysFile.getId());
            if (sysUserExMapper.update(userDO) > 0) {
                result.put("url", sysFile.getUrl());
            }
        }
        return result;
    }


}