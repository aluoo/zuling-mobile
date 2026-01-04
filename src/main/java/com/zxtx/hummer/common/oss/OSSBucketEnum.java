package com.zxtx.hummer.common.oss;

import lombok.Getter;

@Getter
public enum OSSBucketEnum {
    //合同
    AYXY_CONTRACT("aycx-contract"),
    //行驶证
    AYXY_VEHICLE("aycx-vehicle"),
    //司机身份证
    AYXY_DRIVER("aycx-driver"),
    //车头
    AYXY_CAR_HEAD("aycx-cat-head"),
    //微信相关图片
    AYXY_WECHAT("aycx-wechat"),
    //高速公司来的文件备份
    AYXY_HIGHWAY("aycx-highway"),

    // 暂时存放APK
    AYXY_SELF("aycx-self"),
    AYCX_APK("aycx-apk"),
    AYCX_PRODUCT("aycx-product"),
    AYCX_MEMBER_BENEFIT("aycx-member-benefit"),
    AYCX_NOTICE("aycx-notice"),
    // 视频&封面
    AYCX_VIDEO_MANAGEMENT("aycx-video-management"),
    // 客服上传凭证
    AYCX_CUSTOMER("aycx-customer"),

    // 客诉工单
    AYCX_WORK_ORDER("aycx-work-order"),
    ;

    private final String name;

    OSSBucketEnum(String name) {
        this.name = name;
    }
}