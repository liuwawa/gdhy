package io.renren.modules.api.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import io.renren.common.exception.RRException;
import io.renren.common.utils.MsgStatus;
import io.renren.common.utils.RedisCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by ITMX on 2017/12/6.
 */
@Component
public class SmsUtils {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisCacheUtils redisCacheUtils;

    @Value("${private.sms.product}")    private String product;
    @Value("${private.sms.domain}")    private String domain;
    @Value("${private.sms.accessKeyId}")    private String accessKeyId;
    @Value("${private.sms.accessKeySecret}")    private String accessKeySecret;
    @Value("${private.sms.endpointName}")    private String endpointName;
    @Value("${private.sms.regionId}")    private String regionId;
    @Value("${private.sms.smsTimeout}")    private long smsTimeout;
    @Value("${private.sms.sign}")    private String sign;
    @Value("${private.sms.maxErrCount}")    private Integer maxErrCount;
    //短信模板==================================================
    @Value("${private.sms.tpl.userReg}")    private String userRegTpl;
    @Value("${private.sms.tpl.modifyOrFindPass}")    private String modifyOrFindPassTpl;
    @Value("${private.sms.tpl.smsLogin}")    private String smsLoginTpl;
    @Value("${private.sms.tpl.modifyPhone}")    private String modifyPhoneTpl;
    @Value("${private.sms.tpl.withdrawals}")    private String withdrawalsTpl;
    @Value("${private.sms.tpl.inviteRegOk}")    private String inviteRegOkTpl;
    //短信模板================================================end

    public enum SMS_TYPE{
        USER_REG,
        MODIFY_OR_FIND_PASS,
        SMS_LOGIN,
        MODIFY_PHONE,
        INVITE_REG_OK,
        WITHDRAWALS
    }

    /**
     * 发送提现验证码
     * @param phone
     * @return int 0：下发失败，1下发成功，-1下发异常,-2发送频率过高
     */
    public int withdrawals(String phone){

        logger.info(phone + "请求提现验证短信");

        //产生验证码随机数
        String smsCode = randCode();
        logger.info(phone + "随机验证码产生：" + smsCode);

        SendSmsRequest request = createSmsReq(phone, withdrawalsTpl, smsCode);

        int sendResult = doSend(request,SMS_TYPE.WITHDRAWALS);
        if(sendResult == 1){
            redisCacheUtils.set("SMS:" + SMS_TYPE.WITHDRAWALS + "_" + request.getPhoneNumbers(),smsCode,smsTimeout);
        }
        return sendResult;
    }

    /**
     * 发送修改手机号验证码
     * @param phone
     * @return int 0：下发失败，1下发成功，-1下发异常,-2发送频率过高
     */
    public int modifyPhone(String phone){

        logger.info(phone + "请求修改手机号验证短信");

        //产生验证码随机数
        String smsCode = randCode();
        logger.info(phone + "随机验证码产生：" + smsCode);

        SendSmsRequest request = createSmsReq(phone, modifyPhoneTpl, smsCode);

        int sendResult = doSend(request,SMS_TYPE.MODIFY_PHONE);
        if(sendResult == 1){
            redisCacheUtils.set("SMS:" + SMS_TYPE.MODIFY_PHONE + "_" + request.getPhoneNumbers(),smsCode,smsTimeout);
        }
        return sendResult;
    }

    /**
     * 发送用户注册验证码
     * @param phone
     * @return int 0：下发失败，1下发成功，-1下发异常,-2发送频率过高
     */
    public int userReg(String phone){

        logger.info(phone + "请求发送用户注册短信");

        //产生验证码随机数
        String smsCode = randCode();
        logger.info(phone + "随机验证码产生：" + smsCode);

        SendSmsRequest request = createSmsReq(phone, userRegTpl, smsCode);

        int sendResult = doSend(request,SMS_TYPE.USER_REG);
        if(sendResult == 1){
            redisCacheUtils.set("SMS:" + SMS_TYPE.USER_REG + "_" + request.getPhoneNumbers(),smsCode,smsTimeout);
        }
        return sendResult;
    }

    /**
     * 发送登录验证码
     * @param phone
     * @return int 0：下发失败，1下发成功，-1下发异常,-2发送频率过高
     */
    public int smsLogin(String phone){

        logger.info(phone + "请求发送登录验证码");

        //产生验证码随机数
        String smsCode = randCode();
        logger.info(phone + "随机验证码产生：" + smsCode);

        SendSmsRequest request = createSmsReq(phone, smsLoginTpl, smsCode);

        int sendResult = doSend(request,SMS_TYPE.SMS_LOGIN);
        if(sendResult == 1){
            redisCacheUtils.set("SMS:" + SMS_TYPE.SMS_LOGIN + "_" + request.getPhoneNumbers(),smsCode,smsTimeout);
        }
        return sendResult;
    }

    /**
     * 发送修改/找回密码验证码
     * @param phone
     * @return int 0：下发失败，1下发成功，-1下发异常,-2发送频率过高
     */
    public int modifyOrFindPass(String phone) {

        logger.info(phone + "请求发送修改/找回密码短信");

        //产生验证码随机数
        String smsCode = randCode();
        logger.info(phone + "随机验证码产生：" + smsCode);

        SendSmsRequest request = createSmsReq(phone, modifyOrFindPassTpl, smsCode);

        int sendResult = doSend(request,SMS_TYPE.MODIFY_OR_FIND_PASS);
        if(sendResult == 1){
            redisCacheUtils.set("SMS:" + SMS_TYPE.MODIFY_OR_FIND_PASS + "_" + request.getPhoneNumbers(),smsCode,smsTimeout);
        }
        return sendResult;
    }

    /**
     * 邀请注册成功，通知用户初始密码。模板：${username}恭喜您注册成功，您的初始登录密码为：${passwd}。
     * @param phone
     * @param header
     * @param footer
     * @return
     */
    public int inviteRegSuccess(String phone,String header,String footer){
        logger.info(phone + "请求发送邀请注册成功短信");

        SendSmsRequest request = createRegOkReq(phone, inviteRegOkTpl, header,footer);

        return doSend(request,SMS_TYPE.INVITE_REG_OK);
    }

    /**
     * 校验验证码是否正确
     * @param phone
     * @param smsCode
     * @param smsType
     * @return
     */
    public boolean validate(String phone,String smsCode,SMS_TYPE smsType){

        if(phone == null || smsCode == null){
            return false;
        }

        String code = redisCacheUtils.get("SMS:" + smsType + "_" + phone);
        boolean equals = Objects.equals(code, smsCode);
        if(equals){
            //如果验证通过，将验证码移除
            //redisCacheUtils.delete("SMS:" + smsType + "_" + phone);
            //redisCacheUtils.delete("SMS:ERROR_COUNT_" + phone);
        }else{
            //如果确实下发过验证码，并且在验证码有效期
            if (code!= null) {
                //那就记录失败次数
                Integer errCount = redisCacheUtils.get("SMS:ERROR_COUNT_" + phone);
                if(errCount == null){
                    errCount = 0;
                }
                //如果错误次数过多
                if(++errCount >= maxErrCount){
                    //重置错误计数，并将验证码移除
                    redisCacheUtils.delete("SMS:" + smsType + "_" + phone);
                    redisCacheUtils.delete("SMS:ERROR_COUNT_" + phone);
                    throw new RRException(MsgStatus.EXCESSIVE_NUMBER_OF_ERRORS);
                }else{
                    redisCacheUtils.set("SMS:ERROR_COUNT_" + phone,errCount,smsTimeout);
                }
            }
        }
        return equals;
    }

    /**
     * 快速生成6位随机数
     * @return
     */
    private static String randCode(){
        return String.valueOf((int) ((Math.random() * 9 + 1) * 1000000)).substring(1);
    }

    /**
     * 创建一个只含code变量的SendSmsRequest
     * @param phone
     * @param templateCode
     * @param smsCode
     * @return
     */
    private SendSmsRequest createSmsReq(String phone,String templateCode,String smsCode){
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(sign);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + smsCode + "\"}");
        return request;
    }

    /**
     * 创建一个包含username、passwd变量的SendSmsRequest
     * @param phone
     * @param templateCode
     * @param username
     * @param passwd
     * @return
     */
    private SendSmsRequest createRegOkReq(String phone,String templateCode,String username,String passwd){
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(sign);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"username\":\"" + username + "\",\"passwd\":\"" + passwd + "\"}");
        return request;
    }



    /**
     * 下发短信
     * @param request
     * @return int 0：下发失败，1下发成功，-1下发异常,-2发送频率过高
     */
    private int doSend(SendSmsRequest request,SMS_TYPE smsType){
        try {
            //判断是否存在标志
            Object s = redisCacheUtils.get("SMS:SEND_" + smsType + "_" + request.getPhoneNumbers());
            if(s!=null){
                return -2;
            }

            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint(endpointName, regionId, product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsResponse ssp = acsClient.getAcsResponse(request);

            logger.info(request.getPhoneNumbers() + "短信验证码请求完毕");

            if(ssp.getCode() != null && ssp.getCode().equals("OK")){
                logger.info(request.getPhoneNumbers() + "短信验证码下发成功");
                //重置错误次数
                redisCacheUtils.delete("SMS:ERROR_COUNT_" + request.getPhoneNumbers());
                redisCacheUtils.delete("VERIFY_SMS_FAIL:" + request.getPhoneNumbers());
                //标识该号码已发送短信，标志于一分钟后失效
                redisCacheUtils.set("SMS:SEND_" + smsType + "_" + request.getPhoneNumbers(),true,60);
                return 1;
            }else{
                logger.error(request.getPhoneNumbers() + "短信验证码下发失败：" + new Gson().toJson(ssp));
                //如果时发送频率过高导致的失败则返回 -2
                return Objects.equals(ssp.getCode(), "isv.BUSINESS_LIMIT_CONTROL")?-2:0;
            }
        } catch (ClientException e) {
            logger.error(request.getPhoneNumbers() + "短信验证码下发出现异常",e);
            return -1;
        }
    }

}
