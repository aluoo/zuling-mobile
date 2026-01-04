package com.zxtx.hummer.withdraw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.withdraw.domain.WithdrawEmployeeApply;
import com.zxtx.hummer.withdraw.req.WithdrawApplyReq;
import com.zxtx.hummer.withdraw.req.WithdrawCheckReq;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 提现申请表 服务类
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 */
public interface IWithdrawEmployeeApplyService extends IService<WithdrawEmployeeApply> {

    /**
     * @author chenjian
     * @create 2023/6/5 14:52
     * @desc 后台提现申请记录分页列表
     **/
    List<WithdrawApplyVo> selectPage(WithdrawApplyReq req);

    /**
     * @author chenjian
     * @create 2023/6/5 15:34
     * @desc 审核不通过
     **/
    void checkUnPass(WithdrawCheckReq req);

    /**
     * @author chenjian
     * @create 2023/6/5 15:34
     * @desc 审核通过
     **/
    void checkPass(WithdrawCheckReq req);

    /**
     * @author chenjian
     * @create 2023/6/5 15:34
     * @desc 打款成功
     **/
    void payPass(WithdrawCheckReq req);

    /**
     * @author chenjian
     * @create 2023/6/5 15:34
     * @desc 打款失败
     **/
    void payUnPass(WithdrawCheckReq req);

    /**
     * 导入
     *
     * @param file
     */
    String importExcel(MultipartFile file);


}
