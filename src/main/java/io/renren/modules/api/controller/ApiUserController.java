package io.renren.modules.api.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.google.gson.Gson;
import io.renren.common.exception.RRException;
import io.renren.common.utils.MsgStatus;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisCacheUtils;
import io.renren.common.validator.Assert;
import io.renren.config.AlipayConfig;
import io.renren.modules.api.annotation.Login;
import io.renren.modules.api.annotation.LoginUser;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.service.UserService;
import io.renren.modules.api.utils.AppLogUtils;
import io.renren.modules.api.utils.JwtUtils;
import io.renren.modules.api.utils.SmsUtils;
import io.renren.modules.api.utils.WeChatUtils;
import io.renren.modules.invite.service.InviteRecordService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ITMX on 2017/12/6.
 */
@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private WeChatUtils weChatUtils;
    @Autowired
    private AppLogUtils appLogUtils;
    @Autowired
    private RedisCacheUtils redisCacheUtils;
//    @Autowired
//    private InviteCodeService inviteCodeService;
    @Autowired
    private InviteRecordService inviteRecordService;

    /**
     * 用户注册
     * @param phone 手机号
     * @param smsCode 短信验证码
     * @param password 密码
     * @param deviceId 设备唯一id
     * @param inviteCode 邀请码
     */
    @PostMapping("reg")
    public R register(
            @RequestParam String phone,
            @RequestParam String smsCode,
            @RequestParam String password,
            @RequestParam(required = false)String deviceId,
            @RequestParam(required = false,defaultValue = "") String inviteCode){
        Assert.isBlank(phone, "手机号不能为空");
        Assert.isBlank(smsCode, "短信验证码不能为空");
        Assert.isBlank(password, "密码不能为空");
        Assert.minLen(password,6,"密码长度至少6位");

        if(!smsUtils.validate(phone,smsCode, SmsUtils.SMS_TYPE.USER_REG)){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.PASSWD_REGISTER,"手机号注册失败，验证码错误");
            return R.error(MsgStatus.INVALID_VERIFICATION_CODE);
        }

        //检查邀请码，不为空且为数字
//        if(!StringUtils.isBlank(inviteCode) && StringUtils.isNumeric(inviteCode)) {
//            InviteCodeEntity inviteCodeEntity = inviteCodeService.queryObject(Integer.valueOf(inviteCode));
//            if (inviteCodeEntity == null || inviteCodeEntity.getState() != 0) {
//                appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.PASSWD_REGISTER, "邀请码无效");
//                inviteCode = "";
//            }
//        }else{
//            inviteCode = "";
//        }

        //检查手机号是否已注册
        UserEntity userEntity = userService.queryByPhone(phone);
        if(userEntity != null){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.PASSWD_REGISTER,"手机号注册失败，该手机号已注册");
            return R.error(MsgStatus.ALREADY_REGISTERED);
        }

        userEntity = new UserEntity();
        userEntity.setPhone(phone);
        userEntity.setUsername("用户" + phone.substring(phone.length() - 4));
        userEntity.setPassword(password);
        userEntity.setInviteCode(inviteCode);
        userEntity.setDeviceId(deviceId);

        userService.save(userEntity);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.PASSWD_REGISTER,"手机号注册成功，邀请码为：" + inviteCode,userEntity.getUserId());

        return R.ok("注册成功");
    }

    /**
     * 用户登录
     * @param phone 手机号
     * @param password 密码
     * @param deviceId 设备唯一id
     */
    @PostMapping("login")
    public R login(@RequestParam String phone,
                   @RequestParam String password,
                   @RequestParam(required = false) String deviceId){
        Assert.isBlank(phone, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");
        Assert.minLen(password,6,"密码长度至少6位");

        //用户登录
        UserEntity userEntity = userService.queryByPhone(phone);

        try {
            userService.checkPwd(userEntity, password);
        } catch (RRException e) {
            if(userEntity == null){
                appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.PWD_LOGIN,"手机号登录失败，未注册：" + phone);
            }else{
                appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.PWD_LOGIN,"手机号登录失败，密码错误",userEntity.getUserId());
            }
            throw new RRException(MsgStatus.BAD_PHONE_OR_PASSWORD);
        }

        userService.checkStatus(userEntity);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.PWD_LOGIN,"手机号登录成功",userEntity.getUserId());

        //生成token，上一次登录token将作废
        Map<String, Object> map = jwtUtils.makeToMap(jwtUtils.generateToken(userEntity.getUserId()));

        //发放奖励
//        inviteRecordService.firstReward(userEntity);

        return R.ok("登录成功").put("data",map);
    }

    /**
     * 短信验证码登录
     * @param phone 手机号
     * @param smsCode 密码
     * @param deviceId 设备唯一id
     */
    @PostMapping("smsLogin")
    public R smsLogin(@RequestParam String phone,
                      @RequestParam String smsCode,
                      @RequestParam(required = false)String deviceId){
        Assert.isBlank(phone, "手机号不能为空");
        Assert.isBlank(smsCode, "短信验证码不能为空");

        if(!smsUtils.validate(phone,smsCode, SmsUtils.SMS_TYPE.SMS_LOGIN)){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.SMS_LOGIN,"短信登录验证码不正确，手机：" + phone);
            return R.error(MsgStatus.INVALID_VERIFICATION_CODE);
        }

        UserEntity userEntity = userService.queryByPhone(phone);
        if(userEntity != null){
            userService.checkStatus(userEntity);
        }else{
            //如果没注册，就注册一个
            userEntity = new UserEntity();
            userEntity.setPhone(phone);
            userEntity.setUsername("用户" + phone.substring(phone.length() - 4));
            userEntity.setPassword(null);
            userEntity.setDeviceId(deviceId);

            userService.save(userEntity);
            appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.PHONE_REGISTER,"手机号注册成功",userEntity.getUserId());
        }

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.SMS_LOGIN,"短信登录成功",userEntity.getUserId());

        //生成token，上一次登录token将作废
        Map<String, Object> map = jwtUtils.makeToMap(jwtUtils.generateToken(userEntity.getUserId()));

        //发放奖励
//        inviteRecordService.firstReward(userEntity);

        return R.ok("登录成功").put("data",map);
    }

    /**
     * 通过短信找回密码
     * @param phone
     * @param smsCode
     * @param password
     * @return
     */
    @PostMapping("forgetPassword")
    public R forgetPassword(@RequestParam String phone,
                            @RequestParam String smsCode,
                            @RequestParam String password){
        Assert.isBlank(phone, "手机号不能为空");
        Assert.isBlank(smsCode, "短信验证码不能为空");
        Assert.isBlank(password, "密码不能为空");
        Assert.minLen(password,6,"密码长度至少6位");

        if(!smsUtils.validate(phone,smsCode, SmsUtils.SMS_TYPE.MODIFY_OR_FIND_PASS)){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.FORGET_PASSWORD,"找回密码验证码不正确，手机：" + phone);
            return R.error(MsgStatus.INVALID_VERIFICATION_CODE);
        }

        UserEntity userEntity = userService.queryByPhone(phone);
        userService.checkStatus(userEntity);
        userService.changePwd(userEntity,password);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.FORGET_PASSWORD,"找回密码成功",userEntity.getUserId());

        //将上次登录toke作废
        jwtUtils.abandoned(userEntity.getUserId());

        return R.ok("修改密码成功");
    }

    /**
     * 检查密码是否正确
     * @param oldPassword
     * @param user
     * @return
     */
    @Login
    @PostMapping("verifyPwd")
    public R verifyPwd(@RequestParam String oldPassword,
                       @LoginUser UserEntity user){

        //检查次数是否超过10次
        Integer failCount = redisCacheUtils.get("VERIFY_PWD_FAIL:" + user.getUserId());
        if(failCount == null){
            failCount = 0;
        }else if(failCount >= 10){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.VERIFY_PWD,"密码错误次数过多",user.getUserId());
            return R.error("密码错误次数过多，请30分钟后再试");
        }
        try {
            userService.checkPwd(user,oldPassword);
        } catch (RRException e) {
            redisCacheUtils.set("VERIFY_PWD_FAIL:" + user.getUserId(),++failCount,1800);
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.VERIFY_PWD,"密码错误",user.getUserId());
            throw e;
        }

        return R.ok("密码正确");
    }

    /**
     * 修改密码
     * @param password
     * @param oldPassword
     * @param user
     * @return
     */
    @Login
    @PostMapping("changePassword")
    public R changePassword(@RequestParam(required = false,defaultValue = "PWD") String type,
                            @RequestParam String password,
                            @RequestParam String oldPassword,
                            @RequestParam(required = false) String smsCode,
                            @LoginUser UserEntity user){
        Assert.isBlank(password, "新密码不能为空");
        Assert.isBlank(oldPassword, "旧密码不能为空");
        Assert.minLen(password,6,"密码长度至少6位");

        if("PWD".equals(type)) {
            //检查密码是否正确
            userService.checkPwd(user, oldPassword);
        }else if("SMS".equals(type)){
            //检查验证码是否正确
            boolean validate = smsUtils.validate(user.getPhone(), smsCode, SmsUtils.SMS_TYPE.MODIFY_OR_FIND_PASS);
            if(!validate){
                appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.CHANGE_PASSWORD,"短信验证码错误",user.getUserId());
                return R.error("短信验证码错误");
            }
        }else{
            return R.error("错误的凭据类型");
        }

        userService.changePwd(user,password);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.CHANGE_PASSWORD,"密码修改成功",user.getUserId());

        //将上次登录toke作废
        jwtUtils.abandoned(user.getUserId());

        return R.ok("修改密码成功");
    }

    /**
     * 注销登录
     * @param userId
     * @param deviceId
     * @return
     */
    @Login
    @PostMapping("logout")
    public R logout(@RequestAttribute("userId") Long userId,
                    @RequestParam(required = false)String deviceId){
        //清除登录token
        jwtUtils.abandoned(userId);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.LOGOUT,"注销成功",userId);

        return R.ok("账号己退出");
    }

    /**
     * 获取用户信息
     * @param user
     */
    @Login
    @PostMapping("info")
    public R userInfo(@LoginUser UserEntity user){
        user.setPassword(StringUtils.isEmpty(user.getPassword())?"":"Y");
        user.setSalt(null);
        //user.setUserId(0L);
        user.setDeviceId(null);
        String phone = user.getPhone();
        if(phone!=null){
            user.setPhone(phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
        }
        return R.ok("请求个人信息成功").put("info", user);
    }

    /**
     * 绑定手机号码
     * @param phone
     * @param smsCode
     * @param password
     * @param user
     * @return
     */
    @Login
    @PostMapping("bindPhone")
    public R bindPhone(@RequestParam String phone,
                       @RequestParam String smsCode,
                       @RequestParam String password,
                       @LoginUser UserEntity user){
        Assert.isBlank(phone, "手机号不能为空");
        Assert.isBlank(smsCode, "短信验证码不能为空");
        Assert.isBlank(password, "密码不能为空");
        Assert.minLen(password,6,"密码长度至少6位");

        //检查是否已绑定手机号
        if(user.getPhone()!=null){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_PHONE,"手机号绑定失败，应调用修改绑定接口",user.getUserId());
            return R.error("绑定失败，请返回首页后再尝试");
        }

        //检查该手机号是否已绑定
        UserEntity userEntity = userService.queryByPhone(phone);
        if(userEntity != null){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_PHONE,"该手机号已其它用户被绑定：" + phone,user.getUserId());
            return R.error(MsgStatus.PHONE_ALREADY_BIND);
        }

        //检查验证码
        if(!smsUtils.validate(phone,smsCode, SmsUtils.SMS_TYPE.MODIFY_PHONE)){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_PHONE,"手机号绑定失败，验证码不正确",user.getUserId());
            return R.error(MsgStatus.INVALID_VERIFICATION_CODE);
        }

        //修改手机号
        userService.changePhone(user,phone);

        //修改密码
        userService.changePwd(user,password);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_PHONE,"绑定手机号成功",user.getUserId());

        return R.ok("绑定手机号成功");
    }

    /**
     * 修改绑定手机号码
     * @param type PWD/SMS
     * @param phone
     * @param smsCode
     * @param password
     * @param user
     * @return
     */
    @Login
    @PostMapping("changePhone")
    public R changePhone(@RequestParam(required = false,defaultValue = "PWD") String type,
                         @RequestParam String password,
                         @RequestParam(required = false) String oldPhoneSmsCode,
                         @RequestParam String phone,
                         @RequestParam String smsCode,
                         @LoginUser UserEntity user){
        Assert.isBlank(phone, "手机号不能为空");
        Assert.isBlank(smsCode, "短信验证码不能为空");
        Assert.isBlank(password, "密码不能为空");
        Assert.minLen(password,6,"密码长度至少6位");

        if("PWD".equals(type)){//依据密码修改绑定手机号
            //检查用户密码
            userService.checkPwd(user,password);
        }else if("SMS".equals(type)){//依据短信修改绑定手机号
            boolean validate = smsUtils.validate(user.getPhone(), oldPhoneSmsCode, SmsUtils.SMS_TYPE.MODIFY_PHONE);
            if(!validate){
                appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.MODIFY_PHONE,"原手机号验证码错误或失效",user.getUserId());
                return R.error("原手机号验证码错误或失效");
            }
        }else{
            return R.error("错误的凭据类型");
        }

        //检查新手机号验证码
        if(!smsUtils.validate(phone,smsCode, SmsUtils.SMS_TYPE.MODIFY_PHONE)){
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.MODIFY_PHONE,"验证码错误",user.getUserId());
            return R.error(MsgStatus.INVALID_VERIFICATION_CODE);
        }

        userService.changePhone(user,phone);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.MODIFY_PHONE,"修改绑定手机成功",user.getUserId());

        return R.ok("更新绑定手机成功");
    }

    /**
     * 用户可以自由修改的字段
     */
    private static final List<String> USER_CAN_MODIFY = Arrays.asList("username", "avatar","gender","birthday","alipayAccount","alipayName","weixinPayName","weixinPayPhone");

    /**
     * 修改用户资料
     * @param key
     * @param value
     * @param userId
     * @return
     */
    @Login
    @PostMapping("updateInfo")
    public R updateInfo(@RequestParam String key,
                        @RequestParam String value,
                        @RequestAttribute("userId") Long userId){
        Assert.isBlank(key, "key不可为空");
        Assert.isBlank(value, "value不可为空");

        //判断是否是可修改的字段
        if(!USER_CAN_MODIFY.contains(key)){
            appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.MODIFY_INFO,"修改资料失败，非法字段：" + key + "=" + value,userId);
            throw new RRException(MsgStatus.ILLEGAL_OPERATION);
        }

        UserEntity userEntity = new UserEntity();
        try {
            //根据字段名获得字段，并设置属性
            Field field = userEntity.getClass().getDeclaredField(key);
            field.setAccessible(true);
            if(field.getType() == Integer.class) {
                field.set(userEntity, Integer.parseInt(value));
            }else {
                field.set(userEntity, value);
            }
            field.setAccessible(false);
            userEntity.setUserId(userId);
            userService.update(userEntity);
        } catch (Exception e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.MODIFY_INFO,"修改资料异常：" + e.getMessage(),userId);
            throw new RRException(MsgStatus.INTERNAL_SERVER_ERROR,e);
        }

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.MODIFY_INFO,"修改资料成功",userId);

        return R.ok("修改成功");
    }

    /**
     * 支付宝登录
     * @param code
     * @param deviceId
     * @return
     */
    @PostMapping("alipayLogin")
    public R alipayLogin(@RequestParam String code,
                         @RequestParam(required = false)String deviceId){

        AlipayClient alipayClient = AlipayConfig.getAlipayClient();

        AlipaySystemOauthTokenRequest tokenReq = new AlipaySystemOauthTokenRequest();
        tokenReq.setGrantType("authorization_code");
        tokenReq.setCode(code);
        AlipaySystemOauthTokenResponse tokenRes;
        try {
            tokenRes = alipayClient.execute(tokenReq);
            if(!tokenRes.isSuccess()){
                appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.ALIPAY_LOGIN,"支付宝登录失败：" + new Gson().toJson(tokenRes));
                throw new RRException("支付宝登录失败，请稍后再试");
            }
        } catch (AlipayApiException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.ALIPAY_LOGIN,"支付宝登录异常：" + e.getMessage());
            throw new RRException("支付宝登录失败，请稍后再试",e);
        }

        UserEntity userEntity = userService.queryByAlipay(tokenRes.getUserId());
        if(userEntity == null){
            AlipayUserInfoShareRequest userInfoReq = new AlipayUserInfoShareRequest();

            AlipayUserInfoShareResponse userInfoRes = null;
            try {
                userInfoRes = alipayClient.execute(userInfoReq,tokenRes.getAccessToken());
                if(!userInfoRes.isSuccess()){
                    appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.ALIPAY_LOGIN,"获取支付宝用户信息失败：" + new Gson().toJson(userInfoRes));
                    throw new RRException("获取用户信息失败，请稍后再试");
                }
            } catch (AlipayApiException e) {
                appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.ALIPAY_LOGIN,"支付宝登录异常：" + e.getMessage());
                throw new RRException("支付宝登录失败，请稍后再试");
            }

            //注册
            userEntity = new UserEntity();
            userEntity.setAvatar(userInfoRes.getAvatar());
            userEntity.setUsername(userInfoRes.getNickName());
            userEntity.setAlipayBindOpenid(userInfoRes.getUserId());
            userEntity.setGender(Objects.equals(userInfoRes.getGender(), "M")?1:2);
            userEntity.setDeviceId(deviceId);
            userService.save(userEntity);
        }

        userService.checkStatus(userEntity);//检查用户状态

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.ALIPAY_LOGIN,"支付宝登录成功");

        //生成token
        Map<String, Object> map = jwtUtils.makeToMap(jwtUtils.generateToken(userEntity.getUserId()));
        return R.ok("支付宝登录成功").put("data",map);
    }

    /**
     * 微信登录接口
     * @param code
     * @param deviceId
     * @return
     */
    @PostMapping("wxLogin")
    public R wxLogin(@RequestParam String code,
                     @RequestParam(required = false)String deviceId){
        Assert.isBlank(code,"code不可为空");

        //通过code兑换openid
        JSONObject jsonObject = null;
        try {
            jsonObject = weChatUtils.code2openid(code);
        } catch (WeChatUtils.WeChatApiException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.WECHAT_LOGIN,e.getMessage());
            throw new RRException("登录失败，请稍后重试",e);
        }
        String openid = jsonObject.getString("openid");

        //根据openid获取用户
        UserEntity userEntity = userService.queryByWeChat(openid);

        //如果用户不存在
        if(userEntity == null){
            //注册用户
            //获取微信用户信息
            JSONObject userInfo = null;
            try {
                userInfo = weChatUtils.getUserInfoByOpenid(openid, jsonObject.getString("access_token"));
            } catch (WeChatUtils.WeChatApiException e) {
                appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.WECHAT_LOGIN,e.getMessage());
                throw new RRException("获取用户信息失败，请稍后重试",e);
            }

            userEntity=new UserEntity();
            userEntity.setWeixinBindOpenid(userInfo.getString("openid"));
            userEntity.setUsername(userInfo.getString("nickname"));
            userEntity.setGender(userInfo.getInt("sex"));
            userEntity.setAvatar(userInfo.getString("headimgurl"));
            userEntity.setDeviceId(deviceId);
            userService.save(userEntity);

            appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.WECHAT_REGISTER,"微信注册成功",userEntity.getUserId());
        }

        //检查用户状态
        userService.checkStatus(userEntity);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.WECHAT_LOGIN,"微信登录成功",userEntity.getUserId());

        //生成token
        Map<String, Object> map = jwtUtils.makeToMap(jwtUtils.generateToken(userEntity.getUserId()));
        return R.ok("微信登录成功").put("data",map);
    }

    /**
     * 微信绑定
     * @param code
     * @return
     */
    @PostMapping("wxBinding")
    public R wxBinding(@RequestParam String code,
                       @RequestAttribute("userId") Long userId) {
        Assert.isBlank(code,"code不可为空");

        //通过code兑换openid
        JSONObject jsonObject = null;
        try {
            jsonObject = weChatUtils.code2openid(code);
        } catch (WeChatUtils.WeChatApiException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_WECHAT,e.getMessage());
            throw new RRException("绑定微信失败，请稍后重试",e);
        }

        String openid = jsonObject.getString("openid");

        //根据openid获取用户
        UserEntity userEntity = userService.queryByWeChat(openid);

        //如果用户存在
        if(userEntity != null){
            //说明已经绑定过了
            appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_WECHAT,"微信绑定失败",userId);
            return R.error(MsgStatus.BIND_WECHAT_ERROR).put("bind",1);//返回绑定错误code码
        }

        //没有用户信息表示没有被绑定过   给当前的用户信息里加上openid
        userEntity=new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setWeixinBindOpenid(openid);
        userService.update(userEntity);
        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_WECHAT,"微信绑定成功",userId);
        return R.ok("绑定成功").put("bind",1);
    }

    /**
     * 微信钱包绑定
     * @param code
     * @return
     */
    @Login
    @PostMapping("weixinPayBind")
    public R weixinPayBind(@RequestParam String code,
                           @RequestAttribute("userId") Long userId){
        Assert.isBlank(code,"code不可为空");

        //通过code兑换openid
        JSONObject jsonObject = null;
        try {
            jsonObject = weChatUtils.code2openid(code);
        } catch (WeChatUtils.WeChatApiException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_WEIXIN_PAY,e.getMessage());
            throw new RRException("绑定微信钱包失败，请稍后重试",e);
        }
        String openid = jsonObject.getString("openid");

        //获取微信用户信息
        JSONObject userInfo = null;
        try {
            userInfo = weChatUtils.getUserInfoByOpenid(openid, jsonObject.getString("access_token"));
        } catch (WeChatUtils.WeChatApiException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_WEIXIN_PAY,e.getMessage());
            throw new RRException("获取用户信息失败，请稍后重试",e);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setWeixinPayOpenid(openid);
        userEntity.setWeixinPayNickname(userInfo.getString("nickname"));
        userEntity.setWeixinPayAvatar(userInfo.getString("headimgurl"));
        userService.update(userEntity);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_WEIXIN_PAY,"绑定微信钱包成功",userId);

        return R.ok("绑定微信钱包成功").put("nickname",userEntity.getWeixinPayNickname()).put("headimgurl",userEntity.getWeixinPayAvatar()).put("bind",1);
    }

    /**
     * 支付宝绑定
     * @param code
     * @return
     */
    @PostMapping("alipayBinding")
    public R alipayBinding(@RequestParam String code,@RequestAttribute("userId") Long userId) {
        Assert.isBlank(code,"code不可为空");

        AlipayClient alipayClient = AlipayConfig.getAlipayClient();

        AlipaySystemOauthTokenRequest tokenReq = new AlipaySystemOauthTokenRequest();
        tokenReq.setGrantType("authorization_code");
        tokenReq.setCode(code);
        AlipaySystemOauthTokenResponse tokenRes;

        try {
            tokenRes = alipayClient.execute(tokenReq);
            if(!tokenRes.isSuccess()){
                appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_ALIPAY,"支付宝绑定失败：" + new Gson().toJson(tokenRes));
                throw new RRException("支付宝绑定失败，请稍后再试");
            }
        } catch (AlipayApiException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_ALIPAY,"支付宝绑定异常：" + e.getMessage());
            throw new RRException("支付宝绑定失败，请稍后再试",e);
        }

        //查找该支付宝有没有绑定过
        UserEntity userEntity = userService.queryByAlipay(tokenRes.getUserId());
        if(userEntity != null){//绑定过
            return R.error(MsgStatus.BIND_ALIPAY_ERROR).put("bind",1);
        }

        //添加绑定值
        userEntity = new UserEntity();
        userEntity.setAlipayBindOpenid(tokenRes.getUserId());
        userEntity.setUserId(userId);
        userService.update(userEntity);

        appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.BIND_ALIPAY,"支付宝绑定成功");

        return R.ok("绑定成功").put("bind",1);
    }


}
