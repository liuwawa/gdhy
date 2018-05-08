package io.renren.modules.stats.task;

import io.renren.modules.stats.entity.GoldStatsEntity;
import io.renren.modules.stats.service.GoldStatsService;
import io.renren.modules.stats.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by ITMX on 2018/2/6.
 */
@Component("goldStatsTask")
public class GoldStatsTask {

    @Autowired
    private GoldStatsService goldStatsService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 每小时统计
     */
    public void hour(){
        logger.info("开始时级统计...");
        GoldStatsEntity entity = new GoldStatsEntity();
        entity.setCreateTime(new Date());
        entity.setThanHour(DateUtils.preHour(entity.getCreateTime()));
        Date date = DateUtils.preHourTime(entity.getCreateTime());
        entity.setThanDay(DateUtils.currentDayTime(date));
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("HOUR");

//        //统计上一个小时领取的金币数
//        Integer total = goldStatsService.sumGoldByTime(DateUtils.preHourTime(new Date()), DateUtils.currentHourTime(new Date()));
//        entity.setTotal(total);
        //前天跟昨天的
        Integer yesterdayTotal = goldStatsService.sumGoldByTime(DateUtils.preHours(entity.getCreateTime(),-2), date);
        //今天的
        Integer total= goldStatsService.sumGoldByTime(date, DateUtils.currentHourTime(entity.getCreateTime()));
        total=(total==null?0:total);
        entity.setCompairson(total-(yesterdayTotal==null?0:yesterdayTotal));
        entity.setTotal(total);

        goldStatsService.save(entity);
        logger.info("完成时级统计...");
    }

    /**
     * 每天统计
     */
    public void day(){
        logger.info("开始日级统计...");
        GoldStatsEntity entity = new GoldStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preDayTime(entity.getCreateTime());
        entity.setThanDay(DateUtils.currentDayTime(date));
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("DAY");

        //前天跟昨天的
        Integer yesterdayTotal = goldStatsService.sumGoldByTime(DateUtils.preYesterday(entity.getCreateTime()), date);
        //今天的
        Integer total= goldStatsService.sumGoldByTime(date, DateUtils.currentDayTime(entity.getCreateTime()));
        total=(total==null?0:total);
        entity.setCompairson(total-(yesterdayTotal==null?0:yesterdayTotal));
        entity.setTotal(total);

        goldStatsService.save(entity);
        logger.info("完成日级统计...");
    }

    /**
     * 每月统计
     */
    public void month(){
        logger.info("开始月级统计...");
        GoldStatsEntity entity = new GoldStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preMonthTime(entity.getCreateTime());
        entity.setThanDay(date);
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("MONTH");

//        //统计上月领取的金币数
//        Integer total = goldStatsService.sumGoldFromStats(date, DateUtils.currentMonthTime(entity.getCreateTime()),"DAY");
//        if(total == null){
//            total = goldStatsService.sumGoldByTime(date, DateUtils.currentMonthTime(entity.getCreateTime()));
//        }
//        entity.setTotal(total);

        //前天跟昨天的
        Integer yesterdayTotal = goldStatsService.sumGoldByTime(DateUtils.preTwoMonths(entity.getCreateTime()),date);
        //今天的
        Integer total = goldStatsService.sumGoldByTime(date, DateUtils.currentMonthTime(entity.getCreateTime()));
        total=(total==null?0:total);
        entity.setCompairson(total-(yesterdayTotal==null?0:yesterdayTotal));

        entity.setTotal(total);

        goldStatsService.save(entity);
        logger.info("完成月级统计...");
    }

    /**
     * 每年统计
     */
    public void year(){
        logger.info("开始年级统计...");
        GoldStatsEntity entity = new GoldStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preYearTime(entity.getCreateTime());
        entity.setThanDay(date);
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("YEAR");

        //统计去年领取的金币数
        Integer total = goldStatsService.sumGoldFromStats(date, DateUtils.currentYearTime(entity.getCreateTime()),"MONTH");
        if(total == null){
            total = goldStatsService.sumGoldByTime(date, DateUtils.currentYearTime(entity.getCreateTime()));
        }
        entity.setTotal(total);

        goldStatsService.save(entity);
        logger.info("完成年级统计...");
    }

}
