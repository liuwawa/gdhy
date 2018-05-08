package io.renren.modules.stats.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 提现统计
 *
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
public class WithdrawalsStatsEntity implements Serializable {

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
	//总计订单数量
	private Long total;
	//提现成功的总计
	private Long totalComplete;
	//提现失败的总计
	private Long totalFail;
	//同比增长
	private Long compairson;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getThanHour() {
		return thanHour;
	}

	public void setThanHour(Integer thanHour) {
		this.thanHour = thanHour;
	}

	public Date getThanDay() {
		return thanDay;
	}

	public void setThanDay(Date thanDay) {
		this.thanDay = thanDay;
	}

	public Integer getThanMonth() {
		return thanMonth;
	}

	public void setThanMonth(Integer thanMonth) {
		this.thanMonth = thanMonth;
	}

	public Integer getThanYear() {
		return thanYear;
	}

	public void setThanYear(Integer thanYear) {
		this.thanYear = thanYear;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getTotalComplete() {
		return totalComplete;
	}

	public void setTotalComplete(Long totalComplete) {
		this.totalComplete = totalComplete;
	}

	public Long getTotalFail() {
		return totalFail;
	}

	public void setTotalFail(Long totalFail) {
		this.totalFail = totalFail;
	}

	public Long getCompairson() {
		return compairson;
	}

	public void setCompairson(Long compairson) {
		this.compairson = compairson;
	}
}
