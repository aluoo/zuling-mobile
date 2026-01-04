package com.zxtx.hummer.system.service;

import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.system.dao.ProjectsDao;
import com.zxtx.hummer.system.domain.ProjectsDO;
import com.zxtx.hummer.system.enums.ProjectStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProjectsService {

    @Autowired
    private ProjectsDao projectsDao;

    public ProjectsDO get(Integer projectId) {
        return projectsDao.get(projectId);
    }

    public List<ProjectsDO> list(Map<String, Object> map) {
        return projectsDao.list(map);
    }

    public int count(Map<String, Object> map) {
        return projectsDao.count(map);
    }

    public int save(ProjectsDO projects) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buildCode", 0);
        List<ProjectsDO> projectslist = projectsDao.list(map);
        if (projectslist.size() < 1) {
            projects.setProjectCode(1);
        } else {
            projects.setProjectCode(projectslist.get(0).getProjectCode() + 1);//设置新的项目code
        }
        //获取创建人
        projects.setCreator(ShiroUtils.getUser().getUserName());
        projects.setCreatTime(new Date());
        projects.setBuildCode(0);//buildcode为0就是项目本身
        return projectsDao.save(projects);
    }


    @Transactional(rollbackFor = Exception.class)
    public int update(ProjectsDO projects) {

        if (projects.getStatus() == ProjectStatusEnum.NOW.getCode()) {//如果启用当前版本则把目前的版本更新为已过期
            projects.setOnlineTime(new Date());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("projectCode", projects.getProjectCode());
            ProjectsDO projectsByWeb = projectsDao.getFirst(map);//查询正在启用的最新版
            if (projectsByWeb != null) {
                projectsByWeb.setStatus(ProjectStatusEnum.EXPIRED.getCode());
                update(projectsByWeb);//将目前的最新版更新状态为已过期
            }

        }
        return projectsDao.update(projects);
    }


    @Transactional(rollbackFor = Exception.class)
    public int saveofson(ProjectsDO projects) {

        Map<String, Object> mapL = new HashMap<String, Object>();
        mapL.put("projectCode", projects.getProjectCode());
        ProjectsDO projects1 = projectsDao.getByProjectCode(projects.getProjectCode());
        projects.setProjectName(projects1.getProjectName());
        projects.setDevice(projects1.getDevice());
        projects.setCreator(ShiroUtils.getUser().getUserName());
        if (projects.getStatus() == ProjectStatusEnum.NOW.getCode()) {//如果启用当前版本则把目前的版本更新为已过期
            projects.setOnlineTime(new Date());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("projectCode", projects1.getProjectCode());
            ProjectsDO projectsByWeb = projectsDao.getFirst(map);//查询正在启用的最新版
            if (projectsByWeb != null) {
                projectsByWeb.setStatus(ProjectStatusEnum.EXPIRED.getCode());
                update(projectsByWeb);//将目前的最新版更新状态为已过期
            }

        }
        return projectsDao.save(projects);
    }


    public int remove(Integer projectId) {
        return projectsDao.remove(projectId);
    }

    public int batchRemove(Integer[] projectIds) {
        return projectsDao.batchRemove(projectIds);
    }

    public ProjectsDO getByApp(Map<String, Object> params) {
        return projectsDao.getByApp(params);
    }

    public ProjectsDO getByWeb(Map<String, Object> params) {
        return projectsDao.getByWeb(params);
    }

    public ProjectsDO getByWebV2(Map<String, Object> params) {
        return projectsDao.getByWebV2(params);
    }

    public List<ProjectsDO> getList(Map<String, Object> paramsList) {
        return projectsDao.getList(paramsList);
    }

    public List<ProjectsDO> getListV2(Map<String, Object> paramsList) {
        return projectsDao.getListV2(paramsList);
    }

    public List<ProjectsDO> listofson(Map<String, Object> map) {
        return projectsDao.listofson(map);
    }

    public int countofson(Map<String, Object> map) {
        return projectsDao.countofson(map);
    }


    public ProjectsDO getByCode(Integer projectCode) {
        return projectsDao.getByCode(projectCode);
    }

    public List<ProjectsDO> getFutureVersion(String onlineTime) {
        return projectsDao.getFutureVersion(onlineTime);
    }


}
