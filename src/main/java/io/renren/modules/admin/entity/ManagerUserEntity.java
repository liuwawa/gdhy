package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-16 15:08:17
 */
public class ManagerUserEntity implements Serializable {

	
	//
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
	//性别，1为男，2为女，0为未设
	private Integer gender;
	//生日
	private Date birthday;
	//账号状态
	private String status;
	//支付宝账户
	private String alipayAccount;
	//支付宝姓名
	private String alipayName;
	//微信钱包姓名
	private String weixinPayName;
	//微信钱包手机号码
	private String weixinPayPhone;
	//微信提现授权openid
	private String weixinPayOpenid;
	//微信提现授权昵称
	private String weixinPayNickname;
	//微信提现授权头像
	private String weixinPayAvatar;
	//创建时间
	private Date createTime;
	//资料更新时间
	private Date modifyTime;
	//是否禁用
	private Integer disableFlag;
	//被禁原因
	private String disableMsg;
	//被禁时间
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
	//伪删除，1为删除
	private Integer delFlag;

	/**
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
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
	 * 设置：盐
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * 获取：盐
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * 设置：邀请码
	 */
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	/**
	 * 获取：邀请码
	 */
	public String getInviteCode() {
		return inviteCode;
	}
	/**
	 * 设置：头像
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	/**
	 * 获取：头像
	 */
	public String getAvatar() {
		return avatar;
	}
	/**
	 * 设置：性别，1为男，2为女，0为未设
	 */
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	/**
	 * 获取：性别，1为男，2为女，0为未设
	 */
	public Integer getGender() {
		return gender;
	}
	/**
	 * 设置：生日
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	/**
	 * 获取：生日
	 */
	public Date getBirthday() {
		return birthday;
	}
	/**
	 * 设置：账号状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：账号状态
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：支付宝账户
	 */
	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
	/**
	 * 获取：支付宝账户
	 */
	public String getAlipayAccount() {
		return alipayAccount;
	}
	/**
	 * 设置：支付宝姓名
	 */
	public void setAlipayName(String alipayName) {
		this.alipayName = alipayName;
	}
	/**
	 * 获取：支付宝姓名
	 */
	public String getAlipayName() {
		return alipayName;
	}
	/**
	 * 设置：微信钱包姓名
	 */
	public void setWeixinPayName(String weixinPayName) {
		this.weixinPayName = weixinPayName;
	}
	/**
	 * 获取：微信钱包姓名
	 */
	public String getWeixinPayName() {
		return weixinPayName;
	}
	/**
	 * 设置：微信钱包手机号码
	 */
	public void setWeixinPayPhone(String weixinPayPhone) {
		this.weixinPayPhone = weixinPayPhone;
	}
	/**
	 * 获取：微信钱包手机号码
	 */
	public String getWeixinPayPhone() {
		return weixinPayPhone;
	}
	/**
	 * 设置：微信提现授权openid
	 */
	public void setWeixinPayOpenid(String weixinPayOpenid) {
		this.weixinPayOpenid = weixinPayOpenid;
	}
	/**
	 * 获取：微信提现授权openid
	 */
	public String getWeixinPayOpenid() {
		return weixinPayOpenid;
	}
	/**
	 * 设置：微信提现授权昵称
	 */
	public void setWeixinPayNickname(String weixinPayNickname) {
		this.weixinPayNickname = weixinPayNickname;
	}
	/**
	 * 获取：微信提现授权昵称
	 */
	public String getWeixinPayNickname() {
		return weixinPayNickname;
	}
	/**
	 * 设置：微信提现授权头像
	 */
	public void setWeixinPayAvatar(String weixinPayAvatar) {
		this.weixinPayAvatar = weixinPayAvatar;
	}
	/**
	 * 获取：微信提现授权头像
	 */
	public String getWeixinPayAvatar() {
		return weixinPayAvatar;
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
	/**
	 * 设置：资料更新时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：资料更新时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：是否禁用
	 */
	public void setDisableFlag(Integer disableFlag) {
		this.disableFlag = disableFlag;
	}
	/**
	 * 获取：是否禁用
	 */
	public Integer getDisableFlag() {
		return disableFlag;
	}
	/**
	 * 设置：被禁原因
	 */
	public void setDisableMsg(String disableMsg) {
		this.disableMsg = disableMsg;
	}
	/**
	 * 获取：被禁原因
	 */
	public String getDisableMsg() {
		return disableMsg;
	}
	/**
	 * 设置：被禁时间
	 */
	public void setDisableTime(Date disableTime) {
		this.disableTime = disableTime;
	}
	/**
	 * 获取：被禁时间
	 */
	public Date getDisableTime() {
		return disableTime;
	}
	/**
	 * 设置：是否自动解禁
	 */
	public void setAutoEnable(Integer autoEnable) {
		this.autoEnable = autoEnable;
	}
	/**
	 * 获取：是否自动解禁
	 */
	public Integer getAutoEnable() {
		return autoEnable;
	}
	/**
	 * 设置：自动解禁时间
	 */
	public void setAutoEnableTime(Date autoEnableTime) {
		this.autoEnableTime = autoEnableTime;
	}
	/**
	 * 获取：自动解禁时间
	 */
	public Date getAutoEnableTime() {
		return autoEnableTime;
	}
	/**
	 * 设置：微信绑定
	 */
	public void setWeixinBindOpenid(String weixinBindOpenid) {
		this.weixinBindOpenid = weixinBindOpenid;
	}
	/**
	 * 获取：微信绑定
	 */
	public String getWeixinBindOpenid() {
		return weixinBindOpenid;
	}
	/**
	 * 设置：支付宝绑定
	 */
	public void setAlipayBindOpenid(String alipayBindOpenid) {
		this.alipayBindOpenid = alipayBindOpenid;
	}
	/**
	 * 获取：支付宝绑定
	 */
	public String getAlipayBindOpenid() {
		return alipayBindOpenid;
	}
	/**
	 * 设置：用户注册时的设备ID
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：用户注册时的设备ID
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：伪删除，1为删除
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：伪删除，1为删除
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
