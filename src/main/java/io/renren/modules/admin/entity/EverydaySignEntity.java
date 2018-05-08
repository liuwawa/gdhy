package io.renren.modules.admin.entity;

import io.renren.modules.api.entity.SignRecordStatus;

import java.io.Serializable;
import java.util.Date;


/**
 * 签到奖励
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-04 10:36:14
 */
public class EverydaySignEntity implements Serializable {

	//第几天
	private Integer days;
	//金币奖励
	private Integer reward;
	//修改时间
	private Date modifyTime;
	//修改者
	private Long modifyUser;
	//改天状态 0未签到，1可签到，2已签到
	private int receive = SignRecordStatus.SIGN_NONE;

	/**
	 * 设置：第几天
	 */
	public void setDays(Integer days) {
		this.days = days;
	}
	/**
	 * 获取：第几天
	 */
	public Integer getDays() {
		return days;
	}
	/**
	 * 设置：金币奖励
	 */
	public void setReward(Integer reward) {
		this.reward = reward;
	}
	/**
	 * 获取：金币奖励
	 */
	public Integer getReward() {
		return reward;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：修改者
	 */
	public void setModifyUser(Long modifyUser) {
		this.modifyUser = modifyUser;
	}
	/**
	 * 获取：修改者
	 */
	public Long getModifyUser() {
		return modifyUser;
	}

	public int getReceive() {
		return receive;
	}

	public void setReceive(int receive) {
		this.receive = receive;
	}
}
