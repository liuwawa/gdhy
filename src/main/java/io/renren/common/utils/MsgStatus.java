package io.renren.common.utils;

public enum MsgStatus {

    OK(200, "操作成功"),
    INVALID_AUTHORIZED(401, "未经授权或授权失效"),
    INVALID_SIGNATURE(412, "操作成功"),
    INTERNAL_SERVER_ERROR(500, "系统繁忙"),
    NOT_SUPPORT_SMS_TYPE(501,"暂不支持的短信类型"),
    INVALID_VERIFICATION_CODE(502, "验证码有误或已过期"),
    INVALID_PARAMETER(503,"参数不正确"),
    ALREADY_REGISTERED(504,"该手机号已注册过，请直接登录"),
    BAD_PHONE_OR_PASSWORD(505,"手机号或密码错误"),
    USER_DISABLE(506, "账户已被禁止登录"),
    LOST_TIMESTAMP(507,"缺少用户ID"),
    INVALID_TIMESTAMP(508,"请调整系统时间"),
    EMPTY_FILE(509, "上传文件不能为空"),
    USER_NOT_REGISTER(510,"该手机号未注册"),
    EXCESSIVE_NUMBER_OF_ERRORS(511,"验证码错误次数过多，本次验证码已失效"),
    PHONE_IDENTICAL(512,"新手机号与原绑定号码一致"),
    RECEIVE_FAILURE(513,"金币领取失败"),
    ILLEGAL_OPERATION(514,"非法操作"),
    INVALID_METHOD(515, "无效的请求"),
    UNAUTHORIZED(516,"没有权限，请联系管理员授权"),
    DATA_DUPLICATION(517, "数据重复"),
    PHONE_ALREADY_BIND(518, "该手机号已其它用户被绑定，请先解绑"),
    BIND_WECHAT_ERROR(519, "该微信已绑定其他账号"),
    BIND_ALIPAY_ERROR(520, "该支付宝已绑定其他账号"),
    WITHDRAWALS_EXCEPTION(521,"提现异常"),
    BAD_PASSWORD(522,"密码错误");

    private final int value;
    private final String reasonPhrase;

    MsgStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String toString() {
        return Integer.toString(this.value);
    }

    public static MsgStatus valueOf(int statusCode) {
        MsgStatus[] var1 = values();

        for (MsgStatus status : var1) {
            if (status.value == statusCode) {
                return status;
            }
        }

        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }

}