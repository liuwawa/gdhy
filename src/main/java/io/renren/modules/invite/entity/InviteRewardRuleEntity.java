package io.renren.modules.invite.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 奖励规则
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-19 14:27:31
 */
public class InviteRewardRuleEntity implements Serializable {

	//ID
	private Integer id;
	//立即奖励
	private Integer firstReward;
	//第1天
	private Integer day1;
	//第2天
	private Integer day2;
	//第3天
	private Integer day3;
	//第4天
	private Integer day4;
	//第5天
	private Integer day5;
	//第6天
	private Integer day6;
	//第7天
	private Integer day7;
	//发放条件
	private Integer threshold;
	//创建时间
	private Date createTime;
	//创建者
	private Long createUser;
	//规则生效
	private Date effectiveTime;
	//规则过期
	private Date expiryTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFirstReward() {
		return firstReward;
	}

	public void setFirstReward(Integer firstReward) {
		this.firstReward = firstReward;
	}

	public Integer getDay1() {
		return day1;
	}

	public void setDay1(Integer day1) {
		this.day1 = day1;
	}

	public Integer getDay2() {
		return day2;
	}

	public void setDay2(Integer day2) {
		this.day2 = day2;
	}

	public Integer getDay3() {
		return day3;
	}

	public void setDay3(Integer day3) {
		this.day3 = day3;
	}

	public Integer getDay4() {
		return day4;
	}

	public void setDay4(Integer day4) {
		this.day4 = day4;
	}

	public Integer getDay5() {
		return day5;
	}

	public void setDay5(Integer day5) {
		this.day5 = day5;
	}

	public Integer getDay6() {
		return day6;
	}

	public void setDay6(Integer day6) {
		this.day6 = day6;
	}

	public Integer getDay7() {
		return day7;
	}

	public void setDay7(Integer day7) {
		this.day7 = day7;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}

}
