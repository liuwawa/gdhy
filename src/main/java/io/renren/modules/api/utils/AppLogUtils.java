package io.renren.modules.api.utils;

import com.google.gson.Gson;
import io.renren.common.utils.HttpContextUtils;
import io.renren.common.utils.IPUtils;
import io.renren.modules.admin.entity.AppLogEntity;
import io.renren.modules.admin.service.AppLogService;
import io.renren.modules.api.interceptor.AuthorizationInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ITMX on 2017/12/19.
 */
@Component
public class AppLogUtils {
    @Autowired
    private AppLogService appLogService;
    private static Logger logger = LoggerFactory.getLogger(AppLogUtils.class);
    private Gson gson = new Gson();

    @Value("${private.instanceId}")
    private String instanceId;
    @Value("${spring.application.name}")
    private String appId;

    public enum LOG_TYPE {
        USER(1,"用户模块"), UTILS(2,"工具模块"),GOLD(3,"金币模块"), INVITE(4, "收徒邀请");

        private final int value;
        private final String name;

        LOG_TYPE(int value,String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return Integer.toString(this.value);
        }

        public static LOG_TYPE valueOf(int value) {
            LOG_TYPE[] types = values();

            for (LOG_TYPE type : types) {
                if (type.value == value) {
                    return type;
                }
            }

            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
    }

    public enum OPERATION{
        PWD_LOGIN(1,"密码登录"),
        PASSWD_REGISTER(2,"密码注册"),
        SMS_LOGIN(3,"短信登录"),
        FORGET_PASSWORD(4,"找回密码"),
        CHANGE_PASSWORD(5,"修改密码"),
        BIND_PHONE(6,"绑定手机"),
        MODIFY_PHONE(7,"更换绑定手机"),
        MODIFY_INFO(8,"修改资料"),
        ALIPAY_LOGIN(9,"支付宝登录"),
        WECHAT_LOGIN(10,"微信登录"),
        LOGOUT(11,"登出"),
        WECHAT_REGISTER(12,"微信注册"),
        UPLOAD_FILE(13,"上传文件"),
        SEND_SMS(14,"发送短信"),
        AUTO_ENABLE(15,"自动解禁" ),
        DISABLE(16, "禁止登录"),
        GOLD_READ(17,"阅读奖励"),
        GOLD_WATCH(18, "看视频奖励"),
        GOLD_DURATION(19, "时段奖励"),
        TASK_DAILY(20, "日常任务"),
        TASK_NOVICE(21, "新手任务"),
        TASK_SIGN(22, "每日签到"),
        BIND_WECHAT(23,"绑定微信"),
        BIND_ALIPAY(24,"绑定支付宝"),
        TASK_FINISH(25, "完成任务"),
        GOLD_WITHDRAWALS(26, "金币提现"),
        BIND_WEIXIN_PAY(27, "绑定微信钱包"),
        PHONE_REGISTER(28, "短信登录注册"),
        VERIFY_SMS(29, "验证短信验证码"),
        VERIFY_PWD(30, "验证密码"),
        INVITE_REGISTER(31, "邀请注册"),
        INVITE_REWARD(32, "邀请奖励");

        private int value;
        private String name;

        OPERATION(int value,String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return Integer.toString(this.value);
        }

        public static OPERATION valueOf(int value) {
            OPERATION[] operations = values();

            for (OPERATION operation : operations) {
                if (operation.value == value) {
                    return operation;
                }
            }

            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
    }

    private static final Integer INFO = 10;
    private static final Integer WARN = 20;
    private static final Integer ERROR = 30;

    /**
     * 记录
     * @param level 日志级别
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     * @param userId 用户id
     * @param remarks 备注，如未携带设备id等
     */
    private AppLogEntity record(Integer level,LOG_TYPE type, OPERATION operation, String desc, Long userId, String remarks){
        AppLogEntity appLogEntity = new AppLogEntity();
        appLogEntity.setLevel(level);
        appLogEntity.setType(type.getValue());
        appLogEntity.setUser(userId);
        appLogEntity.setAppId(appId);
        appLogEntity.setOperation(operation.getValue());
        appLogEntity.setDescribe(desc);
        appLogEntity.setRemarks(remarks);
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        appLogEntity.setIp(IPUtils.getIpAddr(request));
        if(userId == null) {
            Object uid = request.getAttribute(AuthorizationInterceptor.USER_KEY);
            if (uid != null) {
                appLogEntity.setUser((Long) uid);
            }
        }
        //FIXME 请使用真实的实例ID
        if("".equals(instanceId)) {
            appLogEntity.setInstanceId(request.getLocalName());
        }
        //TODO 阅读文章、观看视频金币领取暂时不记录
        if(operation == OPERATION.GOLD_READ || operation == OPERATION.GOLD_WATCH){
            return appLogEntity;
        }
        appLogService.save(appLogEntity);
        return appLogEntity;
    }

    /**
     * 记录信息
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     * @param userId 用户id
     * @param remarks 备注，如未携带设备id等
     */
    public void i(LOG_TYPE type, OPERATION operation, String desc, Long userId, String remarks){
        AppLogEntity appLogEntity = record(INFO, type, operation, desc, userId, remarks);
        logger.info(gson.toJson(appLogEntity));
    }

    /**
     * 记录信息
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     * @param userId 用户id
     */
    public void i(LOG_TYPE type, OPERATION operation, String desc,Long userId) {
        i(type,operation,desc,userId,null);
    }

    /**
     * 记录信息
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     */
    public void i(LOG_TYPE type, OPERATION operation, String desc) {
        i(type,operation,desc,null,null);
    }

    /**
     * 记录警告
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     * @param userId 用户id
     * @param remarks 备注，如未携带设备id等
     */
    public void w(LOG_TYPE type, OPERATION operation, String desc, Long userId, String remarks){
        AppLogEntity appLogEntity = record(WARN, type, operation, desc, userId, remarks);
        logger.info(gson.toJson(appLogEntity));
    }

    /**
     * 记录警告
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     * @param userId 用户id
     */
    public void w(LOG_TYPE type, OPERATION operation, String desc,Long userId) {
        w(type,operation,desc,userId,null);
    }

    /**
     * 记录警告
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     */
    public void w(LOG_TYPE type, OPERATION operation, String desc) {
        w(type,operation,desc,null,null);
    }

    /**
     * 记录异常
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     * @param userId 用户id
     * @param remarks 备注，如未携带设备id等
     */
    public void e(LOG_TYPE type, OPERATION operation, String desc, Long userId, String remarks){
        AppLogEntity appLogEntity = record(ERROR, type, operation, desc, userId, remarks);
        logger.info(gson.toJson(appLogEntity));
    }

    /**
     * 记录异常
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     * @param userId 用户id
     */
    public void e(LOG_TYPE type, OPERATION operation, String desc,Long userId) {
        e(type,operation,desc,userId,null);
    }

    /**
     * 记录异常
     * @param type 日志类型，如用户日志
     * @param operation 操作，如登录
     * @param desc 描述，如微信登录成功
     */
    public void e(LOG_TYPE type, OPERATION operation, String desc) {
        e(type,operation,desc,null,null);
    }

}
