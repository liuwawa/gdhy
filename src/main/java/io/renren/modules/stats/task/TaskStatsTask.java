package io.renren.modules.stats.task;

import io.renren.modules.stats.entity.TaskActive;
import io.renren.modules.stats.entity.TaskStatsEntity;
import io.renren.modules.stats.service.TaskStatsService;
import io.renren.modules.stats.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by ITMX on 2018/2/6.
 */
@Component("taskStatsTask")
public class TaskStatsTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskStatsService taskStatsService;

    /**
     * 每小时统计
     */
    public void hour(){
        logger.info("开始时级统计...");
        Date createTime = new Date();
        Integer thanHour = DateUtils.preHour(createTime);
        Date preHourTime = DateUtils.preHourTime(createTime);
        Date thanDay = DateUtils.currentDayTime(preHourTime);
        Integer thanMonth = DateUtils.currentMonth(preHourTime);
        Integer thanYear = DateUtils.currentYear(preHourTime);

        List<TaskActive> taskActives = taskStatsService.queryTaskTypeRecordByTime(preHourTime, DateUtils.currentHourTime(createTime));
        for (TaskActive ta : taskActives) {
            TaskStatsEntity entity = new TaskStatsEntity();
            entity.setCreateTime(createTime);
            entity.setThanHour(thanHour);
            entity.setThanDay(thanDay);
            entity.setThanMonth(thanMonth);
            entity.setThanYear(thanYear);
            entity.setType("HOUR");
            entity.setTaskId(ta.getType());
            entity.setTotal(ta.getTotal());
            entity.setIncrease(ta.getIncrease());
            taskStatsService.save(entity);
        }

        logger.info("完成时级统计...");
    }

    /**
     * 每日统计
     */
    public void day(){
        logger.info("开始日级统计...");

        Date createTime = new Date();
        Date thanDay = DateUtils.preDayTime(createTime);
        Integer thanMonth = DateUtils.currentMonth(thanDay);
        Integer thanYear = DateUtils.currentYear(thanDay);

        List<TaskActive> taskActives = taskStatsService.sumTaskActiveFromStats("HOUR",thanDay, DateUtils.currentDayTime(createTime));
        for (TaskActive ta : taskActives) {
            TaskStatsEntity entity = new TaskStatsEntity();
            entity.setCreateTime(createTime);
            entity.setThanDay(thanDay);
            entity.setThanMonth(thanMonth);
            entity.setThanYear(thanYear);
            entity.setType("DAY");
            entity.setIncrease(ta.getIncrease());
            entity.setTotal(ta.getTotal());
            entity.setTaskId(ta.getType());
            taskStatsService.save(entity);
        }

        logger.info("完成日级统计...");
    }

    /**
     * 每月统计
     */
    public void month(){
        logger.info("开始月级统计...");

        Date createTime = new Date();
        Integer thanMonth = DateUtils.preMonth(createTime);
        Date thanMonthTime = DateUtils.preMonthTime(createTime);
        Integer thanYear = DateUtils.currentYear(thanMonthTime);

        List<TaskActive> taskActives = taskStatsService.sumTaskActiveFromStats("DAY",thanMonthTime, DateUtils.currentMonthTime(createTime));
        for (TaskActive ta : taskActives) {
            TaskStatsEntity entity = new TaskStatsEntity();
            entity.setCreateTime(createTime);
            entity.setThanDay(thanMonthTime);
            entity.setThanMonth(thanMonth);
            entity.setThanYear(thanYear);
            entity.setType("MONTH");
            entity.setTotal(ta.getTotal());
            entity.setIncrease(ta.getIncrease());
            entity.setTaskId(ta.getType());
            taskStatsService.save(entity);
        }

        logger.info("完成月级统计...");
    }

    /**
     * 每年统计
     */
    public void year(){
        logger.info("开始年级统计...");

        Date createTime = new Date();
        Date thanYearTime = DateUtils.preYearTime(createTime);
        Integer thanYear = DateUtils.preYear(createTime);

        List<TaskActive> taskActives = taskStatsService.sumTaskActiveFromStats("MONTH",thanYearTime, DateUtils.currentYearTime(createTime));
        for (TaskActive ta : taskActives) {
            TaskStatsEntity entity = new TaskStatsEntity();
            entity.setCreateTime(createTime);
            entity.setThanDay(thanYearTime);
            entity.setThanYear(thanYear);
            entity.setType("YEAR");
            entity.setTotal(ta.getTotal());
            entity.setTaskId(ta.getType());
            entity.setIncrease(ta.getIncrease());
            taskStatsService.save(entity);
        }

        logger.info("完成年级统计...");
    }
}
