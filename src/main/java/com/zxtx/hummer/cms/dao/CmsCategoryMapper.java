package com.zxtx.hummer.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.cms.domain.CmsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Mapper
@Repository
public interface CmsCategoryMapper extends BaseMapper<CmsCategory> {
}