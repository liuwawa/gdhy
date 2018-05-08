package io.renren.modules.invite.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 邀请码
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-18 16:28:29
 */
public class InviteCodeEntity implements Serializable {

	//邀请码
	private Integer inviteCode;
	//用户ID
	private Long userId;
	//0正常,-1禁用,-2作废
	private Integer state;
	//创建时间
	private Date createTime;

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
	 * 设置：0正常,-1禁用,-2作废
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：0正常,-1禁用,-2作废
	 */
	public Integer getState() {
		return state;
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
}
