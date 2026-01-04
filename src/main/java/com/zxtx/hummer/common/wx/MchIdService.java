package com.zxtx.hummer.common.wx;


import com.zxtx.hummer.common.wx.config.WxPayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MchIdService {

    @Autowired
    private WxPayProperties properties;

    public String getAnyiSubMchId() {
        return properties.getConfigs().get(0).getSubMchId();
    }

    public String getJxSubMchId() {return properties.getConfigs().get(1).getSubMchId();}

    public String getJxzSubMchId() {return properties.getConfigs().get(2).getSubMchId();}
}