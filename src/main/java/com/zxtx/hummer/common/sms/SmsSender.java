package com.zxtx.hummer.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

//@Component
public class SmsSender {
    private BlockingQueue<SmsMessage> queue = new LinkedBlockingQueue<>();

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final Logger logger = LoggerFactory.getLogger(SmsSender.class);

    private volatile boolean isRunning = true;

    @Value("${ali.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${ali.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${spring.profiles.active}")
    private String env;

    @PostConstruct
    private void init() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        SmsMessage smsMessage = queue.take();
                        doSend(smsMessage);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        });
    }

    @PreDestroy
    private void destroy() {
        isRunning = false;
        executor.shutdownNow();
    }

    /**
     * 发送短信
     *
     * @param smsMessage
     */
    private void doSend(SmsMessage smsMessage) {
        if (!"production".equals(env)) {
            logger.info("非正式环境，模拟发送短信:" + smsMessage.toString());
            return;
        }
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", smsMessage.getMobile());
        request.putQueryParameter("SignName", smsMessage.getSignName());
        request.putQueryParameter("TemplateCode", smsMessage.getTemplateCode());
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(smsMessage.getParams()));
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.info("发送短信模板:{},发送手机号:{},参数:{},返回:{}", smsMessage.getTemplateCode(), smsMessage.getMobile(), JSONObject.toJSONString(smsMessage.getParams()), response.getData());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 发送短信
     *
     * @param message
     */
    public void send(SmsMessage message) {
        queue.add(message);
    }
}
