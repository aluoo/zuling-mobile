package com.zxtx.hummer.withdraw.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.account.constant.EmployAccountChangeEnum;
import com.zxtx.hummer.account.service.EmployeeAccountChangeService;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.utils.ExcelImportUtil;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.notice.domain.NoticeEmployeeMsg;
import com.zxtx.hummer.notice.enums.MsgBizTypeEnum;
import com.zxtx.hummer.notice.enums.MsgReadStatusEnum;
import com.zxtx.hummer.notice.service.INoticeEmployeeMsgService;
import com.zxtx.hummer.withdraw.constant.WithdrawApplyStatusEnum;
import com.zxtx.hummer.withdraw.constant.WithdrawCardIllegalTypeEnum;
import com.zxtx.hummer.withdraw.constant.WithdrawCardStatusEnum;
import com.zxtx.hummer.withdraw.dao.mapper.WithdrawEmployeeApplyMapper;
import com.zxtx.hummer.withdraw.domain.WithdrawCheckLog;
import com.zxtx.hummer.withdraw.domain.WithdrawEmployeeApply;
import com.zxtx.hummer.withdraw.domain.WithdrawEmployeeCard;
import com.zxtx.hummer.withdraw.dto.StateSnapshotDTO;
import com.zxtx.hummer.withdraw.req.WithdrawApplyReq;
import com.zxtx.hummer.withdraw.req.WithdrawCheckReq;
import com.zxtx.hummer.withdraw.service.IWithdrawCheckLogService;
import com.zxtx.hummer.withdraw.service.IWithdrawEmployeeApplyService;
import com.zxtx.hummer.withdraw.service.IWithdrawEmployeeCardService;
import com.zxtx.hummer.withdraw.vo.ImportWithdrawVO;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 提现申请表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 */
@Service
@Slf4j
public class WithdrawEmployeeApplyServiceImpl extends ServiceImpl<WithdrawEmployeeApplyMapper, WithdrawEmployeeApply> implements IWithdrawEmployeeApplyService {

    @Autowired
    IWithdrawCheckLogService checkLogService;
    @Autowired
    IWithdrawEmployeeCardService cardService;
    @Autowired
    INoticeEmployeeMsgService messageService;
    @Autowired
    EmployeeAccountChangeService employeeAccountChangeService;
    @Autowired
    EmService emService;

    @Override
    public List<WithdrawApplyVo> selectPage(WithdrawApplyReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<WithdrawApplyVo> resultList = this.getBaseMapper().selectByParam(req);
        if (CollUtil.isNotEmpty(resultList)) {
            for (WithdrawApplyVo applyVo : resultList) {
                //applyVo.setSecondDept(emService.getSecondDept(applyVo.getEmployeeId()));
                applyVo.setAmountStr(StringUtils.fenToYuan(ObjectUtil.isNotNull(applyVo.getAmount()) ? applyVo.getAmount() : 0));
                applyVo.setTaxAmountStr(StringUtils.fenToYuan(ObjectUtil.isNotNull(applyVo.getTaxAmount()) ? applyVo.getTaxAmount() : 0));
                applyVo.setInAmountStr(StringUtils.fenToYuan(ObjectUtil.isNotNull(applyVo.getInAmount()) ? applyVo.getInAmount() : 0));
            }
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkUnPass(WithdrawCheckReq req) {
        //设置申请状态为失败
        WithdrawEmployeeApply apply = this.baseMapper.selectById(req.getId());
        dealApply(apply, req.getVioResult(), WithdrawApplyStatusEnum.fail);
        //更新员工账户信息
        boolean flag = employeeAccountChangeService.changeAccount(apply.getEmployeeId(), EmployAccountChangeEnum.withdrawUnPass, apply.getAmount(), apply.getId(), null);
        if (!flag) {
            throw new BusinessException("员工账户资金变动失败");
        }
        //插入通知_员工消息
        NoticeEmployeeMsg msg = new NoticeEmployeeMsg();
        msg.setEmployeeId(apply.getEmployeeId());
        msg.setBizType(MsgBizTypeEnum.WITHDRAW.getCode());
        msg.setTitle("提现审核失败");
        msg.setPushTime(new Date());
        msg.setHasRead(MsgReadStatusEnum.UNREAD.getCode());
        msg.setContent("提现审核失败,原因是" + req.getVioResult());
        msg.setBizId(apply.getId());
        msg.setCreateTime(new Date());
        messageService.save(msg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkPass(WithdrawCheckReq req) {
        //设置申请状态为审核通过
        WithdrawEmployeeApply apply = this.baseMapper.selectById(req.getId());
        dealApply(apply, req.getVioResult(), WithdrawApplyStatusEnum.wait_pay);
        //插入通知_员工消息
        NoticeEmployeeMsg msg = new NoticeEmployeeMsg();
        msg.setEmployeeId(apply.getEmployeeId());
        msg.setBizType(MsgBizTypeEnum.WITHDRAW.getCode());
        msg.setTitle("提现审核成功");
        msg.setPushTime(new Date());
        msg.setHasRead(MsgReadStatusEnum.UNREAD.getCode());
        msg.setContent("提现审核成功");
        msg.setBizId(apply.getId());
        msg.setCreateTime(new Date());
        messageService.save(msg);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payPass(WithdrawCheckReq req) {
        WithdrawEmployeeApply apply = this.baseMapper.selectById(req.getId());
        if (!apply.getStatus().equals(WithdrawApplyStatusEnum.wait_pay.getType())) {
            throw new BaseException(99999, "订单状态不是待打款");
        }
        JSONArray jsonArray = JSONUtil.parseArray(apply.getStateSnapshot());
        //快照设置
        List<StateSnapshotDTO> stateSnapshotDTOs = jsonArray.stream().map(item -> JSONUtil.toBean((JSONObject) item, StateSnapshotDTO.class)).collect(Collectors.toList());
        StateSnapshotDTO snapshotDTO = new StateSnapshotDTO();
        snapshotDTO.setReachTime(LocalDateTime.now());
        snapshotDTO.setStatus(WithdrawApplyStatusEnum.pay_success.getType());
        stateSnapshotDTOs.add(snapshotDTO);
        apply.setStateSnapshot(JSONUtil.toJsonStr(stateSnapshotDTOs));
        apply.setStatus(WithdrawApplyStatusEnum.pay_success.getType());
        apply.setUpdateTime(new Date());
        this.baseMapper.updateById(apply);

        //更新员工账户提现方式表
        LambdaUpdateWrapper<WithdrawEmployeeCard> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.set(WithdrawEmployeeCard::getStatus, WithdrawCardStatusEnum.normal.getType());
        updateWrapper.eq(WithdrawEmployeeCard::getEmployeeId, apply.getEmployeeId());
        cardService.update(updateWrapper);

        //更新员工账户信息
        boolean flag = employeeAccountChangeService.changeAccount(apply.getEmployeeId(), EmployAccountChangeEnum.withdrawPass, apply.getAmount(), apply.getId(), null);
        if (!flag) {
            throw new BusinessException("员工账户资金变动失败");
        }

        //插入通知_员工消息
        NoticeEmployeeMsg msg = new NoticeEmployeeMsg();
        msg.setEmployeeId(apply.getEmployeeId());
        msg.setBizType(MsgBizTypeEnum.WITHDRAW.getCode());
        msg.setTitle("提现打款成功");
        msg.setPushTime(new Date());
        msg.setHasRead(MsgReadStatusEnum.UNREAD.getCode());
        msg.setContent("提现打款成功");
        msg.setBizId(apply.getId());
        msg.setCreateTime(new Date());
        messageService.save(msg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payUnPass(WithdrawCheckReq req) {
        String errorMsg = "";
        if (CollUtil.isNotEmpty(req.getTypeCodes())) {
            for (String msg : req.getTypeCodes()) {
                errorMsg = errorMsg + WithdrawCardIllegalTypeEnum.getDescByCode(msg);
            }
        }
        WithdrawEmployeeApply apply = this.baseMapper.selectById(req.getId());
        if (!apply.getStatus().equals(WithdrawApplyStatusEnum.wait_pay.getType())) {
            throw new BaseException(99999, "订单状态不是待打款");
        }
        JSONArray jsonArray = JSONUtil.parseArray(apply.getStateSnapshot());
        //快照设置
        List<StateSnapshotDTO> stateSnapshotDTOs = jsonArray.stream().map(item -> JSONUtil.toBean((JSONObject) item, StateSnapshotDTO.class)).collect(Collectors.toList());
        StateSnapshotDTO snapshotDTO = new StateSnapshotDTO();
        snapshotDTO.setReachTime(LocalDateTime.now());
        snapshotDTO.setStatus(WithdrawApplyStatusEnum.fail.getType());
        snapshotDTO.setRemark(errorMsg);
        stateSnapshotDTOs.add(snapshotDTO);
        apply.setStateSnapshot(JSONUtil.toJsonStr(stateSnapshotDTOs));
        apply.setStatus(WithdrawApplyStatusEnum.fail.getType());
        apply.setUpdateTime(new Date());
        this.baseMapper.updateById(apply);

        //有选择异常原因才更新绑定银行卡状态
        if (CollUtil.isNotEmpty(req.getTypeCodes())) {
            //更新员工账户提现方式表
            LambdaUpdateWrapper<WithdrawEmployeeCard> updateWrapper = new LambdaUpdateWrapper();
            updateWrapper.set(WithdrawEmployeeCard::getStatus, WithdrawCardStatusEnum.abnormal.getType());
            updateWrapper.set(WithdrawEmployeeCard::getIllegalTypes, String.join(",", req.getTypeCodes()));
            updateWrapper.eq(WithdrawEmployeeCard::getEmployeeId, apply.getEmployeeId());
            cardService.update(updateWrapper);
        }

        //更新员工账户信息
        boolean flag = employeeAccountChangeService.changeAccount(apply.getEmployeeId(), EmployAccountChangeEnum.withdrawUnPass, apply.getAmount(), apply.getId(), null);
        if (!flag) {
            throw new BusinessException("员工账户资金变动失败");
        }

        //插入通知_员工消息
        NoticeEmployeeMsg msg = new NoticeEmployeeMsg();
        msg.setEmployeeId(apply.getEmployeeId());
        msg.setBizType(MsgBizTypeEnum.WITHDRAW.getCode());
        msg.setTitle("提现打款失败");
        msg.setPushTime(new Date());
        msg.setHasRead(MsgReadStatusEnum.UNREAD.getCode());
        msg.setContent("提现打款失败，原因是" + errorMsg);
        msg.setBizId(apply.getId());
        msg.setCreateTime(new Date());
        messageService.save(msg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importExcel(MultipartFile file) {
        String resultMessage = "";
        List<ImportWithdrawVO> importList = null;
        try {
            importList = ExcelImportUtil.importFromExcel(file.getOriginalFilename(), file.getInputStream(),
                    20, ImportWithdrawVO.class);
        } catch (Exception e) {
            throw new BusinessException(BizError.WITHDRAW_IMPORT_FAILED);
        }
        if (CollectionUtils.isEmpty(importList)) {
            throw new BusinessException(BizError.WITHDRAW_IMPORT_FAILED);
        }

        //订单号对应的申请记录
        List<String> applyNos = importList.stream().map(ImportWithdrawVO::getApplyNo).collect(Collectors.toList());
        LambdaQueryWrapper<WithdrawEmployeeApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WithdrawEmployeeApply::getStatus, WithdrawApplyStatusEnum.wait_pay.getType());
        queryWrapper.in(WithdrawEmployeeApply::getApplyNo, applyNos);
        Map<String, WithdrawEmployeeApply> applyMap = this.baseMapper.selectList(queryWrapper).stream().collect(
                Collectors.toMap(WithdrawEmployeeApply::getApplyNo, Function.identity(), (a, b) -> b));
        //更新的申请记录集合
        List<WithdrawEmployeeApply> updateList = new ArrayList<>();
        List<WithdrawEmployeeApply> successList = new ArrayList<>();
        List<WithdrawEmployeeApply> failList = new ArrayList<>();

        for (ImportWithdrawVO vo : importList) {
            WithdrawEmployeeApply apply = applyMap.get(vo.getApplyNo());
            if (ObjectUtil.isNull(apply)) {
                throw new BusinessException("存在订单号" + vo.getApplyNo() + "在系统中不存在或状态不是待打款");
            }
            JSONArray jsonArray = JSONUtil.parseArray(apply.getStateSnapshot());
            //快照设置
            List<StateSnapshotDTO> stateSnapshotDTOs = jsonArray.stream().map(item -> JSONUtil.toBean((JSONObject) item, StateSnapshotDTO.class)).collect(Collectors.toList());
            StateSnapshotDTO snapshotDTO = new StateSnapshotDTO();
            snapshotDTO.setReachTime(LocalDateTime.now());
            if(vo.getPayStatus().equals("已完成")){
                snapshotDTO.setStatus(WithdrawApplyStatusEnum.pay_success.getType());
                apply.setStatus(WithdrawApplyStatusEnum.pay_success.getType());
                successList.add(apply);
            }else{
                snapshotDTO.setStatus(WithdrawApplyStatusEnum.fail.getType());
                apply.setStatus(WithdrawApplyStatusEnum.fail.getType());
                failList.add(apply);
            }
            stateSnapshotDTOs.add(snapshotDTO);
            apply.setStateSnapshot(JSONUtil.toJsonStr(stateSnapshotDTOs));
            apply.setUpdateTime(new Date());
            updateList.add(apply);
        }
        //更新申请记录
        if (CollectionUtils.isEmpty(updateList)) return resultMessage;

        //更新失败的
        if(CollUtil.isNotEmpty(failList)){
            for (WithdrawEmployeeApply apply : failList) {
                //更新员工账户信息
                boolean flag = employeeAccountChangeService.changeAccount(apply.getEmployeeId(), EmployAccountChangeEnum.withdrawUnPass, apply.getAmount(), apply.getId(), null);
                if (!flag) {
                    throw new BusinessException("订单号" + apply.getApplyNo() + "账户处理失败，请联系管理员处理后再次导入");
                }
            }
        }

        for (WithdrawEmployeeApply apply : successList) {
            //更新员工账户信息
            boolean flag = employeeAccountChangeService.changeAccount(apply.getEmployeeId(), EmployAccountChangeEnum.withdrawPass, apply.getAmount(), apply.getId(), null);
            if (!flag) {
                throw new BusinessException("订单号" + apply.getApplyNo() + "账户处理失败，请联系管理员处理后再次导入");
            }
        }

        this.updateBatchById(updateList);
        return resultMessage;
    }

    private void dealApply(WithdrawEmployeeApply apply, String vioResult, WithdrawApplyStatusEnum status) {
        JSONArray jsonArray = JSONUtil.parseArray(apply.getStateSnapshot());
        //快照设置
        List<StateSnapshotDTO> stateSnapshotDTOs = jsonArray.stream().map(item -> JSONUtil.toBean((JSONObject) item, StateSnapshotDTO.class)).collect(Collectors.toList());
        StateSnapshotDTO snapshotDTO = new StateSnapshotDTO();
        snapshotDTO.setReachTime(LocalDateTime.now());
        snapshotDTO.setStatus(status.getType());
        snapshotDTO.setRemark(vioResult);
        stateSnapshotDTOs.add(snapshotDTO);
        apply.setStateSnapshot(JSONUtil.toJsonStr(stateSnapshotDTOs));
        apply.setStatus(status.getType());
        apply.setUpdateTime(new Date());
        if (status.equals(WithdrawApplyStatusEnum.fail) && StrUtil.isNotBlank(vioResult)) {
            apply.setRemark(vioResult);
        }
        this.baseMapper.updateById(apply);

        //添加审核记录
        WithdrawCheckLog checkLog = new WithdrawCheckLog();
        checkLog.setApplyId(apply.getId());
        checkLog.setSysUserId(ShiroUtils.getUser().getUserId());
        checkLog.setEmployeeId(ShiroUtils.getUser().getEmployeeId());
        checkLog.setToEmployeeId(apply.getToEmployeeId());
        checkLog.setOldStatus(apply.getStatus());
        checkLog.setNewStatus(status.getType());
        checkLog.setRemark(vioResult);
        checkLog.setCreateTime(new Date());
        checkLogService.save(checkLog);
    }

}