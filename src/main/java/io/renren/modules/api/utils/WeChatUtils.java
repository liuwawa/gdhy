package io.renren.modules.api.utils;

import io.renren.common.utils.SignUtils;
import io.renren.modules.admin.entity.WithdrawalsEntity;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by ITMX on 2017/12/11.
 */
@Component
public class WeChatUtils {

    @Value("${private.wx.appid}")
    private String appid;

    @Value("${private.wx.app_secret}")
    private String appSecret;

    //------------------微信支付
    @Value("${private.wx.pay.cert_path}")
    private String certPath;

    @Value("${private.wx.pay.sign_key}")
    private String signKey;

    @Value("${private.wx.pay.mch_appid}")
    private String mchId;

    @Value("${private.wx.pay.local_ip}")
    private String localIp;
    //-----------------微信支付end

    private RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8"))).build();

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 通过code兑换openid
     * @param code
     * @return
     */
    public JSONObject code2openid(String code) throws WeChatApiException {

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";

        String result = null;
        try {
            result = restTemplate.getForObject(url,String.class);
        } catch (RestClientException e) {
            throw new WeChatApiException("请求微信code2openid接口失败",e);
        }
        if(result == null){
            throw new WeChatApiException("请求微信code2openid接口失败");
        }

        logger.debug("获取微信access_token：" + result);

        JSONObject jsonObject = JSONObject.fromObject(result);
        if(jsonObject.has("errcode")){
            throw new WeChatApiException(jsonObject.getString("errcode"),jsonObject.getString("errmsg"));
        }

        return jsonObject;
    }

    /**
     * 根据用户openid获取微信用户资料
     * @param openid
     * @param accessToken
     * @return
     */
    public JSONObject getUserInfoByOpenid(String openid,String accessToken) throws WeChatApiException {
        //注册用户
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid;
        //获取用户信息
        String result = null;
        try {
            result = restTemplate.getForObject(url,String.class);
        } catch (RestClientException e) {
            throw new WeChatApiException("请求微信userinfo接口失败",e);
        }
        if(result == null){
            throw new WeChatApiException("请求微信userinfo接口失败");
        }

        logger.debug("获取微信用户信息：" + result);

        JSONObject userInfo = JSONObject.fromObject(result);

        if(userInfo.has("errcode") && userInfo.getInt("errcode") != 0){
            throw new WeChatApiException(userInfo.getString("errcode"),userInfo.getString("errmsg"));
        }
        //修改头像大小
        String avatar = userInfo.getString("headimgurl");
        if(!StringUtils.isEmpty(avatar)) {
            avatar = avatar.substring(0, avatar.lastIndexOf("/")) + "/132";
            userInfo.put("headimgurl", avatar);
        }
        return userInfo;
    }


    /**
     * 微信提现，链接参数
     */
    public HashMap<String, Object> createTransfersParam(WithdrawalsEntity withdrawalsEntity) {
        HashMap<String,Object> map = new HashMap<>();

        map.put("mch_appid", appid);//商户账号appid
        map.put("mchid", mchId);//商户号
        map.put("nonce_str", UUID.randomUUID().toString().trim().replaceAll("-", ""));// 随机字符串
        map.put("partner_trade_no",withdrawalsEntity.getId());// 商户订单号
        map.put("openid", withdrawalsEntity.getUserAccount());//收款用户openid
        map.put("check_name", "FORCE_CHECK");// 校验用户姓名选项
        map.put("re_user_name",withdrawalsEntity.getName()); //设置收款人名称
        map.put("amount", withdrawalsEntity.getRmb());//金额
        map.put("desc", "大众看点提现" + (withdrawalsEntity.getRmb()/100.0) + "元");//企业付款描述信息
        map.put("spbill_create_ip", localIp);

        String sign = SignUtils.doSign("key=" + signKey, map);
        map.put("sign",sign.toUpperCase());//签名，变大写

        return map;
    }

    /**
     * 向微信用户个人付款
     * @param paramMap
     * @return
     */
    public Map<String, String> transfers(HashMap<String, Object> paramMap) throws WeChatApiException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        //注册用户
        String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

        String postData = XMLUtil.mapToXML(paramMap).toString();

        logger.debug("向微信用户个人付款请求：" + postData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONNECTION, "keep-alive");
        headers.add(HttpHeaders.ACCEPT, "*/*");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        headers.add(HttpHeaders.HOST, "api.mch.weixin.qq.com");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headers.add(HttpHeaders.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");

        HttpEntity<String> formEntity = new HttpEntity<>(postData,headers);

        String result;
        String error;
        try {
            result = sslRestTemplate().postForObject(url, formEntity, String.class);
        } catch (RestClientException e) {
            error = "请求向微信用户个人付款接口失败";
            logger.error(error + "，请求数据：" + postData,e);
            throw new WeChatApiException(error,e);
        }

        if(result == null){
            logger.error("请求向微信用户个人付款接口，响应数据为空，请求数据：" + postData);
            throw new WeChatApiException("请求向微信用户个人付款接口失败");
        }

        logger.debug("向微信用户个人付款结果：" + result);

        HashMap<String, String> responseHashMap = XMLUtil.xmlToMap(result);

        //检查通信情况
        if(!"SUCCESS".equals(responseHashMap.get("return_code"))){
            logger.error("请求向微信用户个人付款接口，通信失败，请求数据：" + postData);
            throw new WeChatApiException(responseHashMap.get("return_code"),responseHashMap.get("return_msg"));
        }

        //检查业务处理情况
        if(!"SUCCESS".equals(responseHashMap.get("result_code"))){
            logger.error("请求向微信用户个人付款接口：" + responseHashMap.get("err_code_des"));
            throw new WeChatApiException(responseHashMap.get("err_code"),responseHashMap.get("err_code_des"));
        }

        //检查商户订单号是否一致
        if(!Objects.equals(paramMap.get("partner_trade_no"),responseHashMap.get("partner_trade_no"))){
            error="[紧急情况]转账失败，订单号不一致。提现订单：" + paramMap.get("partner_trade_no") +
                    "，微信返回：" + responseHashMap.get("partner_trade_no") +
                    "，响应：" + result;
            logger.error(error);
            throw new WeChatApiException(error);
        }

        return responseHashMap;
    }

    private RestTemplate sslRestTemplate;
    /**
     * 创建ssl RestTemplate
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    private RestTemplate sslRestTemplate() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

        if(sslRestTemplate != null){
            return sslRestTemplate;
        }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(certPath));
        keyStore.load(instream, mchId.toCharArray());
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, mchId.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,  new String[]{"TLSv1"},
                null,hostnameVerifier);

        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpclient);
        sslRestTemplate = new RestTemplate(clientHttpRequestFactory);

        return sslRestTemplate;

    }

    public class WeChatApiException extends Exception {
        private String errCode;
        private String errMsg;

        public WeChatApiException() {}

        public WeChatApiException(String message, Throwable cause) {
            super(message, cause);
        }

        public WeChatApiException(String message) {
            super(message);
        }

        public WeChatApiException(Throwable cause) {
            super(cause);
        }

        public WeChatApiException(String errCode, String errMsg) {
            super(errCode + ":" + errMsg);
            this.errCode = errCode;
            this.errMsg = errMsg;
        }

        public String getErrCode() {
            return this.errCode;
        }

        public String getErrMsg() {
            return this.errMsg;
        }
    }
}
