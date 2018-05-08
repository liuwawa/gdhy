package io.renren.modules.admin.entity;

import java.io.Serializable;


/**
 * 
 * 
 * @author xieweilie
 * 资讯点赞的实体
 * @date 2018-03-20 17:34:17
 */
public class Fabulous implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//点赞id
	private Integer fid;
	//用户ID
	private Long uid;
	//文章ID
	private String aid;

	//资讯点赞总数
	private Integer infoFabTotal;
	//用户对该文章是否已经点赞
	private boolean flag;


	public Integer getInfoFabTotal() {
		return infoFabTotal;
	}

	public void setInfoFabTotal(Integer infoFabTotal) {
		this.infoFabTotal = infoFabTotal;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * 设置：点赞id
	 */
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	/**
	 * 获取：点赞id
	 */
	public Integer getFid() {
		return fid;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：文章ID
	 */
	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}
}
