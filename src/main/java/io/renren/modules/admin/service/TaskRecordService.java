package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.TaskRecordEntity;

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
public interface TaskRecordService {
	
	TaskRecordEntity queryObject(Date id, Long userId);
	
	List<TaskRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

    int saveBaseTask(TaskRecordEntity entity, int timeLimit);

	int saveDailyTask(TaskRecordEntity entity);

    int saveNoviceTask(TaskRecordEntity entity);

    int saveSignTask(TaskRecordEntity entity);

    String translateErr(int result);

    List<TaskRecordEntity> queryByUser(Long userId,int typeId, int page, int limit);

    /**
     * 查询某个用户完成的日常任务的id
     * @param userId
     * @return
     */
    List<Integer> dailyFinishIdList(Long userId);

    /**
     * 查询某个用户完成的新手任务的id
     * @param userId
     * @return
     */
    List<Integer> noviceFinishIdList(Long userId);

    Integer querySumReward(Long userid, int[] types, String time, String timeB);
}
