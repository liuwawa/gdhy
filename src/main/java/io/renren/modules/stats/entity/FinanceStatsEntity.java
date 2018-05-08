package io.renren.modules.stats.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 财务支出情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
public class FinanceStatsEntity implements Serializable {

	//
	private Integer id;
	//概况类型：HOUR,DAY,MONTH,YEAR
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
	//总计金额
	private Integer total;
	//增长金额
	private Integer increase;
	//同比增长
	private Integer compairson;

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
	 * 设置：概况类型：HOUR,DAY,MONTH,YEAR
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：概况类型：HOUR,DAY,MONTH,YEAR
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
	 * 设置：总计金额
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * 获取：总计金额
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * 设置：增长金额
	 */
	public void setIncrease(Integer increase) {
		this.increase = increase;
	}
	/**
	 * 获取：增长金额
	 */
	public Integer getIncrease() {
		return increase;
	}
	/**
	 * 设置：同比增长
	 */
	public void setCompairson(Integer compairson) {
		this.compairson = compairson;
	}
	/**
	 * 获取：同比增长
	 */
	public Integer getCompairson() {
		return compairson;
	}
}
