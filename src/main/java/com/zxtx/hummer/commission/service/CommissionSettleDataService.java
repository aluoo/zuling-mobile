package com.zxtx.hummer.commission.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.commission.dao.mapper.CommissionSettleMapper;
import com.zxtx.hummer.commission.domain.CommissionSettle;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyBase;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyDetail;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyTotal;
import com.zxtx.hummer.commission.dto.CommissionSettleDataDetailInfoDTO;
import com.zxtx.hummer.commission.enums.CommissionBizType;
import com.zxtx.hummer.commission.enums.CommissionSettleGainType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Slf4j
@Service
public class CommissionSettleDataService {
    @Autowired
    private CommissionSettleDataDailyBaseService dailyBaseService;
    @Autowired
    private CommissionSettleDataDailyTotalService dailyTotalService;
    @Autowired
    private CommissionSettleDataDailyDetailService dailyDetailService;
    @Autowired
    private CommissionSettleMapper commissionSettleMapper;

    @Transactional(rollbackFor = Exception.class)
    public void pullByDayAndUser(Long employeeId, String dateStr, boolean removeExist) {
        log.info("CommissionSettleDataService.pullByUserAndDate.start");
        if (employeeId == null || StrUtil.isBlank(dateStr)) {
            log.info("CommissionSettleDataService.pullByDayAndUser.end: employeeId or date is null");
            return;
        }
        DateTime now = DateUtil.date();
        DateTime targetDay = DateUtil.parse(dateStr);
        DateTime beginOfTargetDay = DateUtil.beginOfDay(targetDay);
        DateTime endOfTargetDay = DateUtil.endOfDay(targetDay);
        log.info("CommissionSettleDataService.pullByDayAndUser.info: now-{}, targetEmployeeId-{}, targetDay-{}, pull range of settle time between-[{}-{}]",
                now, employeeId, targetDay, beginOfTargetDay, endOfTargetDay);
        if (exist(beginOfTargetDay.toJdkDate(), employeeId)) {
            log.info("CommissionSettleDataService.pullByDayAndUser.info: {} data exist", beginOfTargetDay);
            if (!removeExist) {
                return;
            }
            removeExist(beginOfTargetDay.toJdkDate(), employeeId);
        }
        List<CommissionSettleDataDailyBase> bases = buildBase(beginOfTargetDay.toJdkDate(),
                endOfTargetDay.toJdkDate(), employeeId);
        if (CollUtil.isEmpty(bases)) {
            log.info("CommissionSettleDataService.pullByDayAndUser.end: empty bases data");
            return;
        }
        dailyBaseService.saveBatch(bases);

        List<CommissionSettleDataDailyTotal> totals = buildAndSaveTotal(bases, beginOfTargetDay.toJdkDate());

        List<CommissionSettleDataDailyDetail> details = buildAndSaveDetails(bases, beginOfTargetDay.toJdkDate(), endOfTargetDay.toJdkDate());

        log.info("CommissionSettleDataService.pullByDayAndUser.end: saved base-{}, total-{}, detail-{}", bases.size(), totals.size(), details.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void pullAllByDay(String dateStr, boolean removeExist) {
        log.info("CommissionSettleDataService.pullAllByDay.start");
        // 每日拉取前一日数据进行统计
        DateTime now = DateUtil.date();
        DateTime targetDay;
        if (StrUtil.isNotBlank(dateStr)) {
            targetDay = DateUtil.parse(dateStr);
        } else {
            targetDay = DateUtil.yesterday();
        }
        DateTime beginOfTargetDay = DateUtil.beginOfDay(targetDay);
        DateTime endOfTargetDay = DateUtil.endOfDay(targetDay);
        log.info("CommissionSettleDataService.pullAllByDay.info: now-{}, targetDay-{}, pull range of settle time between-[{}-{}]",
                now, targetDay, beginOfTargetDay, endOfTargetDay);
        if (exist(beginOfTargetDay.toJdkDate())) {
            log.info("CommissionSettleDataService.pullAllByDay.info: {} data exist", beginOfTargetDay);
            if (!removeExist) {
                return;
            }
            log.info("CommissionSettleDataService.pullAllByDay.info: remove exist data");
            removeExist(beginOfTargetDay.toJdkDate());
        }

        List<CommissionSettleDataDailyBase> bases = buildBase(beginOfTargetDay.toJdkDate(),
                endOfTargetDay.toJdkDate(),
                null);

        if (CollUtil.isEmpty(bases)) {
            log.info("CommissionSettleDataService.pullAllByDay.end: empty bases data");
            return;
        }
        dailyBaseService.saveBatch(bases);

        List<CommissionSettleDataDailyTotal> totals = buildAndSaveTotal(bases, beginOfTargetDay.toJdkDate());

        List<CommissionSettleDataDailyDetail> details = buildAndSaveDetails(bases, beginOfTargetDay.toJdkDate(), endOfTargetDay.toJdkDate());

        log.info("CommissionSettleDataService.pullAllByDay.end: saved base-{}, total-{}, detail-{}", bases.size(), totals.size(), details.size());
    }

    private List<CommissionSettleDataDailyBase> countBalance(int bizType, int gainType, Date beginTime, Date endTime, Long employeeId) {
        List<CommissionSettleDataDailyBase> resp = new ArrayList<>();
        List<CommissionSettle> settles = commissionSettleMapper.countBalanceGroupByEmployee(bizType,
                gainType,
                beginTime,
                endTime,
                employeeId);
        if (CollUtil.isEmpty(settles)) {
            return resp;
        }
        settles.forEach(settle -> {
            if (settle.getSettleBalance() <= 0) {
                return;
            }
            CommissionSettleDataDailyBase base = CommissionSettleDataDailyBase.builder()
                    .day(beginTime)
                    .startTime(beginTime)
                    .endTime(endTime)
                    .createTime(DateUtil.date().toJdkDate())
                    .employeeId(settle.getEmployeeId())
                    .bizType(bizType)
                    .gainType(gainType)
                    .value(settle.getSettleBalance())
                    .build();
            resp.add(base);
        });
        return resp;
    }

    private List<CommissionSettleDataDailyBase> buildBase(Date begin, Date end, Long employeeId) {
        List<CommissionSettleDataDailyBase> resp = new ArrayList<>();
        // 服务费个人
        resp.addAll(countBalance(CommissionBizType.PLAT_SERVICE.getType(),
                CommissionSettleGainType.BY_MYSELF.getType(),
                begin,
                end,
                employeeId));
        // 软件拉新个人
        resp.addAll(countBalance(CommissionBizType.APP_NEW.getType(),
                CommissionSettleGainType.BY_MYSELF.getType(),
                begin,
                end,
                employeeId));
        // 手机回收个人
        resp.addAll(countBalance(CommissionBizType.PHONE_DOWN.getType(),
                CommissionSettleGainType.BY_MYSELF.getType(),
                begin,
                end,
                employeeId));
        // 享转数保个人
        resp.addAll(countBalance(CommissionBizType.INSURANCE_SERVICE.getType(),
                CommissionSettleGainType.BY_MYSELF.getType(),
                begin,
                end,
                employeeId));
        // 号卡个人
        resp.addAll(countBalance(CommissionBizType.HK_SERVICE.getType(),
                CommissionSettleGainType.BY_MYSELF.getType(),
                begin,
                end,
                employeeId));
        // 服务费团队贡献
        resp.addAll(countBalance(CommissionBizType.PLAT_SERVICE.getType(),
                CommissionSettleGainType.CHILD_CONTRIBUTE.getType(),
                begin,
                end,
                employeeId));
        // 软件拉新团队贡献
        resp.addAll(countBalance(CommissionBizType.APP_NEW.getType(),
                CommissionSettleGainType.CHILD_CONTRIBUTE.getType(),
                begin,
                end,
                employeeId));
        // 软件拉新团队贡献
        resp.addAll(countBalance(CommissionBizType.PHONE_DOWN.getType(),
                CommissionSettleGainType.CHILD_CONTRIBUTE.getType(),
                begin,
                end,
                employeeId));
        // 享转数保团队贡献
        resp.addAll(countBalance(CommissionBizType.INSURANCE_SERVICE.getType(),
                CommissionSettleGainType.CHILD_CONTRIBUTE.getType(),
                begin,
                end,
                employeeId));
        // 号卡团队贡献
        resp.addAll(countBalance(CommissionBizType.HK_SERVICE.getType(),
                CommissionSettleGainType.CHILD_CONTRIBUTE.getType(),
                begin,
                end,
                employeeId));
        return resp;
    }

    private List<CommissionSettleDataDailyTotal> buildAndSaveTotal(List<CommissionSettleDataDailyBase> bases, Date day) {
        List<CommissionSettleDataDailyTotal> totals = new ArrayList<>();
        if (CollUtil.isEmpty(bases)) {
            return totals;
        }
        Map<Long, List<CommissionSettleDataDailyBase>> collect = bases.stream().collect(Collectors.groupingBy(CommissionSettleDataDailyBase::getEmployeeId, Collectors.toList()));
        collect.forEach((k, list) -> {
            int sum = list.stream().mapToInt(CommissionSettleDataDailyBase::getValue).sum();
            if (sum <= 0) {
                return;
            }
            CommissionSettleDataDailyTotal total = CommissionSettleDataDailyTotal.builder()
                    .day(day)
                    .employeeId(k)
                    .createTime(DateUtil.date().toJdkDate())
                    .value(sum)
                    .build();
            totals.add(total);
        });

        if (CollUtil.isNotEmpty(totals)) {
            dailyTotalService.saveBatch(totals);
        }

        return totals;
    }

    private List<CommissionSettleDataDailyDetail> buildAndSaveDetails(List<CommissionSettleDataDailyBase> bases, Date beginTime, Date endTime) {
        List<CommissionSettleDataDailyDetail> details = new ArrayList<>();
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.PLAT_SERVICE, CommissionSettleGainType.BY_MYSELF));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.PLAT_SERVICE, CommissionSettleGainType.CHILD_CONTRIBUTE));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.APP_NEW, CommissionSettleGainType.BY_MYSELF));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.APP_NEW, CommissionSettleGainType.CHILD_CONTRIBUTE));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.PHONE_DOWN, CommissionSettleGainType.BY_MYSELF));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.PHONE_DOWN, CommissionSettleGainType.CHILD_CONTRIBUTE));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.INSURANCE_SERVICE, CommissionSettleGainType.BY_MYSELF));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.INSURANCE_SERVICE, CommissionSettleGainType.CHILD_CONTRIBUTE));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.HK_SERVICE, CommissionSettleGainType.BY_MYSELF));
        details.addAll(buildPlatSelfDetailData(bases, beginTime, endTime, CommissionBizType.HK_SERVICE, CommissionSettleGainType.CHILD_CONTRIBUTE));
        if (CollUtil.isNotEmpty(details)) {
            dailyDetailService.saveBatch(details);
        }
        return details;
    }

    private List<CommissionSettleDataDailyDetail> buildPlatSelfDetailData(List<CommissionSettleDataDailyBase> bases, Date beginTime, Date endTime, CommissionBizType bizType, CommissionSettleGainType gainType) {
        List<CommissionSettleDataDailyDetail> resp = new ArrayList<>();
        if (CollUtil.isEmpty(bases)) {
            return resp;
        }
        // 筛出服务费个人的base数据
        List<CommissionSettleDataDailyBase> issueSelfBases = bases.stream()
                .filter(o -> o.getBizType().equals(bizType.getType()) && o.getGainType().equals(gainType.getType()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(issueSelfBases)) {
            return resp;
        }
        List<Long> employeeIds = issueSelfBases.stream().map(CommissionSettleDataDailyBase::getEmployeeId).collect(Collectors.toList());
        if (CollUtil.isEmpty(employeeIds)) {
            return resp;
        }
        List<CommissionSettleDataDetailInfoDTO> details = commissionSettleMapper.getBySelfData(bizType.getType(), gainType.getType(), beginTime, endTime, employeeIds);
        if (CollUtil.isEmpty(details)) {
            return resp;
        }
        Map<Long, CommissionSettleDataDailyBase> collect = issueSelfBases.stream().collect(Collectors.toMap(CommissionSettleDataDailyBase::getEmployeeId, Function.identity()));

        details.forEach(info -> {
            if (info.getSettleBalance() <= 0) {
                return;
            }
            Optional<CommissionSettleDataDailyBase> bean = Optional.ofNullable(collect.get(info.getEmployeeId()));
            CommissionSettleDataDailyDetail detail = CommissionSettleDataDailyDetail.builder()
                    .day(beginTime)
                    .createTime(DateUtil.date().toJdkDate())
                    .employeeId(info.getEmployeeId())
                    .bizType(bizType.getType())
                    .gainType(gainType.getType())
                    .value(info.getSettleBalance())
                    .remark(info.getRemark())
                    .dailyBaseId(bean.map(CommissionSettleDataDailyBase::getId).orElse(null))
                    .build();
            resp.add(detail);
        });

        return resp;
    }

    private boolean exist(Date day) {
        return dailyTotalService.lambdaQuery()
                .eq(CommissionSettleDataDailyTotal::getDay, day)
                .exists();
    }

    private boolean exist(Date day, Long employeeId) {
        return dailyTotalService.lambdaQuery()
                .eq(CommissionSettleDataDailyTotal::getDay, day)
                .eq(CommissionSettleDataDailyTotal::getEmployeeId, employeeId)
                .exists();
    }

    private void removeExist(Date day, Long employeeId) {
        dailyTotalService.remove(new LambdaQueryWrapper<CommissionSettleDataDailyTotal>()
                .eq(CommissionSettleDataDailyTotal::getDay, day)
                .eq(CommissionSettleDataDailyTotal::getEmployeeId, employeeId));
        dailyBaseService.remove(new LambdaQueryWrapper<CommissionSettleDataDailyBase>()
                .eq(CommissionSettleDataDailyBase::getDay, day)
                .eq(CommissionSettleDataDailyBase::getEmployeeId, employeeId));
        dailyDetailService.remove(new LambdaQueryWrapper<CommissionSettleDataDailyDetail>()
                .eq(CommissionSettleDataDailyDetail::getDay, day)
                .eq(CommissionSettleDataDailyDetail::getEmployeeId, employeeId));
    }

    private void removeExist(Date day) {
        dailyTotalService.remove(new LambdaQueryWrapper<CommissionSettleDataDailyTotal>()
                .eq(CommissionSettleDataDailyTotal::getDay, day));
        dailyBaseService.remove(new LambdaQueryWrapper<CommissionSettleDataDailyBase>()
                .eq(CommissionSettleDataDailyBase::getDay, day));
        dailyDetailService.remove(new LambdaQueryWrapper<CommissionSettleDataDailyDetail>()
                .eq(CommissionSettleDataDailyDetail::getDay, day));
    }
}