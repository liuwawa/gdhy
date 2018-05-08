package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 金币兑换
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-08 09:09:41
 */
public class GoldExchangeEntity implements Serializable {

	//人民币
	private Integer rmb;
	//金币
	private Integer gold;
	//剩余份数
	private Integer surplus;
	//创建时间
	private Date createTime;
	//创建者
	private Long createUser;
	//修改时间
	private Date modifyTime;
	//修改者
	private Long modifyUser;
	//伪删除，1为删除
	private Integer delFlag;

	/**
	 * 设置：人民币
	 */
	public void setRmb(Integer rmb) {
		this.rmb = rmb;
	}
	/**
	 * 获取：人民币
	 */
	public Integer getRmb() {
		return rmb;
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

	public Integer getSurplus() {
		return surplus;
	}

	public void setSurplus(Integer surplus) {
		this.surplus = surplus;
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
	 * 设置：创建者
	 */
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	/**
	 * 获取：创建者
	 */
	public Long getCreateUser() {
		return createUser;
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
