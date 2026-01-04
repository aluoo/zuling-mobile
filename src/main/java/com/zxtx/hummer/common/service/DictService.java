package com.zxtx.hummer.common.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.common.dao.mapper.DictMapper;
import com.zxtx.hummer.common.domain.DictDO;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.common.utils.TimeUtil;
import com.zxtx.hummer.common.vo.DictMapVO;
import com.zxtx.hummer.common.vo.Dicts;
import com.zxtx.hummer.system.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class DictService {


    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public DictDO get(Long id) {
        return dictMapper.get(id);
    }

    public List<DictDO> list(Map<String, Object> map) {
        return dictMapper.list(map);
    }

    public int count(Map<String, Object> map) {
        return dictMapper.count(map);
    }

    public int save(DictDO dict) {
        return dictMapper.save(dict);
    }

    @Transactional
    public int update(DictDO dict) {
        int r = dictMapper.update(dict);
        String name = dict.getName();
        String byName = getByName(name);
        if (byName != null) {
            redisTemplate.opsForValue().set(Constants.dict_name_cache_prefix + name, byName);
        }
        if (StringUtils.isNotEmpty(dict.getType())) {
            List<Dicts> dicts = dictMapper.getByType(dict.getType());
            if (dicts.size() > 0) {
                redisTemplate.opsForValue().set(Constants.dict_type_cache_prefix + dict.getType(), JSON.toJSONString(dicts), TimeUtil.thisDaysMinute(), TimeUnit.MINUTES);
            }
        }

        return r;
    }


    public int remove(Long id) {
        return dictMapper.remove(id);
    }

    public int batchRemove(Long[] ids) {
        return dictMapper.batchRemove(ids);
    }


    public List<DictDO> listType() {
        return dictMapper.listType();
    }


    public Map<String, DictDO> getTypeList(String type) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        List<DictDO> lsDictDO = dictMapper.list(param);
        Map<String, DictDO> mapType2Do = new HashMap<>();
        if (lsDictDO != null) {
            for (DictDO dictDO : lsDictDO) {
                mapType2Do.put(dictDO.getValue(), dictDO);
            }
        }
        return mapType2Do;
    }


    public String getName(String type, String value) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        String rString = dictMapper.list(param).get(0).getName();
        return rString;
    }

    public String getByName(String name) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("name", name);
        List<DictDO> records = dictMapper.list(param);
        if (records != null && records.size() > 0) {
            return dictMapper.list(param).get(0).getValue();
        }
        return null;
    }

    public String getByNameWithCache(String name) {
        String s = redisTemplate.opsForValue().get(Constants.dict_name_cache_prefix + name);
        if (s != null) {
            return s;
        }
        String byName = getByName(name);
        if (byName != null) {
            redisTemplate.opsForValue().set(Constants.dict_name_cache_prefix + name, byName);
        }
        return byName;
    }

    public List<Dicts> getByTypeWithCache(String type) {
        String data = redisTemplate.opsForValue().get(Constants.dict_type_cache_prefix + type);
        if (data != null) {
            return JSONArray.parseArray(data, Dicts.class);
        }
        List<Dicts> dicts = dictMapper.getByType(type);
        if (dicts.size() > 0) {
            redisTemplate.opsForValue().set(Constants.dict_type_cache_prefix + type, JSON.toJSONString(dicts), TimeUtil.thisDaysMinute(), TimeUnit.MINUTES);
        }
        return dicts;
    }

    public List<DictDO> getHobbyList(UserDO userDO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "hobby");
        List<DictDO> hobbyList = dictMapper.list(param);

        if (StringUtils.isNotEmpty(userDO.getHobby())) {
            String userHobbys[] = userDO.getHobby().split(";");
            for (String userHobby : userHobbys) {
                for (DictDO hobby : hobbyList) {
                    if (!Objects.equals(userHobby, hobby.getId().toString())) {
                        continue;
                    }
                    hobby.setRemarks("true");
                    break;
                }
            }
        }

        return hobbyList;
    }

    public List<DictDO> getSexList() {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "sex");
        return dictMapper.list(param);
    }

    public List<DictDO> listByType(String type) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", type);
        return dictMapper.list(param);
    }

    public Map<String, String> getOperators() {
        List<DictDO> lsDicOpers = listByType(Constants.dic_type_oper);
        Map<String, String> mapId2Name = new HashMap<>();
        lsDicOpers.forEach(dictDO -> {
            JSONObject joValue = JSONObject.parseObject(dictDO.getValue());
            mapId2Name.put(joValue.getString("operatorId"), dictDO.getName());
        });
        return mapId2Name;
    }


    public List<Map<String, String>> listOperators() {

        List lsRet = new ArrayList();
        List<DictDO> lsDicOpers = listByType(Constants.dic_type_oper);
        Map<String, String> mapId2Name = new HashMap<>();
        lsDicOpers.forEach(dictDO -> {
            JSONObject joValue = JSONObject.parseObject(dictDO.getValue());
            Map<String, String> mapObj = new HashMap<>();
            mapObj.put("id", joValue.getString("operatorId"));
            mapObj.put("name", dictDO.getName());
            lsRet.add(mapObj);
        });
        return lsRet;
    }

    public DictMapVO getNameMap(String name) {
        Map<String, String> resp = new HashMap<>();
        String value = getByName(name);
        if (StrUtil.isNotBlank(value)) {
            resp = JSONUtil.toBean(value, new TypeReference<Map<String, String>>() {
            }, true);
        }
        DictMapVO dictMapVO = new DictMapVO();
        dictMapVO.setResultMap(resp);
        return dictMapVO;
    }


}
