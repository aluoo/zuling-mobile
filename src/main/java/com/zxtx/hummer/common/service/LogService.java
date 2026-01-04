package com.zxtx.hummer.common.service;

import com.zxtx.hummer.common.dao.mapper.LogMapper;
import com.zxtx.hummer.common.domain.LogDO;
import com.zxtx.hummer.common.domain.PageDO;
import com.zxtx.hummer.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    @Autowired
    LogMapper logMapper;

    @Async
    public void save(LogDO logDO) {
        logMapper.save(logDO);
    }

    public PageDO<LogDO> queryList(Query query) {
        int total = logMapper.count(query);
        List<LogDO> logs = logMapper.list(query);
        PageDO<LogDO> page = new PageDO<>();
        page.setTotal(total);
        page.setRows(logs);
        return page;
    }

    public int remove(Long id) {

        int count = logMapper.remove(id);
        return count;
    }

    public int batchRemove(Long[] ids) {
        return logMapper.batchRemove(ids);
    }
}
