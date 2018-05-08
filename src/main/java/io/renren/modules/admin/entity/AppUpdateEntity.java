package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 版本更新
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-07 11:06:17
 */
public class AppUpdateEntity implements Serializable {

	//
	private Integer id;
	private String appId;
	//版本
	private String version;
	private Integer versionCode;
	//apk下载地址
	private String apkUrl;
	//强制更新
	private Integer force;
	//描述
	private String describe;
	//开放下载
	private Integer openDown;
	//开放时间
	private Date openDownTime;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * 设置：版本
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * 获取：版本
	 */
	public String getVersion() {
		return version;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	/**
	 * 设置：apk下载地址
	 */
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	/**
	 * 获取：apk下载地址
	 */
	public String getApkUrl() {
		return apkUrl;
	}
	/**
	 * 设置：强制更新
	 */
	public void setForce(Integer force) {
		this.force = force;
	}
	/**
	 * 获取：强制更新
	 */
	public Integer getForce() {
		return force;
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
	 * 设置：开放下载
	 */
	public void setOpenDown(Integer openDown) {
		this.openDown = openDown;
	}
	/**
	 * 获取：开放下载
	 */
	public Integer getOpenDown() {
		return openDown;
	}
	/**
	 * 设置：开放时间
	 */
	public void setOpenDownTime(Date openDownTime) {
		this.openDownTime = openDownTime;
	}
	/**
	 * 获取：开放时间
	 */
	public Date getOpenDownTime() {
		return openDownTime;
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
