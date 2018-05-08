package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 提现记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-08 09:09:41
 */
public class WithdrawalsEntity implements Serializable {

	//ID
	private String id;
	//用户ID
	private Long userId;
	//提现金币
	private Integer gold;
	//提现金额(分)
	private Integer rmb;
	//累计提现
	private Integer totalRmb;
	//提现状态：-1已关闭，1处理中，2已完成
	private Integer status;
	//收款渠道
	private String channel;
	//手机号
	private String phone;
	//姓名
	private String name;
	//收款账户
	private String userAccount;
	//提现后余额
	private Integer banlance;
	//支付单号
	private String payNo;
	//下单时间
	private Date createTime;
	//完成时间
	private Date finishTime;
	//关闭时间
	private Date closeTime;
	//订单关闭原因
	private String closeMsg;

	/**
	 * 设置：ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public String getId() {
		return id;
	}
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
	 * 设置：提现金币
	 */
	public void setGold(Integer gold) {
		this.gold = gold;
	}
	/**
	 * 获取：提现金币
	 */
	public Integer getGold() {
		return gold;
	}
	/**
	 * 设置：提现金额(分)
	 */
	public void setRmb(Integer rmb) {
		this.rmb = rmb;
	}
	/**
	 * 获取：提现金额(分)
	 */
	public Integer getRmb() {
		return rmb;
	}

	public Integer getTotalRmb() {
		return totalRmb;
	}

	public void setTotalRmb(Integer totalRmb) {
		this.totalRmb = totalRmb;
	}

	/**
	 * 设置：提现状态：-1已关闭，1处理中，2已完成
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：提现状态：-1已关闭，1处理中，2已完成
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：收款渠道
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * 获取：收款渠道
	 */
	public String getChannel() {
		return channel;
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
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * 设置：提现后余额
	 */
	public void setBanlance(Integer banlance) {
		this.banlance = banlance;
	}
	/**
	 * 获取：提现后余额
	 */
	public Integer getBanlance() {
		return banlance;
	}
	/**
	 * 设置：支付单号
	 */
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	/**
	 * 获取：支付单号
	 */
	public String getPayNo() {
		return payNo;
	}
	/**
	 * 设置：下单时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：下单时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：完成时间
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	/**
	 * 获取：完成时间
	 */
	public Date getFinishTime() {
		return finishTime;
	}
	/**
	 * 设置：关闭时间
	 */
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	/**
	 * 获取：关闭时间
	 */
	public Date getCloseTime() {
		return closeTime;
	}
	/**
	 * 设置：订单关闭原因
	 */
	public void setCloseMsg(String closeMsg) {
		this.closeMsg = closeMsg;
	}
	/**
	 * 获取：订单关闭原因
	 */
	public String getCloseMsg() {
		return closeMsg;
	}
}
