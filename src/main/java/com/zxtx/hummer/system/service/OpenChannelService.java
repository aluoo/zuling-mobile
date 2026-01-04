package com.zxtx.hummer.system.service;

import com.zxtx.hummer.system.dao.mapper.OpenChannelMapper;
import com.zxtx.hummer.system.domain.OpenChannel;
import com.zxtx.hummer.system.domain.OpenChannelExample;
import com.zxtx.hummer.system.vo.OpenChannelRep;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenChannelService {

    @Autowired
    private OpenChannelMapper openChannelMapper;


    public List<OpenChannelRep> getAllOpenChannels() {

        List<OpenChannelRep> lsOpenChannelRep = new ArrayList<>();
        OpenChannelRep defaultOpenChannelRep = new OpenChannelRep();
        defaultOpenChannelRep.setOperatorId("lanhai");
        defaultOpenChannelRep.setOperatorName("安逸出行");
        lsOpenChannelRep.add(defaultOpenChannelRep);
        List<OpenChannel> lsOpenChannel = openChannelMapper.selectByExample(new OpenChannelExample());
        if (lsOpenChannel != null) {
            lsOpenChannel.forEach((OpenChannel openChannel) -> {

                OpenChannelRep openChannelRep = new OpenChannelRep();
                BeanUtils.copyProperties(openChannel, openChannelRep);
                lsOpenChannelRep.add(openChannelRep);
            });
        }
        return lsOpenChannelRep;
    }

}
