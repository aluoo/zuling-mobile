package com.zxtx.hummer.commission.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.commission.domain.CommissionSettle;
import com.zxtx.hummer.commission.dto.CommissionSettleCheckSumVO;
import com.zxtx.hummer.commission.dto.CommissionSettleDTO;
import com.zxtx.hummer.commission.dto.CommissionSettleDataDetailInfoDTO;
import com.zxtx.hummer.commission.req.SettleLogQueryReq;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统结算单 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
public interface CommissionSettleMapper extends BaseMapper<CommissionSettle> {

    List<CommissionSettle> countBalanceGroupByEmployee(@Param("bizType") int bizType,
                                                       @Param("gainType") int gainType,
                                                       @Param("beginTime") Date beginTime,
                                                       @Param("endTime") Date endTime,
                                                       @Param("employeeId") Long employeeId);

    /**
     * 获取某个类型组合佣金结算(个人)明细数据
     *
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @param employeeIds 员工ID列表
     * @return CommissionSettleDataDetailInfoDTO
     */
    List<CommissionSettleDataDetailInfoDTO> getBySelfData(@Param("bizType") int bizType,
                                                          @Param("gainType") int gainType,
                                                          @Param("beginTime") Date beginTime,
                                                          @Param("endTime") Date endTime,
                                                          @Param("employeeIds") List<Long> employeeIds
    );

    /**
     * 结算记录
     *
     * @param req
     * @return
     */
    List<CommissionSettleDTO> selectBySearch(@Param("req") SettleLogQueryReq req);

    CommissionSettleCheckSumVO sumByParam(@Param("req") SettleLogQueryReq req);
}