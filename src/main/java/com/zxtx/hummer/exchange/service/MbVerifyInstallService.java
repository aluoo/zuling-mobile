package com.zxtx.hummer.exchange.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.exchange.dao.mapper.MbVerifyInstallMapper;
import com.zxtx.hummer.exchange.domain.MbInstall;
import com.zxtx.hummer.exchange.domain.MbVerifyInstall;
import com.zxtx.hummer.exchange.domain.dto.InstallEditStatusDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 拉新安装包 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
public class MbVerifyInstallService extends ServiceImpl<MbVerifyInstallMapper, MbVerifyInstall>{

    public void editMbInstall(MbVerifyInstall dto){
        if(dto.getId()!=null){
            this.updateById(dto);
            return;
        }
        dto.setStatus(1);
        this.save(dto);
    }

    public void editStatus(InstallEditStatusDTO dto){
        MbVerifyInstall mbInstall = this.getById(dto.getId());
        mbInstall.setStatus(dto.getStatus());
        this.updateById(mbInstall);
    }

    public List<MbVerifyInstall> ableInstallList(){
        return this.list(Wrappers.lambdaQuery(MbVerifyInstall.class).eq(MbVerifyInstall::getStatus,1));
    }



}
