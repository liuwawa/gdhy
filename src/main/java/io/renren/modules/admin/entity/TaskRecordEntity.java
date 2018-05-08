package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 任务记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-24 17:59:05
 */
public class TaskRecordEntity implements Serializable {

	//任务开始时间
	private Date id;
	//用户ID
	private Long userId;
	//任务类型
	private Integer type;
	//奖励金币
	private Integer reward;
	//描述
	private String describe;

	/**
	 * 设置：任务开始时间
	 */
	public void setId(Date id) {
		this.id = id;
	}
	/**
	 * 获取：任务开始时间
	 */
	public Date getId() {
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
	 * 设置：任务类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：任务类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：奖励金币
	 */
	public void setReward(Integer reward) {
		this.reward = reward;
	}
	/**
	 * 获取：奖励金币
	 */
	public Integer getReward() {
		return reward;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
