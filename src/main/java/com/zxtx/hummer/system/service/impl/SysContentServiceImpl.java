package com.zxtx.hummer.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.system.dao.mapper.SysContentMapper;
import com.zxtx.hummer.system.domain.SysContent;
import com.zxtx.hummer.system.enums.SysContentStatusEnum;
import com.zxtx.hummer.system.req.ContentQueryReq;
import com.zxtx.hummer.system.service.SysContentService;
import com.zxtx.hummer.system.vo.SysContentVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysContentServiceImpl extends ServiceImpl<SysContentMapper, SysContent> implements SysContentService {

    @Override
    public List<SysContentVo> selectPage(ContentQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        return this.getBaseMapper().selectPage(req);
    }

    @Override
    public void stop(Long id) {
        SysContent content = this.getById(id);
        if (ObjectUtil.isNull(content)) {
            throw new BaseException(99999, "公告不存在");
        }
        content.setStatus(SysContentStatusEnum.STOP.getCode());
        content.setUpdateTime(new Date());
        this.updateById(content);
    }

    @Override
    public void start(Long id) {
        SysContent content = this.getById(id);
        if (ObjectUtil.isNull(content)) {
            throw new BaseException(99999, "公告不存在");
        }
        if (content.getPulishDate().before(new Date())) {
            throw new BaseException(99999, "发布时间必须晚于当前时间");
        }
        content.setStatus(SysContentStatusEnum.WAIT.getCode());
        content.setUpdateTime(new Date());
        this.updateById(content);
    }

}
