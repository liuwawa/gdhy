package io.renren.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 问题列表
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-20 16:43:30
 */
public class FaqEntity implements Serializable {

	//ID
	private Integer id;
	//类型
	private Integer type;
	//问题
	private String question;
	//答案
	private String answer;
	//排序
	private Integer sort;
	//相关连接
	private String relevantUrl;
	//相关问题
	private String relevantFaq;
	//版本
	private Integer version;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 设置：问题
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * 获取：问题
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * 设置：答案
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * 获取：答案
	 */
	public String getAnswer() {
		return answer;
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
	 * 设置：相关连接
	 */
	public void setRelevantUrl(String relevantUrl) {
		this.relevantUrl = relevantUrl;
	}
	/**
	 * 获取：相关连接
	 */
	public String getRelevantUrl() {
		return relevantUrl;
	}
	/**
	 * 设置：相关问题
	 */
	public void setRelevantFaq(String relevantFaq) {
		this.relevantFaq = relevantFaq;
	}
	/**
	 * 获取：相关问题
	 */
	public String getRelevantFaq() {
		return relevantFaq;
	}
	/**
	 * 设置：版本
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	/**
	 * 获取：版本
	 */
	public Integer getVersion() {
		return version;
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
