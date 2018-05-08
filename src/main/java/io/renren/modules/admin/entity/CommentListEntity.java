package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户反馈信息
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-29 16:21:04
 */
public class CommentListEntity implements Serializable {

	//
	private Long id;
	//用户id
	private Long userId;
	//意见
	private String opinion;
	//图片url  分号隔开
	private String imgUrl;
	//手机号码
	private String phone;
	//app版本
	private String appEdition;
	//手机版本
	private String sysEdition;
	//系统名称
	private String sysName;
	//手机标识
	private String phoneMark;
	//手机设备id
	private String phoneEquipmentId;
	//提交时间
	private Date submitTime;
	//是否解决反馈
	private Integer isSolve;
	//备注
	private String remarks;
	//伪删除
	private Integer delFlag;

	//修改时间
	private Date modifyTime;
	//修改者
	private Long modifyUser;

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
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：意见
	 */
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	/**
	 * 获取：意见
	 */
	public String getOpinion() {
		return opinion;
	}
	/**
	 * 设置：图片url  分号隔开
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	/**
	 * 获取：图片url  分号隔开
	 */
	public String getImgUrl() {
		return imgUrl;
	}
	/**
	 * 设置：手机号码
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：手机号码
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：app版本
	 */
	public void setAppEdition(String appEdition) {
		this.appEdition = appEdition;
	}
	/**
	 * 获取：app版本
	 */
	public String getAppEdition() {
		return appEdition;
	}
	/**
	 * 设置：手机版本
	 */
	public void setSysEdition(String sysEdition) {
		this.sysEdition = sysEdition;
	}
	/**
	 * 获取：手机版本
	 */
	public String getSysEdition() {
		return sysEdition;
	}
	/**
	 * 设置：系统名称
	 */
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	/**
	 * 获取：系统名称
	 */
	public String getSysName() {
		return sysName;
	}
	/**
	 * 设置：手机标识
	 */
	public void setPhoneMark(String phoneMark) {
		this.phoneMark = phoneMark;
	}
	/**
	 * 获取：手机标识
	 */
	public String getPhoneMark() {
		return phoneMark;
	}
	/**
	 * 设置：手机设备id
	 */
	public void setPhoneEquipmentId(String phoneEquipmentId) {
		this.phoneEquipmentId = phoneEquipmentId;
	}
	/**
	 * 获取：手机设备id
	 */
	public String getPhoneEquipmentId() {
		return phoneEquipmentId;
	}
	/**
	 * 设置：提交时间
	 */
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	/**
	 * 获取：提交时间
	 */
	public Date getSubmitTime() {
		return submitTime;
	}
	/**
	 * 设置：是否解决反馈
	 */
	public void setIsSolve(Integer isSolve) {
		this.isSolve = isSolve;
	}
	/**
	 * 获取：是否解决反馈
	 */
	public Integer getIsSolve() {
		return isSolve;
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
