package com.zxtx.hummer.insurance.service;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.exchange.domain.MbInstall;
import com.zxtx.hummer.exchange.domain.dto.InstallEditStatusDTO;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import com.zxtx.hummer.insurance.domain.DiType;
import com.zxtx.hummer.insurance.dao.mapper.DiTypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.insurance.req.DiTypeDTO;
import com.zxtx.hummer.insurance.req.DiTypeReq;
import com.zxtx.hummer.insurance.vo.DiTypeVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 险种类型表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiTypeService extends ServiceImpl<DiTypeMapper, DiType> {

    public List<DiTypeVO> selectPage(DiTypeReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiTypeVO> resultList = this.getBaseMapper().selectByParam(req);
        return resultList;
    }

    public void saveType(DiType diType){
        if(diType.getId()==null){
            diType.setStatus(1);
            this.save(diType);
            return;
        }
        this.updateById(diType);
    }

    public void editStatus(DiTypeDTO dto){
        DiType diType = this.getById(dto.getId());
        diType.setStatus(dto.getStatus());
        this.updateById(diType);
    }

}
