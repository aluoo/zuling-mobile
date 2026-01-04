package com.zxtx.hummer.mbr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.mbr.domain.MbrPreOrder;
import com.zxtx.hummer.mbr.req.MbrPreOrderReq;
import com.zxtx.hummer.mbr.response.MbrPreOrderVO;
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
public interface MbrPreOrderMapper extends BaseMapper<MbrPreOrder> {

    List<MbrPreOrderVO> selectBySearch (@Param("req") MbrPreOrderReq mbrPreOrderReq);

}