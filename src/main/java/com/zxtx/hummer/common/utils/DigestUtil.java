package com.zxtx.hummer.common.utils;

import sun.misc.BASE64Encoder;

import java.io.Serializable;
import java.security.MessageDigest;

/**
 * 生成摘要工具类
 */
@SuppressWarnings({"serial", "restriction"})
public class DigestUtil implements Serializable {

    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    public static String encryptBASE64(byte[] md5) {
        return (new BASE64Encoder()).encodeBuffer(md5);
    }

    /**
     * 摘要生成
     *
     * @param data    请求数据
     * @param sign    签名秘钥(key或者parternID)
     * @param charset 编码格式
     * @return 摘要
     * @throws Exception
     */
    public static String digest(String data, String sign, String charset)
            throws Exception {
        String t = encryptBASE64(encryptMD5((data + sign).getBytes(charset)));
        if ("\n".equals(System.getProperty("line.separator"))) {
            String t2 = t.replaceAll("\\n", "\r\n");
            t2 = replaceNull(t2);
            return t2;
        } else {
            return replaceNull(t);
        }
    }

    public static String replaceNull(String url) throws Exception {
        if (null == url) {
            return "";
        } else {
            url = url.replace("/", "");
            return url;
        }
    }


    /**
     * 测试方法
     */
    public static void main(String[] args) {

        String params = "{\"orderNo\":\"ZYJ55535585\",\"ewbNo\":\"0\",\"subOrderId\":null,\"sendSiteId\":\"2256\",\"sendLinkMan\":\"黄忠\",\"sendPhone\":\"13241406257\",\"sendAddress\":\"金源购物中心\",\"sendProvince\":\"北京\",\"sendCity\":\"北京市\",\"sendCounty\":\"海淀区\",\"dispatchSiteId\":null,\"dispatchLinkMan\":\"王老五\",\"dispatchPhone\":\"13578900056\",\"dispatchAddress\":\"北京植物园\",\"dispatchProvince\":\"北京\",\"dispatchCity\":\"北京市\",\"dispatchCounty\":\"海淀区\",\"items\":\"{\"goodsType\":0,\"goodsName\":\"黄瓜\",\"piece\":1}\",\"totalPiece\":5,\"totalWeight\":0.0,\"totalVol\":0.0,\"ecId\":557,\"payModeId\":0,\"freightCharge\":0.0,\"orderRemark\":null,\"deliveryType\":null,\"itemType\":null}";

        params = "{\"sendLinkMan\":\"51ETC物料中心\",\"sendCity\":\"成都市\",\"dispatchLinkMan\":\"51ETC物料中心\",\"orderNo\":607946107893972992,\"sendCounty\":\"双流县\",\"goodsCategory\":\"1\",\"sendProvince\":\"四川省\",\"dispatchAddress\":\"文星镇空港三路215号\",\"dispatchCity\":\"成都市\",\"dispatchPhone\":\"13980687435\",\"parcelPiece\":1,\"packageType\":\"箱\",\"sendAddress\":\"文星镇空港三路215号\",\"dispatchCounty\":\"双流县\",\"itemName\":\"江苏OBU\",\"sellerId\":\"571038000004\",\"pickGoodsModeId\":111,\"ecId\":\"639\",\"totalPiece\":1,\"ewbNo\":0,\"sendPhone\":\"13980687435\",\"dispatchProvince\":\"四川省\",\"insuredAmount\":\"15000\",\"items\":[{\"piece\":1,\"goodsName\":\"江苏OBU\",\"goodsType\":1}]}";
        String digest = "";
        try {
            //生成签名
            digest = DigestUtil.digest(params, "AMfUS3wFy", "utf-8");
            System.out.println("签名：" + digest);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
