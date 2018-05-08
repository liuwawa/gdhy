package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户金币
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-24 17:59:05
 */
public class GoldEntity implements Serializable {

	
	//用户ID
	private Long id;
	//今日金币
	private Integer today;
	//剩余金币
	private Integer surplus;
	//总金币
	private Integer total;
	//最后一次兑换
	private String lastExchange;
	//最后一次任务
	private Date lastTask;
	//最后一次金币变动
	private Date modifyTime;
	//今日的阅读金币
	private Integer todayRead;
	//今日的视频金币
	private Integer todayVideo;
	//昨天的阅读金币
	private Integer yesterdayRead;
	//昨天的视频金币
	private Integer yesterdayVideo;

	/**
	 * 设置：用户ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：今日金币
	 */
	public void setToday(Integer today) {
		this.today = today;
	}
	/**
	 * 获取：今日金币
	 */
	public Integer getToday() {
		return today;
	}
	/**
	 * 设置：剩余金币
	 */
	public void setSurplus(Integer surplus) {
		this.surplus = surplus;
	}
	/**
	 * 获取：剩余金币
	 */
	public Integer getSurplus() {
		return surplus;
	}
	/**
	 * 设置：总金币
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * 获取：总金币
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * 设置：最后一次兑换
	 */
	public void setLastExchange(String lastExchange) {
		this.lastExchange = lastExchange;
	}
	/**
	 * 获取：最后一次兑换
	 */
	public String getLastExchange() {
		return lastExchange;
	}
	/**
	 * 设置：最后一次任务
	 */
	public void setLastTask(Date lastTask) {
		this.lastTask = lastTask;
	}
	/**
	 * 获取：最后一次任务
	 */
	public Date getLastTask() {
		return lastTask;
	}
	/**
	 * 设置：最后一次金币变动
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：最后一次金币变动
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * 获取：今日的阅读金币
	 * @return
	 */
	public Integer getTodayRead() {
		return todayRead;
	}

	/**
	 * 设置：今日的阅读金币
	 * @param todayRead
	 */
	public void setTodayRead(Integer todayRead) {
		this.todayRead = todayRead;
	}

	/**
	 * 获取：今日的视频金币
	 * @return
	 */
	public Integer getTodayVideo() {
		return todayVideo;
	}
	/**
	 * 设置：今日的视频金币
	 * @return
	 */
	public void setTodayVideo(Integer todayVideo) {
		this.todayVideo = todayVideo;
	}

	/**
	 * 获取：今日的阅读金币
	 * @return
	 */
	public Integer getYesterdayRead() {
		return yesterdayRead;
	}

	/**
	 * 设置：昨天的阅读金币
	 * @param yesterdayRead
	 */
	public void setYesterdayRead(Integer yesterdayRead) {
		this.yesterdayRead = yesterdayRead;
	}

	/**
	 * 获取：昨天视频的金币
	 * @return
	 */
	public Integer getYesterdayVideo() {
		return yesterdayVideo;
	}

	/**
	 *  设置：昨天的视频金币
	 * @param yesterdayVideo
	 */
	public void setYesterdayVideo(Integer yesterdayVideo) {
		this.yesterdayVideo = yesterdayVideo;
	}
}
