package com.zxtx.hummer.company.dao.mapper;

import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.vo.ComListRes;
import com.zxtx.hummer.company.vo.CompanyListReq;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExtCompanyMapper {

    List<ComListRes> listByCondition(CompanyListReq req);

    int updatePayAmountById(@Param("id") Long id, @Param("payAmount") Integer payAmount,
                            @Param("updateTime") LocalDateTime updateTime, @Param("updator") String updator);

    void changeChannelStatus(@Param("code") String code, @Param("company") Company company, @Param("oldStatus") int oldStatus);

    Integer selectMaxCode(Long pId);
}