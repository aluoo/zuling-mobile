package com.zxtx.hummer.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * HttpClient
 * <p>
 * http get、post
 *
 * @author quan
 */
public class HttpClientUtil {

//    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static PoolingHttpClientConnectionManager cm;

    private static int timeout = 30 * 1000;

    static {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
            Registry<ConnectionSocketFactory> reg = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", new SSLConnectionSocketFactory(sslContext)).build();
            cm = new PoolingHttpClientConnectionManager(reg);
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(100);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private static CloseableHttpClient getHttpclient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)

                .setSocketTimeout(timeout).build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(cm);
        return httpClientBuilder.build();
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return
     */
    public static String sendGetAndHeader(String url, Map<String, String> header) {
        HttpGet httpget = new HttpGet(url);
        if (header != null) {
            Set<String> keys = header.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                httpget.addHeader(key, header.get(key));
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = getHttpclient().execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return
     */
    public static String sendGet(String url) {

        return sendGetAndHeader(url, null);
    }

    /**
     * 发送HttpGet请求，参数为param
     *
     * @param url
     * @return
     */
    public static String sendGet(String url, Map<String, String> param) {

        String params = "";
        for (Map.Entry<String, String> entry : param.entrySet()) {
            params += entry.getKey() + "=" + entry.getValue() + "&";
        }

        params = params.substring(0, params.length() - 1);
        url += "?" + params;

        return sendGetAndHeader(url, null);
    }

    /**
     * 发送HttpGet请求，参数为param
     *
     * @param url
     * @return
     */
    public static String sendGet(String url, Map<String, String> header, Map<String, String> param) {

        String params = "";
        for (Map.Entry<String, String> entry : param.entrySet()) {
            params += entry.getKey() + "=" + entry.getValue() + "&";
        }

        params = params.substring(0, params.length() - 1);
        url += "?" + params;

        return sendGetAndHeader(url, header);
    }

    /**
     * 发送HttpPost请求，参数为param
     *
     * @param url
     * @param
     * @return
     */
    public static String sendPost(String url, Map<String, String> param) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        String result = null;

        try {
            response = getHttpclient().execute(httppost);
            if (response != null) {
                HttpEntity entity1 = response.getEntity();
                result = EntityUtils.toString(entity1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return result;
    }

    /**
     * 发送HttpPost请求，参数转为json
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendPostJson(String url, Map<String, String> header, JSONObject param) {
        String result = null;
        try {
            StringEntity s = new StringEntity(param.toString(), "UTF-8");
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");

            HttpPost httppost = new HttpPost(url);

            if (header != null) {
                Set<String> keys = header.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    httppost.addHeader(key, header.get(key));
                }
            }

            httppost.setEntity(s);
            CloseableHttpResponse response = getHttpclient().execute(httppost);
            HttpEntity entity1 = response.getEntity();

            result = EntityUtils.toString(entity1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 避免跟高速调用冲突
     *
     * @param url
     * @param header
     * @param param
     * @param charset
     * @return
     */
    public static String sendPostJson(String url, Map<String, String> header, JSONObject param, String charset) {
        String result = null;
        try {
            StringEntity s = new StringEntity(param.toString(), charset);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");

            HttpPost httppost = new HttpPost(url);

            if (header != null) {
                Set<String> keys = header.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    httppost.addHeader(key, header.get(key));
                }
            }

            httppost.setEntity(s);
            CloseableHttpResponse response = getHttpclient().execute(httppost);
            HttpEntity entity1 = response.getEntity();

            result = EntityUtils.toString(entity1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送HttpPost请求，参数转为json
     *
     * @param url
     * @param param
     * @return
     */
    public static byte[] sendPost(String url, JSONObject param) {
        byte[] result = null;
        try {
            StringEntity s = new StringEntity(param.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");

            HttpPost httppost = new HttpPost(url);

            httppost.setEntity(s);
            CloseableHttpResponse response = getHttpclient().execute(httppost);
            HttpEntity entity1 = response.getEntity();

            result = EntityUtils.toByteArray(entity1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送HttpPost请求，参数转为json
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendPostJson(String url, JSONObject param) {
        return sendPostJson(url, null, param);

    }

    public static String sendPostJson(String url, JSONObject param, String charset) {
        return sendPostJson(url, null, param, charset);

    }

    /**
     * 发送不带参数的HttpPost请求
     *
     * @param url
     * @return
     */
    public static String sendPost(String url) {
        HttpPost httppost = new HttpPost(url);
        CloseableHttpResponse response = null;

        try {
            response = getHttpclient().execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity entity = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String sendDelete(String url, Map<String, String> headerMap) {

        String result = null;
        try {
            HttpDelete httpDelete = new HttpDelete(url);
            if (headerMap != null) {
                Set<String> keys = headerMap.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    httpDelete.addHeader(key, headerMap.get(key));
                }
            }
            CloseableHttpResponse response = getHttpclient().execute(httpDelete);
            HttpEntity entity1 = response.getEntity();
            result = EntityUtils.toString(entity1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
