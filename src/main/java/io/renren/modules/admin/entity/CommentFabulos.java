package io.renren.modules.admin.entity;

import java.io.Serializable;

/**
 * 某条评论的点赞实体类
 * Created by xieweilie on 2018/4/8.
 */
public class CommentFabulos implements Serializable{

    private Long cfid;
    private String aid;   //文章ID
    private Long cid;   //评论ID
    private boolean flag;   //标识
    private Long uid;   //用户id


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getCfid() {
        return cfid;
    }

    public void setCfid(Long cfid) {
        this.cfid = cfid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

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

}
