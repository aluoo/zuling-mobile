package com.zxtx.hummer.common.wx;

import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.ecommerce.*;
import com.github.binarywang.wxpay.bean.ecommerce.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.wx.dto.CommonRefundReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CommonWxPayService {
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private MchIdService mchIdService;

    /**
     * 申请支付
     * @param req PartnerTransactionsRequest
     * @return TransactionsResult.JsapiResult
     */
    public TransactionsResult.JsapiResult partnerTransactions(PartnerTransactionsRequest req) {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        try {
            log.info("partnerTransactions.info: req-{}", JSONUtil.toJsonStr(req));
            TransactionsResult.JsapiResult res = wxPayService.getEcommerceService().partnerTransactions(TradeTypeEnum.JSAPI, req);
            log.info("partnerTransactions.info: res-{}", JSONUtil.toJsonStr(res));
            return res;
        } catch (WxPayException e) {
            log.error("partnerTransactions.error: {}", e.getCustomErrorMsg());
            throw new BaseException(-1, e.getErrCode() + e.getErrCodeDes());
        }
    }

    /**
     * 查询支付订单
     * @param req PartnerTransactionsQueryRequest
     * @return PartnerTransactionsResult
     */
    public PartnerTransactionsResult queryPartnerTransactions(PartnerTransactionsQueryRequest req) {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        try {
            log.info("queryPartnerTransactions.info: req-{}", JSONUtil.toJsonStr(req));
            PartnerTransactionsResult res = wxPayService.getEcommerceService().queryPartnerTransactions(req);
            log.info("queryPartnerTransactions.info: res-{}", JSONUtil.toJsonStr(res));
            return res;
        } catch (WxPayException e) {
            log.error("queryPartnerTransactions.error: {}", e.getCustomErrorMsg());
            throw new BaseException(-1, e.getErrCode() + e.getErrCodeDes());
        }
    }

    /**
     * 关闭支付订单
     * @param outTradeNo String
     */
    public void closePartnerTransactions(String outTradeNo) {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsCloseRequest req = new PartnerTransactionsCloseRequest();
        req.setOutTradeNo(outTradeNo);
        req.setSpMchid(wxPayService.getConfig().getMchId());
        req.setSubMchid(wxPayService.getConfig().getSubMchId());
        try {
            wxPayService.getEcommerceService().closePartnerTransactions(req);
            log.info("closePartnerTransactions.info: closed");
        } catch (WxPayException e) {
            log.error("closePartnerTransactions.error: {}", JSONUtil.toJsonStr(e));
            throw new BaseException(-1, e.getErrCode() + e.getErrCodeDes());
        }
    }

    public RefundsResult refundPartnerTransactions(CommonRefundReq req) {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        RefundsRequest.Amount amount = RefundsRequest.Amount.builder()
                .total(req.getFee())
                .refund(req.getRefundFee())
                .currency("CNY")
                .build();
        RefundsRequest refundsReq = RefundsRequest.builder()
                // 服务商商户
                .spAppid(wxPayService.getConfig().getAppId())
                // 子商户
                .subAppid(wxPayService.getConfig().getSubAppId())
                .subMchid(wxPayService.getConfig().getSubMchId())
                .amount(amount)
                .outTradeNo(req.getTradeNo())
                .transactionId(req.getTransactionId())
                .outRefundNo(req.getRefundNo())
                .build();

        try {
            log.info("refundPartnerTransactions.info: req-{}", JSONUtil.toJsonStr(refundsReq));
            RefundsResult res = wxPayService.getEcommerceService().refunds(refundsReq);
            log.info("refundPartnerTransactions.info: res-{}", JSONUtil.toJsonStr(res));
            return res;
        } catch (WxPayException e) {
            log.error("refundPartnerTransactions.error: {}", JSONUtil.toJsonStr(e));
            throw new BaseException(-1, e.getErrCode() + e.getErrCodeDes());
        }
    }

    public PartnerTransactionsNotifyResult parseNotify(String data, String mchId) {
        wxPayService.switchover(mchId);
        PartnerTransactionsNotifyResult notifyResult;
        try {
            notifyResult = wxPayService.getEcommerceService().parsePartnerNotifyResult(data, null);
        } catch (WxPayException e) {
            log.error("parseNotify.error: {}", e.getCustomErrorMsg());
            throw new BaseException(-1, e.getErrCode() + e.getErrCodeDes());
        }
        if (notifyResult == null) {
            log.error("parseNotify.error: notifyResult is null");
            throw new BaseException(BizError.OUTER_ERROR);
        }
        log.info("parseNotify.info: notifyResult-{}", JSONUtil.toJsonStr(notifyResult));
        return notifyResult;
    }
}