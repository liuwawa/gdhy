package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 任务列表
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-27 12:56:40
 */
public class TaskListEntity implements Serializable {

	//
	private Integer id;
	//任务类型名称
	private String name;
	//任务类型，novice新手任务，daily每日任务，base基础任务
	private String type;
	//是否激活
	private Integer activated;
	//是否完成 0未完成，1待领取，2已领取
	private int receive = 0;
	//最大金币奖励
	private Integer goldMax;
	//最小金币奖励
	private Integer goldMini;
	//图标类型
	private String icon;
	//实际收益
	private String profit;
	//链接
	private String btnUrl;
	//任务描述
	private String describe;
	//按钮文字
	private String btnText;
	//事件
	private String event;
	//开放时间
	private Date openTime;
	//关闭时间
	private Date endTime;
	//最小用时
	private Integer duration;
	//版本
	private Integer rersion;
	//排序
	private Integer sort;
	//创建时间
	private Date createTime;
	//创建者
	private Long createUser;
	//修改时间
	private Date modifyTime;
	//修改者
	private Long modifyUser;
	//伪删除
	private Integer delFlag;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：任务类型名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：任务类型名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：任务类型，novice新手任务，daily每日任务
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：任务类型，novice新手任务，daily每日任务
	 */
	public String getType() {
		return type;
	}

	public Integer getActivated() {
		return activated;
	}

	public void setActivated(Integer activated) {
		this.activated = activated;
	}

	public int getReceive() {
		return receive;
	}

	public void setReceive(int receive) {
		this.receive = receive;
	}

	/**
	 * 设置：最大金币奖励
	 */
	public void setGoldMax(Integer goldMax) {
		this.goldMax = goldMax;
	}
	/**
	 * 获取：最大金币奖励
	 */
	public Integer getGoldMax() {
		return goldMax;
	}
	/**
	 * 设置：最小金币奖励
	 */
	public void setGoldMini(Integer goldMini) {
		this.goldMini = goldMini;
	}
	/**
	 * 获取：最小金币奖励
	 */
	public Integer getGoldMini() {
		return goldMini;
	}
	/**
	 * 设置：图标类型
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * 获取：图标类型
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * 设置：实际收益
	 */
	public void setProfit(String profit) {
		this.profit = profit;
	}
	/**
	 * 获取：实际收益
	 */
	public String getProfit() {
		return profit;
	}
	/**
	 * 设置：链接
	 */
	public void setBtnUrl(String btnUrl) {
		this.btnUrl = btnUrl;
	}
	/**
	 * 获取：链接
	 */
	public String getBtnUrl() {
		return btnUrl;
	}
	/**
	 * 设置：任务描述
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	/**
	 * 获取：任务描述
	 */
	public String getDescribe() {
		return describe;
	}
	/**
	 * 设置：按钮文字
	 */
	public void setBtnText(String btnText) {
		this.btnText = btnText;
	}
	/**
	 * 获取：按钮文字
	 */
	public String getBtnText() {
		return btnText;
	}
	/**
	 * 设置：事件
	 */
	public void setEvent(String event) {
		this.event = event;
	}
	/**
	 * 获取：事件
	 */
	public String getEvent() {
		return event;
	}
	/**
	 * 设置：开放时间
	 */
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	/**
	 * 获取：开放时间
	 */
	public Date getOpenTime() {
		return openTime;
	}
	/**
	 * 设置：关闭时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：关闭时间
	 */
	public Date getEndTime() {
		return endTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * 设置：版本
	 */
	public void setRersion(Integer rersion) {
		this.rersion = rersion;
	}
	/**
	 * 获取：版本
	 */
	public Integer getRersion() {
		return rersion;
	}
	/**
	 * 设置：排序
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	/**
	 * 获取：排序
	 */
	public Integer getSort() {
		return sort;
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
	 * 设置：伪删除
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：伪删除
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
