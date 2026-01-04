package com.zxtx.hummer.common.service;

import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.dao.mapper.SysAuditMapper;
import com.zxtx.hummer.common.domain.SysAudit;
import com.zxtx.hummer.common.domain.SysAuditExample;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.common.vo.PageReq;
import com.zxtx.hummer.common.vo.SysAuditReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysAuditService {

    @Autowired
    private SysAuditMapper sysAuditMapper;


    public SysAudit getSysAudit(Long id) {

        return sysAuditMapper.selectByPrimaryKey(id);
    }


    public PageUtils lsSysAudits(SysAuditReq sysAuditReq, PageReq pageReq) {

        SysAuditExample sysAuditExample = new SysAuditExample();
        SysAuditExample.Criteria criteria = sysAuditExample.createCriteria();
        if (null != sysAuditReq.getStart()) {
            criteria.andCreateTimeGreaterThanOrEqualTo(sysAuditReq.getStart());
        }
        if (null != sysAuditReq.getEnd()) {
            criteria.andCreateTimeLessThanOrEqualTo(sysAuditReq.getEnd());
        }
        if (null != sysAuditReq.getUserId()) {
            criteria.andUserIdEqualTo(sysAuditReq.getUserId());
        }
        if (StringUtils.isNotEmpty(sysAuditReq.getUsername())) {
            criteria.andUsernameLike(sysAuditReq.getUsername());
        }
        if (StringUtils.isNotEmpty(sysAuditReq.getOperation())) {
            criteria.andOperationLike(sysAuditReq.getOperation());
        }
        if (StringUtils.isNotEmpty(sysAuditReq.getNewValue())) {
            criteria.andNewValueLike(sysAuditReq.getNewValue());
        }
        if (StringUtils.isNotEmpty(sysAuditReq.getOldValue())) {
            criteria.andOldValueLike(sysAuditReq.getOldValue());
        }
        sysAuditExample.setOrderByClause("id desc");
        PageHelper.startPage(pageReq.getPage(), pageReq.getPageSize());
        List<SysAudit> lsSysAudit = sysAuditMapper.selectByExample(sysAuditExample);
        return PageUtils.create(lsSysAudit);
    }


    public int delById(Long id) {
        return sysAuditMapper.deleteByPrimaryKey(id);
    }


    public int delByIds(List<Long> ids) {
        SysAuditExample sysAuditExample = new SysAuditExample();
        sysAuditExample.createCriteria().andIdIn(ids);
        return sysAuditMapper.deleteByExample(sysAuditExample);
    }


    public void addSysAudit(String oper, String oldVal, String newVal) {

        SysAudit sysAudit = new SysAudit();
        sysAudit.setOperation(oper);
        sysAudit.setOldValue(oldVal);
        sysAudit.setNewValue(newVal);
        sysAudit.setId(SnowflakeIdWorker.nextID());
        sysAudit.setCreateTime(new Date());
        sysAudit.setUserId(ShiroUtils.getUser().getUserId());
        sysAudit.setUsername(ShiroUtils.getUser().getUserName());
        sysAuditMapper.insert(sysAudit);
    }


}
