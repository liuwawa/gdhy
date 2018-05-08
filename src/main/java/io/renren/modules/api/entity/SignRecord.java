package io.renren.modules.api.entity;

import java.util.Date;

/**
 * 用户签到记录实体
 * Created by ITMX on 2018/1/4.
 */
public class SignRecord {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 连续签到的天数
     */
    private Integer days;
    /**
     * 最后一次签到的日期
     */
    private Date lastDay;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Date getLastDay() {
        return lastDay;
    }

    public void setLastDay(Date lastDay) {
        this.lastDay = lastDay;
    }
}
