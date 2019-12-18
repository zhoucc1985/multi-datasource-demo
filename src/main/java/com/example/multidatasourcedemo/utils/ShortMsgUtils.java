package com.example.multidatasourcedemo.utils;

import net.sf.json.JSONObject;
import org.apache.commons.io.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoucc
 * @date 2019-12-18 12:21
 *
 * 重庆电子工程职业学院短信推送接口
 */

public class ShortMsgUtils {

    private static HttpClient httpClient =  new DefaultHttpClient();

    /**
     * 获取token的URL
     */
    private static String TOKEN_URL = "https://10.150.11.22:8244/token";

    /**
     * 发送短信的URL
     */
    private static String SHORT_MESSAGE_URL = "https://10.150.11.22:8244/weichat/v1.0/mpTemplateMessage/sendMessage";

    /**
     * 获取token
     */
    private static String getToken() {
        HttpPost httpPost = new HttpPost(TOKEN_URL);
        httpPost.setHeader("Authorization", "Basic Wlk1YVBrQ0lPNkgxc1FKRjZ1VkVwY0RaN3IwYTo0VjZiZExiR1FvdkltbFNhS1BtbGFOX1J4akFh");
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        paramList.add(new BasicNameValuePair("username", "zhxy"));
        paramList.add(new BasicNameValuePair("password", "zhxy!@34"));
        paramList.add(new BasicNameValuePair("grant_type", "password"));

        StringBuffer buffer = new StringBuffer();
        httpPost.setEntity(new UrlEncodedFormEntity(paramList, Charsets.UTF_8));
        try {
            HttpResponse response = httpClient.execute(httpPost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), Charsets.UTF_8));
            String line = "";
            while ((line = rd.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.fromObject(buffer.toString()).getString("access_token");
    }


    /**
     * 发送短信
     * @param templateId 短信模版id
     * @param receiverId 接收短信用户id
     */
    public static void sendShortMsg(String templateId, String receiverId) {
        enableSSL();
        String token = getToken();
        HttpPost httpPost = new HttpPost(SHORT_MESSAGE_URL);
        httpPost.setHeader("Authorization", "Bearer "+token);
        httpPost.setHeader("Content-Type","application/json; charset=UTF-8");
        String json = "{\"msgType\":\"SMS\",\"senderName\":\"一站式\",\"senderId\":\"OAUTH\",\"recipientInfo\":\""
                + receiverId + "\",\"type\":\"2\",\"content\":\"{}\",\"" + templateId + "\":\"CDYPT01\",\"userTypes\":\"3\"}";
        StringEntity requestEntity = new StringEntity(json,"utf-8");
        requestEntity.setContentEncoding("UTF-8");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(requestEntity);
        try {
            httpClient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 信任所有的证书
     */
    private static TrustManager truseAllManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }

        @Override
        public void checkClientTrusted(X509Certificate[] cert, String oauthType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] cert, String oauthType)
                throws java.security.cert.CertificateException {
        }
    };

    private static void enableSSL() {
        if (!TOKEN_URL.startsWith("https")) {
            return;
        }
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme https = new Scheme("https", sf, 443);
            httpClient.getConnectionManager().getSchemeRegistry().register(https);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
