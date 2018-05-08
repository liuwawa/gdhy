package io.renren.modules.api.controller;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import com.dingtalk.chatbot.DingtalkChatbotClient;
import com.dingtalk.chatbot.message.MarkdownMessage;
import com.google.gson.Gson;
import io.renren.common.utils.*;
import io.renren.common.validator.Assert;
import io.renren.modules.admin.service.DataDictionariesService;
import io.renren.modules.admin.service.TaskRecordService;
import io.renren.modules.api.annotation.Login;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.service.UserService;
import io.renren.modules.api.utils.AppLogUtils;
import io.renren.modules.api.utils.SmsUtils;
import io.renren.modules.invite.entity.InviteCodeEntity;
import io.renren.modules.invite.entity.InviteRecordEntity;
import io.renren.modules.invite.entity.InviteRewardRecordEntity;
import io.renren.modules.invite.entity.InviteRewardRuleEntity;
import io.renren.modules.invite.service.InviteCodeService;
import io.renren.modules.invite.service.InviteRecordService;
import io.renren.modules.invite.service.InviteRewardRecordService;
import io.renren.modules.invite.service.InviteRewardRuleService;
import io.renren.modules.invite.utils.InviteRewardRuleUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ITMX on 2018/3/18.
 */
@RestController
@RequestMapping("/api/invite")
public class ApiInviteController {

    @Autowired
    private UserService userService;
    @Autowired
    private InviteCodeService inviteCodeService;
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private AppLogUtils appLogUtils;
    @Autowired
    private InviteRewardRuleService inviteRewardRuleService;
    @Autowired
    private DataDictionariesService dataDictionariesService;
    @Autowired
    private InviteRecordService inviteRecordService;
    @Autowired
    private InviteRewardRecordService inviteRewardRecordService;
    @Autowired
    private TaskRecordService taskRecordService;
    @Autowired
    private RedisCacheUtils redisCacheUtils;
    @Autowired
    private IAcsClient client;

    @Value("${private.iacs.appKey}")
    public String APP_KEY;

    @Autowired
    private DingtalkChatbotClient dingtalkChatbotClient;
    @Value("${private.chatbot-webhook}")
    private String chatbotWebhook;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取我的邀请码
     * @param userId
     * @return
     */
    @PostMapping("/getInviteCode")
    public R getInviteCode(@RequestAttribute Long userId){

        InviteCodeEntity inviteCodeEntity = inviteCodeService.getInviteCode(userId);
        if(inviteCodeEntity.getState() == -1) {
            return R.error("您暂时无法获得邀请码");
        }

        return R.ok().put("inviteCode",inviteCodeEntity.getInviteCode());
    }

    /**
     * 邀请注册
     * @param phone
     * @param smsCode
     * @param inviteCode
     * @return
     */
    @PostMapping("/register")
    public R register(
            @RequestParam String phone,
            @RequestParam String smsCode,
            @RequestParam String inviteCode,
            HttpServletRequest request){
        Assert.minLen(phone,11,"请正确填写手机号");
        Assert.isBlank(smsCode, "短信验证码不能为空");

        if(!smsUtils.validate(phone,smsCode, SmsUtils.SMS_TYPE.USER_REG)){
            appLogUtils.w(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.INVITE_REGISTER,"邀请注册失败，验证码错误",null,"WEB");
            return R.error(MsgStatus.INVALID_VERIFICATION_CODE);
        }

        String trueIp = request.getHeader("trueIp");
        if(trueIp == null){
            return R.error("接口错误");
        }
        //验证码正确，将该IP从白名单移除
        redisCacheUtils.delete("IP_LIMIT_" + trueIp);

        InviteCodeEntity inviteCodeEntity = null;
        if(!StringUtils.isBlank(inviteCode) && StringUtils.isNumeric(inviteCode)) {
            //检查邀请码
            inviteCodeEntity = inviteCodeService.queryObject(Integer.valueOf(inviteCode));
            if (inviteCodeEntity == null || inviteCodeEntity.getState() != 0) {
                appLogUtils.w(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.INVITE_REGISTER, "邀请码无效", null, "WEB");
                inviteCode = "";
                inviteCodeEntity = null;
            }
        }else{
            inviteCode = "";
        }

        //检查手机号是否已注册
        UserEntity userEntity = userService.queryByPhone(phone);
        if(userEntity != null){
            appLogUtils.w(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.INVITE_REGISTER,"邀请注册失败，该手机号已注册",null,"WEB");
            return R.error(MsgStatus.ALREADY_REGISTERED);
        }

        userEntity = new UserEntity();
        userEntity.setPhone(phone);
        userEntity.setUsername("用户" + phone.substring(phone.length() - 4));
        userEntity.setInviteCode(inviteCode);
        userEntity.setDeviceId("");

        //生成随机密码
        userEntity.setPassword(String.valueOf((int) ((Math.random() * 9 + 1) * 1000000)).substring(1));
        logger.info(userEntity.getUsername() + "初始登录密码：" + userEntity.getPassword());

        //短信发送密码到用户
        String inviteRegSmsHeader = dataDictionariesService.queryByKey("邀请注册成功短信前缀", "1.0.0");
        String inviteRegSmsFooter = dataDictionariesService.queryByKey("邀请注册成功短信后缀", "1.0.0");
        smsUtils.inviteRegSuccess(phone,inviteRegSmsHeader,userEntity.getPassword() + inviteRegSmsFooter);

        userEntity.setPassword(SignUtils.getMD5(userEntity.getPassword()));
        userService.save(userEntity);

        appLogUtils.i(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.INVITE_REGISTER,"邀请注册成功，邀请码为：" + inviteCode,userEntity.getUserId());

        if(inviteCodeEntity != null) {
            //获取当前奖励规则
            InviteRewardRuleEntity currentRule = inviteRewardRuleService.getCurrentRule();

            //创建一条邀请记录
            InviteRecordEntity inviteRecordEntity = new InviteRecordEntity();
            inviteRecordEntity.setInviteCode(Integer.valueOf(inviteCode));
            inviteRecordEntity.setMaster(inviteCodeEntity.getUserId());
            inviteRecordEntity.setApprentice(userEntity.getUserId());
            inviteRecordEntity.setGave(0);
            inviteRecordEntity.setRewardRule(currentRule.getId());
            inviteRecordEntity.setReadProfit(0);
            inviteRecordEntity.setProfit(0);
            inviteRecordEntity.setTitle("徒弟 " + phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
            inviteRecordEntity.setLastRewardDate(null);
            //设置成为0，暂时不发放，等到首次登陆的时候才发放3500金币
            inviteRecordEntity.setNextReward(0);
            inviteRecordEntity.setWaitGive(InviteRewardRuleUtils.getTotal(currentRule));
            inviteRecordService.save(inviteRecordEntity);
        }
        return R.ok("注册成功");
    }

    /**
     * 发送短信验证码接口，必须经过图灵验证
     * @param phone
     * @param request
     * @return
     */
    @PostMapping("/sendSmsCode")
    public R sendSmsCode(@RequestParam String phone,HttpServletRequest request){
        Assert.minLen(phone,11,"请正确填写手机号");

        String trueIp = request.getHeader("trueIp");
        if(trueIp == null){
            return R.error("接口错误");
        }

        String validate = request.getHeader("validate");
        if("true".equals(validate)){
            AuthenticateSigRequest sigRequest = new AuthenticateSigRequest();
            sigRequest.setSessionId(request.getParameter("csessionid"));
            sigRequest.setSig(request.getParameter("sig"));
            sigRequest.setToken(request.getParameter("token"));
            sigRequest.setScene(request.getParameter("scene"));
            sigRequest.setAppKey(APP_KEY);
            sigRequest.setRemoteIp(trueIp);
            try {
                AuthenticateSigResponse response = client.getAcsResponse(sigRequest);
                if(response.getCode() != 100) {
                    logger.warn("滑动验证失败");
                }
            } catch (Exception e) {
                logger.warn("滑动验证异常",e);
                return R.error();
            }
        }

        //检查该IP是否存在狂刷情况
        Integer sendCount = redisCacheUtils.get("IP_LIMIT_" + trueIp);
        if(sendCount == null){
            sendCount = 0;
        }
        if(sendCount >= 15){
            try {
                //推送到钉钉群
                MarkdownMessage message = new MarkdownMessage();
                message.setTitle("邀请注册异常");
                message.add(MarkdownMessage.getHeaderText(3, "邀请注册验证码接口存在狂刷嫌疑："));
                message.add(MarkdownMessage.getReferenceText("IP：" + trueIp + "，请开启滑动验证方案"));

                message.add("* * *");//换行
                message.add("-  URL：" + request.getRequestURL().toString());
                message.add("-  IP：" + IPUtils.getIpAddr(request));
                message.add("-  位置：" + IPUtils.ipToAddressByTaoBao(trueIp));
                message.add("-  时间：" + DateUtils.format(new Date()));
                message.add("-  数据：" + new Gson().toJson(request.getParameterMap()));

                dingtalkChatbotClient.send(chatbotWebhook,message);
            } catch (Exception e1) {
                logger.warn("数据重复异常推送到钉钉群",e1);
            }
            return R.error("今日邀请已达上限");
        }

        //检查手机号是否已注册
        UserEntity userEntity = userService.queryByPhone(phone);
        if(userEntity != null){
            appLogUtils.w(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.SEND_SMS,"该手机号已注册",null,"WEB");
            return R.error(MsgStatus.ALREADY_REGISTERED);
        }

        int sendResult = smsUtils.userReg(phone);

        if(sendResult == 1) {
            appLogUtils.i(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.SEND_SMS,"短信发送成功：" + phone,null,"WEB");

            //累加该IP的短信发送次数
            redisCacheUtils.set("IP_LIMIT_" + trueIp,++sendCount,3600);

            return R.ok("发送成功");
        }else if(sendResult == -2){
            appLogUtils.w(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.SEND_SMS,"短信发送频率过高：" + phone,null,"WEB");
            return R.error("短信发送频率过高，请稍后再试");
        }else if(sendResult == 0){
            appLogUtils.w(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.SEND_SMS,"短信发送失败：" + phone,null,"WEB");
            return R.error("发送失败，请检查手机号码");
        }else {
            appLogUtils.e(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.SEND_SMS,"短信发送异常：" + phone,null,"WEB");
            return R.error("发送异常");
        }
    }

    /**
     * 获取收徒页必需数据
     * @return
     */
    @Login
    @PostMapping("/pageData")
    public R pageData(@RequestParam(required = false,defaultValue = "1.0.0") String version,
                      @RequestAttribute Long userId){
        HashMap<String, Object> map = new HashMap<>();

        InviteRewardRuleEntity currentRule = inviteRewardRuleService.getCurrentRule();
        map.put("rewardRule",currentRule);

        //收一个徒弟可以得到的总奖励额
        Integer total = InviteRewardRuleUtils.getTotal(currentRule);

        String bannerImg = dataDictionariesService.queryByKey("收徒页横幅", version);
        map.put("bannerImg",bannerImg);

        String inviteProfitText = dataDictionariesService.queryByKey("邀请收益", version);
        inviteProfitText = String.format(inviteProfitText, total);
        map.put("inviteProfitText",inviteProfitText);

        String addedProfitText = dataDictionariesService.queryByKey("提成收益", version);
        addedProfitText = String.format(addedProfitText,100);
        map.put("addedProfitText",addedProfitText);

        String rewardRuleText = dataDictionariesService.queryByKey("奖励发放规则描述", version);
        rewardRuleText = String.format(rewardRuleText,total,currentRule.getFirstReward(),total - currentRule.getFirstReward(),currentRule.getThreshold());
        map.put("rewardRuleText",rewardRuleText);

        //成功邀请数
        HashMap<String, Object> param = new HashMap<>();
        param.put("master",userId);
        int count = inviteRecordService.queryTotal(param);
        map.put("inviteCount",count);

        //徒弟贡献总金币
        Integer totalProfit = 0;
        if(count > 0) {
            totalProfit = inviteRecordService.totalProfit(userId);
        }
        map.put("totalIncome",totalProfit);

        return R.ok(map);
    }

    /**
     * 我的收徒邀请列表
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @Login
    @PostMapping("/apprentices")
    public R apprentices(@RequestParam Integer page,
                         @RequestParam Integer limit,
                         @RequestAttribute Long userId){

        List<InviteRecordEntity> list = inviteRecordService.pageByMaster(userId, page, limit,null);

        return R.ok().put("list",list);
    }

    /**
     * 徒弟提供的收益列表
     * @param userId
     * @return
     */
    @Login
    @PostMapping("/profitList")
    public R profitList(@RequestParam Integer page,
                        @RequestParam Integer limit,
                        @RequestAttribute Long userId){

        List<InviteRewardRecordEntity> list = inviteRewardRecordService.pageByMaster(userId, page, limit);

        return R.ok().put("list",list);
    }

    @Value("${private.img-gen-salt}")
    private String imgGenSalt;
    /**
     * 获取收徒分享数据
     * @param version
     * @param userId
     * @return
     */
    @Login
    @PostMapping("/share")
    public R share(@RequestParam(required = false,defaultValue = "1.0.0") String version,
                   @RequestAttribute Long userId) throws UnsupportedEncodingException {

        InviteCodeEntity inviteCodeEntity = inviteCodeService.getInviteCode(userId);
        if(inviteCodeEntity.getState() == -1) {
            return R.error("您暂时无法获得邀请码");
        }

        //分享地址
        String inviteURL = dataDictionariesService.queryByKey("邀请分享链接", version);
        if(StringUtils.isBlank(inviteURL)){
            return R.error("暂未开放分享功能");
        }

        //分享内容
        String inviteContent = dataDictionariesService.queryByKey("邀请分享内容", version);
        inviteContent = inviteContent == null ? "邀请你使用大众看点。": inviteContent;

        //分享图片
        String inviteImages = dataDictionariesService.queryByKey("邀请分享图片", version);
        inviteImages = inviteImages == null ? "": inviteImages;

        HashMap<String,Object> map = new HashMap<>();
        map.put("inviteCode",inviteCodeEntity.getInviteCode());
        map.put("inviteContent",inviteContent);
        if(inviteURL.contains(";")){
            //将URL分割
            String[] urls = inviteURL.split(";");
            //随机从url中取出一个链接地址
            inviteURL  = urls[(int) (Math.random()*(urls.length))];
        }
        inviteURL = inviteURL + inviteCodeEntity.getInviteCode();
        map.put("inviteURL",inviteURL);

        String[] inviteImagesArr;
        if(inviteImages.contains(";")) {
            //将图片分割
            String[] imgs = inviteImages.split(";");
            inviteImages = imgs[(int) (Math.random()*(imgs.length))];
            inviteImagesArr = new String[]{inviteImages};
        }else if(StringUtils.isBlank(inviteImages)){
            inviteImagesArr = new String[]{};
        }else{
            inviteImagesArr = new String[]{inviteImages};
        }

        //使用自动生成二维码的链接
        //http://invite-img.dzkandian.com/gen.php?img=https://watch-everyday.oss-cn-shenzhen.aliyuncs.com/custom/20180416/ecfb3eb955514ebbbc47145e8f63540d.png&pos=260,1000&link=https://m.apk0.cn/invitation/index.html?inviteCode=1000013
        for (int i = 0; i < inviteImagesArr.length; i++) {
            //imgUrl: http://invite-img.dzkandian.com/gen.php?img=https%3a%2f%2fwatch-everyday.oss-cn-shenzhen.aliyuncs.com%2fcustom%2f20180416%2fecfb3eb955514ebbbc47145e8f63540d.png&pos=260%2c1000
            if(inviteImagesArr[i].contains("?")) {
                //添加link参数并添加签名
                inviteImagesArr[i] = inviteImagesArr[i] + "&link=" + URLEncoder.encode(inviteURL, "UTF-8");
                inviteImagesArr[i] = inviteImagesArr[i] + "&sign=" + SignUtils.getMD5(inviteImagesArr[i] + imgGenSalt);
            }
        }
        map.put("inviteImages", inviteImagesArr);

        return R.ok(map);
    }
}
