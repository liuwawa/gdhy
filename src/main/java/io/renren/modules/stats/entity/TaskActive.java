package io.renren.modules.stats.entity;

/**
 * Created by ITMX on 2018/2/6.
 */
public class TaskActive {
    private Integer type;
    private Integer total;
    private Long increase;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getIncrease() {
        return increase;
    }

    public void setIncrease(Long increase) {
        this.increase = increase;
    }
}
