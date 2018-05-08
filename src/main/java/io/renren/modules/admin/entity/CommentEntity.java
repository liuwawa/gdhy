package io.renren.modules.admin.entity;



import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author 谢韦烈
 * @date 2018-03-20 16:35:14
 */

public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//评论ID

	private Long cid;
	//用户ID
	private Long uid;
	//文章ID
	private String aid;
	//用户名
	private String uName;
	//头像地址
	private String headImg;
	//评论

	private String comment;
	//提交评论时间
	private Date commentDate;

	//评论状态
	private Integer delFlag;

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
}
