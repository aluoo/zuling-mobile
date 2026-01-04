package com.zxtx.hummer.system.dao.exmapper;

import com.zxtx.hummer.system.domain.SysUserDO;
import com.zxtx.hummer.system.domain.UserDO;
import com.zxtx.hummer.system.shiro.LoginUser;
import com.zxtx.hummer.system.vo.UserFullInfVo;

import java.util.List;
import java.util.Map;

public interface SysUserExMapper {

    int insertEx(SysUserDO user);

    int delByEmployeeId(Long employeeId);

    Long getUserIdByEmployeeId(Long employeeId);

    int updateByEmployeeId(SysUserDO employeeId);

    UserDO get(Long userId);

    List<UserDO> list(Map<String, Object> map);

    List<UserFullInfVo> lsUserFullInfo(Map<String, Object> map);

    Long[] listAllDept();

    LoginUser getLoginUser(Long userId);


    int count(Map<String, Object> map);

    int save(UserDO user);

    int update(UserDO user);

    int remove(Long userId);


    int batchRemove(Long[] userIds);


    int removeByEmployeeId(Long employeeId);

}
