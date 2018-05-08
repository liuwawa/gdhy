package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 问题分类
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-20 16:43:30
 */
public class FaqTypeEntity implements Serializable {

	
	//ID
	private Integer id;
	//分类名称
	private String title;
	//排序
	private Integer sort;
	//相关链接
	private String relevantUrl;
	//创建者
	private Long createUser;
	//创建时间
	private Date createTime;
	//修改者
	private Long modifyUser;
	//修改时间
	private Date modifyTime;
	//伪删除，1为删除
	private Integer delFlag;

	/**
	 * 设置：ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：分类名称
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：分类名称
	 */
	public String getTitle() {
		return title;
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
	 * 设置：相关链接
	 */
	public void setRelevantUrl(String relevantUrl) {
		this.relevantUrl = relevantUrl;
	}
	/**
	 * 获取：相关链接
	 */
	public String getRelevantUrl() {
		return relevantUrl;
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
