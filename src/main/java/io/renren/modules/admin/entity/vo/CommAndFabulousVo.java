package io.renren.modules.admin.entity.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hc on 2018/3/20.
 */
public class CommAndFabulousVo implements Serializable{

    private Long cid;  //评论ID
    private Long uid;   //用户ID
    private String aid;   //文章ID
    private Integer page; //页数
    private Integer begin; //开始数
    private Integer size; //显示条数
    private Integer commFabTotal; //该评论下的点赞总数
    private boolean flag;  //该用户在该评论下是否是点赞

    private String headImg; //用户头像
    private String uname;  //用户名字
    private String comment; //用户评论
    private Date commentdate;  //时间
    private Integer fabNum; //点赞数
    private Integer totalComment; //评论总数

//    Date nowdate = null;
//    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCommFabTotal() {
        return commFabTotal;
    }

    public void setCommFabTotal(Integer commFabTotal) {
        this.commFabTotal = commFabTotal;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Date getCommentdate() {
        return commentdate;
    }

    public void setCommentdate(Date commentdate) {
        this.commentdate = commentdate;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(Integer totalComment) {
        this.totalComment = totalComment;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    public Integer getFabNum() {
        return fabNum;
    }

    public void setFabNum(Integer fabNum) {
        this.fabNum = fabNum;
    }
}
