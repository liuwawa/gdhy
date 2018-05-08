package io.renren.modules.stats.dao;

import io.renren.modules.stats.entity.TaskActive;
import io.renren.modules.stats.entity.TaskStatsEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 任务活跃情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@Mapper
public interface TaskStatsDao extends BaseDao<TaskStatsEntity> {

    List<TaskActive> queryTaskTypeRecordByTime(@Param("a") Date a, @Param("b") Date b);

    List<TaskActive> sumTaskActiveFromStats(@Param("type") String type,@Param("a") Date a, @Param("b") Date b);

    List<TaskStatsEntity> RangeByCustom(@Param("type") String type,@Param("a") String a,@Param("b") String b,@Param("field") String field);

    List<TaskStatsEntity> RangeByHour(@Param("type") String type,@Param("a") String a,@Param("b") String b
            , @Param("hours") int hours,@Param("hours1") int hours1);
}
