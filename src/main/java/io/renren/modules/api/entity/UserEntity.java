package io.renren.modules.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * Created by ITMX on 2017/12/7.
 */
public class UserEntity implements Serializable {
	//用户ID
	private Long userId;
	//用户名
	private String username;
	//手机号
	private String phone;
	//密码
	private String password;
	//盐
	private String salt;
	//邀请码
	private String inviteCode;
	//头像
	private String avatar;
	//性别 1为男，2为女，0为未知
	private Integer gender;
	//生日
	private String birthday;
	//账号状态
	private UserStatus status;
	//支付宝账户
	private String alipayAccount;
	//支付宝提现姓名
	private String alipayName;
	//微信提现姓名
	private String weixinPayName;
	//微信提现手机
	private String weixinPayPhone;
	//微信提现授权
	private String weixinPayOpenid;
	//微信提现授权昵称
	private String weixinPayNickname;
	//微信提现头像
	private String weixinPayAvatar;
	//创建时间
	private Date createTime;
	//资料最后更新时间
	private Date modifyTime;
	//账户是否被禁用
	private Integer disableFlag;
	//账户禁用提示
	private String disableMsg;
	//账户禁用时间
	private Date disableTime;
	//是否自动解禁
	private Integer autoEnable;
	//自动解禁时间
	private Date autoEnableTime;
	//微信绑定
	private String weixinBindOpenid;
	//支付宝绑定
	private String alipayBindOpenid;
	//用户注册时的设备ID
	private String deviceId;
	//是否完成新手任务
	private Integer noviceTask;
	//完成新手任务时间
	private Date finishNoviceTime;
	//伪删除
	private Integer delFlag;

	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：手机号
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：手机号
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSalt() {
		return salt;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getAlipayName() {
		return alipayName;
	}

	public void setAlipayName(String alipayName) {
		this.alipayName = alipayName;
	}

	public String getWeixinPayName() {
		return weixinPayName;
	}

	public void setWeixinPayName(String weixinPayName) {
		this.weixinPayName = weixinPayName;
	}

	public String getWeixinPayPhone() {
		return weixinPayPhone;
	}

	public void setWeixinPayPhone(String weixinPayPhone) {
		this.weixinPayPhone = weixinPayPhone;
	}

	public String getWeixinPayOpenid() {
		return weixinPayOpenid;
	}

	public void setWeixinPayOpenid(String weixinPayOpenid) {
		this.weixinPayOpenid = weixinPayOpenid;
	}

	public String getWeixinPayNickname() {
		return weixinPayNickname;
	}

	public void setWeixinPayNickname(String weixinPayNickname) {
		this.weixinPayNickname = weixinPayNickname;
	}

	public String getWeixinPayAvatar() {
		return weixinPayAvatar;
	}

	public void setWeixinPayAvatar(String weixinPayAvatar) {
		this.weixinPayAvatar = weixinPayAvatar;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getDisableFlag() {
		return disableFlag;
	}

	public void setDisableFlag(Integer disableFlag) {
		this.disableFlag = disableFlag;
	}

	public String getDisableMsg() {
		return disableMsg;
	}

	public void setDisableMsg(String disableMsg) {
		this.disableMsg = disableMsg;
	}

	public Date getDisableTime() {
		return disableTime;
	}

	public void setDisableTime(Date disableTime) {
		this.disableTime = disableTime;
	}

	public Integer getAutoEnable() {
		return autoEnable;
	}

	public void setAutoEnable(Integer autoEnable) {
		this.autoEnable = autoEnable;
	}

	public Date getAutoEnableTime() {
		return autoEnableTime;
	}

	public void setAutoEnableTime(Date autoEnableTime) {
		this.autoEnableTime = autoEnableTime;
	}

	public String getWeixinBindOpenid() {
		return weixinBindOpenid;
	}

	public void setWeixinBindOpenid(String weixinBindOpenid) {
		this.weixinBindOpenid = weixinBindOpenid;
	}

	public String getAlipayBindOpenid() {
		return alipayBindOpenid;
	}

	public void setAlipayBindOpenid(String alipayBindOpenid) {
		this.alipayBindOpenid = alipayBindOpenid;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getNoviceTask() {
		return noviceTask;
	}

	public void setNoviceTask(Integer noviceTask) {
		this.noviceTask = noviceTask;
	}

	public Date getFinishNoviceTime() {
		return finishNoviceTime;
	}

	public void setFinishNoviceTime(Date finishNoviceTime) {
		this.finishNoviceTime = finishNoviceTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
}
