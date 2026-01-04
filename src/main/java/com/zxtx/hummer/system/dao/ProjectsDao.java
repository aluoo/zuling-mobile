package com.zxtx.hummer.system.dao;


import com.zxtx.hummer.system.dao.exmapper.ProjectsMapper;
import com.zxtx.hummer.system.domain.ProjectsDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @author dongao
 * @email dongao@maoniuchuxing.net
 * @date 2018-06-25 15:15:49
 */
@Repository
public class ProjectsDao {

    @Autowired
    private ProjectsMapper projectsMapper;


    public ProjectsDO get(Integer projectId) {
        return projectsMapper.get(projectId);
    }


    public List<ProjectsDO> list(Map<String, Object> map) {
        return projectsMapper.list(map);
    }

    public int count(Map<String, Object> map) {
        return projectsMapper.count(map);
    }

    public int save(ProjectsDO projects) {
        return projectsMapper.save(projects);
    }

    public int update(ProjectsDO projects) {
        return projectsMapper.update(projects);
    }

    public int remove(Integer projectId) {
        return projectsMapper.remove(projectId);
    }

    public int batchRemove(Integer[] projectIds) {
        return projectsMapper.batchRemove(projectIds);
    }


    public ProjectsDO getByApp(Map<String, Object> params) {
        return projectsMapper.getByApp(params);
    }


    public ProjectsDO getByWeb(Map<String, Object> params) {
        return projectsMapper.getByWeb(params);
    }

    public ProjectsDO getByWebV2(Map<String, Object> params) {
        return projectsMapper.getByWebV2(params);
    }


    public List<ProjectsDO> getList(Map<String, Object> paramsList) {
        return projectsMapper.getList(paramsList);
    }

    public List<ProjectsDO> getListV2(Map<String, Object> paramsList) {
        return projectsMapper.getListV2(paramsList);
    }


    public List<ProjectsDO> listofson(Map<String, Object> map) {
        return projectsMapper.listofson(map);
    }


    public int countofson(Map<String, Object> map) {
        return projectsMapper.countofson(map);
    }


    public ProjectsDO getByProjectCode(Integer projectCode) {
        return projectsMapper.getByProjectCode(projectCode);
    }


    public ProjectsDO getFirst(Map<String, Object> map) {
        return projectsMapper.getFirst(map);
    }


    public ProjectsDO getByCode(Integer projectCode) {
        return projectsMapper.getByProjectCode(projectCode);
    }

    public List<ProjectsDO> getFutureVersion(String onlineTime) {
        return projectsMapper.getFutureVersion(onlineTime);
    }

}
