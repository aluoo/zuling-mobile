package com.zxtx.hummer.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.system.domain.SysContent;
import com.zxtx.hummer.system.req.ContentQueryReq;
import com.zxtx.hummer.system.vo.SysContentVo;

import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/23 11:14
 * @desc
 **/

public interface SysContentService extends IService<SysContent> {

    List<SysContentVo> selectPage(ContentQueryReq req);

    /**
     * 停用
     *
     * @param id
     */
    void stop(Long id);

    /**
     * 启用
     *
     * @param id
     */
    void start(Long id);

}
