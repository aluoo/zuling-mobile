package com.zxtx.hummer.product.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.product.domain.Order;
import com.zxtx.hummer.product.domain.OrderQuotePriceLog;
import com.zxtx.hummer.product.domain.ShippingOrder;
import com.zxtx.hummer.product.domain.enums.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/18
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderTaskService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderQuotePriceLogService orderQuotePriceLogService;
    @Autowired
    private ProductDictService productDictService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private ShippingOrderRelService shippingOrderRelService;

    @Transactional(rollbackFor = Exception.class)
    public void autoCloseOverdueOrder() {
        log.info("OrderTaskService.autoCloseOverdueOrder.start");
        int productOrderExpiredMinutes = productDictService.getProductOrderExpiredMinutes();
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.offset(now, DateField.MINUTE, -productOrderExpiredMinutes);
        log.info("OrderTaskService.autoCloseOverdueOrder.info: now-{}, over expired min-{}, overdue time-{}", now, productOrderExpiredMinutes, offset);

        // 找出已过期的订单列表
        List<Order> list = orderService.lambdaQuery()
                .select(Order::getId)
                .eq(Order::getStatus, OrderStatusEnum.UNCHECKED.getCode())
                .le(AbstractBaseEntity::getCreateTime, offset)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(list)) {
            log.info("OrderTaskService.autoCloseOverdueOrder.end: no available order");
            return;
        }
        // 批量更新 关闭订单
        List<Long> orderIds = list.stream().map(Order::getId).collect(Collectors.toList());
        orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.CANCELED.getCode())
                .eq(Order::getStatus, OrderStatusEnum.UNCHECKED.getCode())
                .in(Order::getId, orderIds)
                .update(new Order());
        // 批量日志
        orderLogService.addLogBatch(null,
                orderIds,
                OrderStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.AUTO_CLOSE.getCode(),
                OrderOperationEnum.AUTO_CLOSE.getDesc(),
                OrderOperationEnum.AUTO_CLOSE.getRemark());
        log.info("OrderTaskService.autoCloseOverdueOrder.end: closed {} orders", orderIds.size());

        // 找出关联的报价记录
        List<OrderQuotePriceLog> quoteLogs = orderQuotePriceLogService.lambdaQuery()
                .in(OrderQuotePriceLog::getOrderId, orderIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(quoteLogs)) {
            log.info("OrderTaskService.autoCloseOverdueOrder.end: no available quote log");
            return;
        }
        // 未报价的更新为报价超时
        // 插入已作废-超时未报价日志
        List<Long> notQuotedLogIds = quoteLogs.stream().filter(o -> o.getQuoted().equals(false)).map(OrderQuotePriceLog::getId).collect(Collectors.toList());
        updateQuoteCanceled(notQuotedLogIds, OrderQuoteLogSubStatusEnum.QUOTE_OVERDUE, OrderOperationEnum.QUOTE_CANCELED_OVERDUE);

        // 已报价的更新为门店确认交易超时
        List<Long> quotedLogIds = quoteLogs.stream().filter(o -> o.getQuoted().equals(true)).map(OrderQuotePriceLog::getId).collect(Collectors.toList());
        updateQuoteCanceled(notQuotedLogIds, OrderQuoteLogSubStatusEnum.CONFIRM_OVERDUE, OrderOperationEnum.QUOTE_CANCELED_CONFIRM_OVERDUE);


        log.info("OrderTaskService.autoCloseOverdueOrder.end: updated quoteOverdue-{}, confirmOverdue-{}", notQuotedLogIds.size(), quotedLogIds.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoCloseOverdueOrderQuote() {
        log.info("OrderTaskService.autoCloseOverdueOrderQuote.start");
        int quoteExpiredMinutes = productDictService.getQuoteExpiredMinutes();
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.offset(now, DateField.MINUTE, -quoteExpiredMinutes);
        log.info("OrderTaskService.autoCloseOverdueOrderQuote.info: now-{}, over expired min-{}, overdue time-{}", now, quoteExpiredMinutes, offset);

        // 找出已过期的订单列表
        List<Order> list = orderService.lambdaQuery()
                .select(Order::getId)
                .eq(Order::getQuotable, true)
                .le(AbstractBaseEntity::getCreateTime, offset)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(list)) {
            log.info("OrderTaskService.autoCloseOverdueOrderQuote.end: no available order");
            return;
        }
        // 批量更新 关闭报价功能
        List<Long> orderIds = list.stream().map(Order::getId).collect(Collectors.toList());
        orderService.lambdaUpdate()
                .set(Order::getQuotable, false)
                .in(Order::getId, orderIds)
                .update(new Order());
        // 批量日志
        orderLogService.addLogBatch(null,
                orderIds,
                OrderStatusEnum.UNCHECKED.getCode(),
                OrderOperationEnum.AUTO_CLOSE_QUOTE.getCode(),
                OrderOperationEnum.AUTO_CLOSE_QUOTE.getDesc(),
                OrderOperationEnum.AUTO_CLOSE_QUOTE.getRemark());
        log.info("OrderTaskService.autoCloseOverdueOrderQuote.end: closed {} orders quote function", orderIds.size());

        // 找出关联的未报价报价记录
        List<OrderQuotePriceLog> quoteLogs = orderQuotePriceLogService.lambdaQuery()
                .in(OrderQuotePriceLog::getOrderId, orderIds)
                .eq(OrderQuotePriceLog::getQuoted, false)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(quoteLogs)) {
            log.info("OrderTaskService.autoCloseOverdueOrderQuote.end: no available quote log");
            return;
        }
        // 未报价的更新为已作废-超时未报价
        List<Long> notQuotedLogIds = quoteLogs.stream().map(OrderQuotePriceLog::getId).collect(Collectors.toList());
        updateQuoteCanceled(notQuotedLogIds, OrderQuoteLogSubStatusEnum.QUOTE_OVERDUE, OrderOperationEnum.QUOTE_CANCELED_OVERDUE);
        log.info("OrderTaskService.autoCloseOverdueOrderQuote.end: updated quoteOverdue-{}", notQuotedLogIds.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoCloseOverdueShippingOrder() {
        log.info("OrderTaskService.autoCloseOverdueShippingOrder.start");
        int shippingOrderExpiredMinutes = productDictService.getShippingOrderExpiredMinutes();
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.offset(now, DateField.MINUTE, -shippingOrderExpiredMinutes);
        log.info("OrderTaskService.autoCloseOverdueShippingOrder.info: now-{}, over expired min-{}, overdue time-{}", now, shippingOrderExpiredMinutes, offset);
        // 找出超时的发货订单
        List<ShippingOrder> shippingOrders = shippingOrderService.lambdaQuery()
                .select(ShippingOrder::getId, ShippingOrder::getStatus)
                .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                .le(AbstractBaseEntity::getCreateTime, offset)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(shippingOrders)) {
            log.info("OrderTaskService.autoCloseOverdueShippingOrder.end: no available shipping order");
            return;
        }
        // 更新为已取消
        List<Long> shippingOrderIds = shippingOrders.stream().map(ShippingOrder::getId).collect(Collectors.toList());
        boolean success = shippingOrderService.lambdaUpdate()
                .set(ShippingOrder::getStatus, ShippingOrderStatusEnum.CANCELED.getCode())
                .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                .in(ShippingOrder::getId, shippingOrderIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new ShippingOrder());
        if (success) {

            orderLogService.addLogBatch(null,
                    shippingOrderIds,
                    ShippingOrderStatusEnum.CANCELED.getCode(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getCode(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getDesc(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getRemark());
            log.info("OrderTaskService.autoCloseOverdueShippingOrder.info: closed {} shipping orders", shippingOrderIds.size());
        }
        // 找出关联报价单
        List<Long> orderIds = shippingOrderRelService.listOrderIdsByShippingOrderIds(shippingOrderIds);
        if (CollUtil.isEmpty(orderIds)) {
            log.info("OrderTaskService.autoCloseOverdueShippingOrder.end: no available relation order");
            return;
        }
        // 更新为待发货
        success = orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .in(Order::getId, orderIds)
                .update(new Order());
        if (success) {
            // 插入自动取消日志
            orderLogService.addLogBatch(null,
                    orderIds,
                    OrderStatusEnum.PENDING_SHIPMENT.getCode(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getCode(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getDesc(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getRemark());
            log.info("OrderTaskService.autoCloseOverdueShippingOrder.info: rollback {} relation orders", orderIds.size());
        }
        log.info("OrderTaskService.autoCloseOverdueShippingOrder.end");
    }

    private void updateQuoteCanceled(List<Long> quotedLogIds, OrderQuoteLogSubStatusEnum subStatus, OrderOperationEnum operation) {
        if (CollUtil.isEmpty(quotedLogIds)) {
            return;
        }
        orderQuotePriceLogService.lambdaUpdate()
                .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.CANCELED.getCode())
                .set(OrderQuotePriceLog::getSubStatus, subStatus.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .in(OrderQuotePriceLog::getId, quotedLogIds)
                .update(new OrderQuotePriceLog());
        // 日志
        orderLogService.addLogBatch(null,
                quotedLogIds,
                OrderQuoteLogStatusEnum.CANCELED.getCode(),
                operation.getCode(),
                operation.getDesc(),
                operation.getRemark());
    }
}