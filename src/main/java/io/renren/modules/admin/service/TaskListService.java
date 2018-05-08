package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.TaskListEntity;
import io.renren.modules.api.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 任务列表

 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-27 12:56:40
 */
public interface TaskListService {
	
	TaskListEntity queryObject(Integer id);
	
	List<TaskListEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TaskListEntity taskList);
	
	void update(TaskListEntity taskList);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	List<TaskListEntity> queryDailyList();

	List<TaskListEntity> queryNoviceList();

    boolean isReceive(TaskListEntity taskEntity, UserEntity user);
}
