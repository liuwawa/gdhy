package io.renren.modules.invite.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 收徒记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-19 21:46:21
 */
public class InviteRecordEntity implements Serializable {

	//师傅ID
	private Long master;
	//徒弟ID
	private Long apprentice;
	//收徒时间
	private Date createTime;
	//收益
	private Integer profit;
	//已发放
	private Integer gave;
	//待发放
	private Integer waitGive;
	//描述
	private String title;
	//邀请码
	private Integer inviteCode;
	//奖励规则
	private Integer rewardRule;
	//下次奖励
	private Integer nextReward;
	//上次奖励
	private Date lastRewardDate;
	//伪删除，1为删除
	private Integer delFlag;
	//徒弟提供的阅读收益
	private Integer readProfit;
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
	 * 设置：收徒时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：收徒时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：收益
	 */
	public void setProfit(Integer profit) {
		this.profit = profit;
	}
	/**
	 * 获取：收益
	 */
	public Integer getProfit() {
		return profit;
	}
	/**
	 * 设置：已发放
	 */
	public void setGave(Integer gave) {
		this.gave = gave;
	}
	/**
	 * 获取：已发放
	 */
	public Integer getGave() {
		return gave;
	}
	/**
	 * 设置：待发放
	 */
	public void setWaitGive(Integer waitGive) {
		this.waitGive = waitGive;
	}
	/**
	 * 获取：待发放
	 */
	public Integer getWaitGive() {
		return waitGive;
	}
	/**
	 * 设置：描述
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：描述
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：邀请码
	 */
	public void setInviteCode(Integer inviteCode) {
		this.inviteCode = inviteCode;
	}
	/**
	 * 获取：邀请码
	 */
	public Integer getInviteCode() {
		return inviteCode;
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
	 * 设置：下次奖励
	 */
	public void setNextReward(Integer nextReward) {
		this.nextReward = nextReward;
	}
	/**
	 * 获取：下次奖励
	 */
	public Integer getNextReward() {
		return nextReward;
	}
	/**
	 * 设置：上次奖励
	 */
	public void setLastRewardDate(Date lastRewardDate) {
		this.lastRewardDate = lastRewardDate;
	}
	/**
	 * 获取：上次奖励
	 */
	public Date getLastRewardDate() {
		return lastRewardDate;
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

	/**
	 * 获得阅读收益
	 * @return
	 */
	public Integer getReadProfit() {
		return readProfit;
	}

	/**
	 * 设置阅读收益
	 * @param readProfit
	 */
	public void setReadProfit(Integer readProfit) {
		this.readProfit = readProfit;
	}
}
