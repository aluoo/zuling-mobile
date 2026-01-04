package com.zxtx.hummer.system.dao.exmapper;


import java.util.List;
import java.util.Map;

import com.zxtx.hummer.system.domain.ProjectsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author dongao
 * @email dongao@maoniuchuxing.net
 * @date 2018-06-25 15:15:49
 */
@Mapper
public interface ProjectsMapper {

    ProjectsDO get(Integer projectId);

    List<ProjectsDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(ProjectsDO projects);

    int update(ProjectsDO projects);

    int remove(Integer project_id);

    int batchRemove(Integer[] projectIds);

    ProjectsDO getByApp(Map<String, Object> params);

    ProjectsDO getByWeb(Map<String, Object> params);

    ProjectsDO getByWebV2(Map<String, Object> params);

    List<ProjectsDO> getList(Map<String, Object> paramsList);

    List<ProjectsDO> getListV2(Map<String, Object> paramsList);

    List<ProjectsDO> listofson(Map<String, Object> map);

    int countofson(Map<String, Object> map);

    ProjectsDO getByProjectCode(Integer projectCode);

    ProjectsDO getFirst(Map<String, Object> map);

    List<ProjectsDO> getFutureVersion(@Param("onlineTime") String onlineTime);
}
