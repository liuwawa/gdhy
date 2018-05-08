package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户日志
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-19 14:46:43
 */
public class AppLogEntity implements Serializable {

	
	//ID
	private String id;
	//日志类型
	private Integer type;
	//用户ID
	private Long user;
	//访问IP
	private String ip;
	//级别
	private Integer level;
	//操作
	private Integer operation;
	//描述
	private String describe;
	//应用ID
	private String appId;
	//实例ID
	private String instanceId;
	//备注
	private String remarks;
	//时间
	private Date createTime;

	/**
	 * 设置：ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：日志类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：日志类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUser(Long user) {
		this.user = user;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUser() {
		return user;
	}
	/**
	 * 设置：访问IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * 获取：访问IP
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置：级别
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * 获取：级别
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * 设置：操作
	 */
	public void setOperation(Integer operation) {
		this.operation = operation;
	}
	/**
	 * 获取：操作
	 */
	public Integer getOperation() {
		return operation;
	}
	/**
	 * 设置：描述
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	/**
	 * 获取：描述
	 */
	public String getDescribe() {
		return describe;
	}
	/**
	 * 设置：应用ID
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * 获取：应用ID
	 */
	public String getAppId() {
		return appId;
	}
	/**
	 * 设置：实例ID
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	/**
	 * 获取：实例ID
	 */
	public String getInstanceId() {
		return instanceId;
	}
	/**
	 * 设置：备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * 设置：时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
