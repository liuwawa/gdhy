package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 活动中心
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-11 17:47:17
 */
public class ActivityCentralEntity implements Serializable {

	//
	private Long id;
	//分类
	private String classification;
	//标题
	private String title;
	//图片地址
	private String imgUrl;
	//描述
	private String desc;
	//标识
	private Integer tag;
	//按钮文字
	private String btnText;
	//开始时间
	private Date startTime;
	//结束时间
	private Date endTime;
	//创建时间
	private Date createTime;
	//创建者
	private String createUser;
	//修改时间
	private Date modifyTime;
	//修改者
	private String modifyUser;
	//伪删除 1为删除
	private Integer delFlag;
	//跳转链接
	private String link;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getTag() {
		return tag;
	}

	public String getBtnText() {
		return btnText;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public void setBtnText(String btnText) {
		this.btnText = btnText;
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
	 * 设置：分类
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}
	/**
	 * 获取：分类
	 */
	public String getClassification() {
		return classification;
	}
	/**
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：图片地址
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	/**
	 * 获取：图片地址
	 */
	public String getImgUrl() {
		return imgUrl;
	}
	/**
	 * 设置：描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 获取：描述
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * 设置：开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getEndTime() {
		return endTime;
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
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	/**
	 * 获取：创建者
	 */
	public String getCreateUser() {
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
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	/**
	 * 获取：修改者
	 */
	public String getModifyUser() {
		return modifyUser;
	}
	/**
	 * 设置：伪删除 1为删除
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：伪删除 1为删除
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
