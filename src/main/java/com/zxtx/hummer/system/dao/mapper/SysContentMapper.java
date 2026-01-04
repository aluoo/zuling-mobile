package com.zxtx.hummer.system.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.system.domain.SysContent;
import com.zxtx.hummer.system.req.ContentQueryReq;
import com.zxtx.hummer.system.vo.SysContentVo;

import java.util.List;

/**
 * @author chenjian
 * @create 2023/7/17 15:17
 * @desc 公告
 **/
public interface SysContentMapper extends BaseMapper<SysContent> {

    List<SysContentVo> selectPage(ContentQueryReq req);

}
