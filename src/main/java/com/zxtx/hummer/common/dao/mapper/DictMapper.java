package com.zxtx.hummer.common.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.common.domain.DictDO;
import com.zxtx.hummer.common.vo.Dicts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
public interface DictMapper extends BaseMapper<Dicts> {

    DictDO get(Long id);

    List<DictDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(DictDO dict);

    int update(DictDO dict);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<DictDO> listType();

    List<Dicts> getByType(@Param("type") String type);
}