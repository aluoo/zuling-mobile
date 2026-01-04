package com.zxtx.hummer.insurance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.insurance.dao.mapper.DiInsuranceOrderMapper;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrder;
import com.zxtx.hummer.insurance.req.DiInsuranceOrderReq;
import com.zxtx.hummer.insurance.vo.DiInsuranceOrderVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数保订单表 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
@Service
public class DiInsuranceOrderService extends ServiceImpl<DiInsuranceOrderMapper, DiInsuranceOrder> {

    public List<DiInsuranceOrderVO> selectPage(DiInsuranceOrderReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiInsuranceOrderVO> resultList = this.getBaseMapper().selectByParam(req);
        return resultList;
    }

}
