package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.TaskRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-24 17:59:05
 */
@Mapper
public interface TaskRecordDao {

    TaskRecordEntity queryObject(@Param("startTime") Date startTime, @Param("userId") Long userId);

    List<TaskRecordEntity> queryList(Map<String, Object> map);

    List<TaskRecordEntity> queryList(TaskRecordEntity id);

    int queryTotal(Map<String, Object> map);

    void saveBaseTask(Map<String, Object> map);

    void saveDailyTask(Map<String, Object> map);

    void saveNoviceTask(Map<String, Object> map);

    void saveSignTask(Map<String, Object> map);

    List<TaskRecordEntity> queryByUser(@Param("userId") Long userId,@Param("typeId") Integer typeId, @Param("a") Integer a, @Param("b") Integer b);

    List<Integer> dailyFinishIdList(Long userId);

    List<Integer> noviceFinishIdList(Long userId);

    Integer querySumReward(@Param("user_id") Long userid,@Param("types") int[] types,@Param("time")String time,@Param("timeB") String timeB);

}
