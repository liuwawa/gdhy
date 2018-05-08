package io.renren.modules.stats.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 任务活跃情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
public class TaskStatsEntity implements Serializable {

	//
	private Integer id;
	//
	private String type;
	//小时
	private Integer thanHour;
	//日期
	private Date thanDay;
	//月份
	private Integer thanMonth;
	//
	private Integer thanYear;
	//创建时间
	private Date createTime;
	//任务ID
	private Integer taskId;
	//任务执行次数
	private Integer total;
	//送出金币总数
	private Long gold;
	//增长数量
	private Long increase;
	//同比增长
	private Long compairson;

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
	 * 设置：
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：小时
	 */
	public void setThanHour(Integer thanHour) {
		this.thanHour = thanHour;
	}
	/**
	 * 获取：小时
	 */
	public Integer getThanHour() {
		return thanHour;
	}
	/**
	 * 设置：日期
	 */
	public void setThanDay(Date thanDay) {
		this.thanDay = thanDay;
	}
	/**
	 * 获取：日期
	 */
	public Date getThanDay() {
		return thanDay;
	}
	/**
	 * 设置：月份
	 */
	public void setThanMonth(Integer thanMonth) {
		this.thanMonth = thanMonth;
	}
	/**
	 * 获取：月份
	 */
	public Integer getThanMonth() {
		return thanMonth;
	}
	/**
	 * 设置：
	 */
	public void setThanYear(Integer thanYear) {
		this.thanYear = thanYear;
	}
	/**
	 * 获取：
	 */
	public Integer getThanYear() {
		return thanYear;
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
	 * 设置：任务ID
	 */
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	/**
	 * 获取：任务ID
	 */
	public Integer getTaskId() {
		return taskId;
	}
	/**
	 * 设置：任务执行次数
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * 获取：任务执行次数
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * 设置：送出金币总数
	 */
	public void setGold(Long gold) {
		this.gold = gold;
	}
	/**
	 * 获取：送出金币总数
	 */
	public Long getGold() {
		return gold;
	}
	/**
	 * 设置：增长数量
	 */
	public void setIncrease(Long increase) {
		this.increase = increase;
	}
	/**
	 * 获取：增长数量
	 */
	public Long getIncrease() {
		return increase;
	}
	/**
	 * 设置：同比增长
	 */
	public void setCompairson(Long compairson) {
		this.compairson = compairson;
	}
	/**
	 * 获取：同比增长
	 */
	public Long getCompairson() {
		return compairson;
	}
}
