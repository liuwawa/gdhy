package io.renren.modules.invite.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 发放记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-20 10:14:28
 */
public class InviteRewardRecordEntity implements Serializable {

	//师傅ID
	private Long master;
	//徒弟ID
	private Long apprentice;
	//第几天
	private Integer day;
	//金币
	private Integer gold;
	//奖励规则
	private Integer rewardRule;
	//创建时间
	private Date createTime;
	//标题
	private String title;
	//描述
	private String describe;

	/**
	 * 设置：师傅ID
	 */
	public void setMaster(Long master) {
		this.master = master;
	}
	/**
	 * 获取：师傅ID
	 */
	public Long getMaster() {
		return master;
	}
	/**
	 * 设置：徒弟ID
	 */
	public void setApprentice(Long apprentice) {
		this.apprentice = apprentice;
	}
	/**
	 * 获取：徒弟ID
	 */
	public Long getApprentice() {
		return apprentice;
	}
	/**
	 * 设置：第几天
	 */
	public void setDay(Integer day) {
		this.day = day;
	}
	/**
	 * 获取：第几天
	 */
	public Integer getDay() {
		return day;
	}
	/**
	 * 设置：金币
	 */
	public void setGold(Integer gold) {
		this.gold = gold;
	}
	/**
	 * 获取：金币
	 */
	public Integer getGold() {
		return gold;
	}
	/**
	 * 设置：奖励规则
	 */
	public void setRewardRule(Integer rewardRule) {
		this.rewardRule = rewardRule;
	}
	/**
	 * 获取：奖励规则
	 */
	public Integer getRewardRule() {
		return rewardRule;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
