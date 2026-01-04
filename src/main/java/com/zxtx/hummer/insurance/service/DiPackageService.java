package com.zxtx.hummer.insurance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.insurance.dao.mapper.DiPackageMapper;
import com.zxtx.hummer.insurance.domain.DiPackage;
import com.zxtx.hummer.insurance.req.DiPackageDTO;
import com.zxtx.hummer.insurance.req.DiPackageReq;
import com.zxtx.hummer.insurance.vo.DiPackageVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 保险套餐表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiPackageService extends ServiceImpl<DiPackageMapper, DiPackage> {

    public List<DiPackageVO> selectPage(DiPackageReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiPackageVO> resultList = this.getBaseMapper().selectByParam(req);
        return resultList;
    }

    public List<DiPackage> selectByTypeId(Long typeId) {
        List<DiPackage> resultList = this.lambdaQuery().eq(DiPackage::getStatus,1)
                .eq(DiPackage::getTypeId,typeId).list();
        return resultList;
    }

    public void saveType(DiPackage diPackage){
        if(diPackage.getId()==null){
            diPackage.setStatus(1);
            this.save(diPackage);
            return;
        }
        this.updateById(diPackage);
    }

    public void editStatus(DiPackageDTO dto){
        DiPackage diPackage = this.getById(dto.getId());
        diPackage.setStatus(dto.getStatus());
        this.updateById(diPackage);
    }

}
