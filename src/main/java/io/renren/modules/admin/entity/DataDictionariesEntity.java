package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 数据字典
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-13 15:01:38
 */
public class DataDictionariesEntity implements Serializable {

	//
	private Long id;
	//key
	private String key;
	//类型
	private Integer type;
	//
	private String value;

	private String version;
	//
	private Date createTime;
	//
	private Long createUser;
	//
	private Date modifyTime;
	//
	private Long modifyUser;
	//
	private Integer delFlag;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * 获取：key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * 设置：类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取：
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	/**
	 * 获取：
	 */
	public Long getCreateUser() {
		return createUser;
	}
	/**
	 * 设置：
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：
	 */
	public void setModifyUser(Long modifyUser) {
		this.modifyUser = modifyUser;
	}
	/**
	 * 获取：
	 */
	public Long getModifyUser() {
		return modifyUser;
	}
	/**
	 * 设置：
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
