package com.zxtx.hummer.mbr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.mbr.domain.MbrOrder;
import com.zxtx.hummer.mbr.req.MbrOrderReq;
import com.zxtx.hummer.mbr.response.MbrOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数保产品选项表 Mapper 接口
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Mapper
public interface MbrOrderMapper extends BaseMapper<MbrOrder> {

    List<MbrOrderVO> selectBySearch (@Param("req") MbrOrderReq mbrOrderReq);

}