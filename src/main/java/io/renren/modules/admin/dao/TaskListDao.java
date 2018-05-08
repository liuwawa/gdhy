package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.TaskListEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 任务列表

 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-27 12:56:40
 */
@Mapper
public interface TaskListDao extends BaseDao<TaskListEntity> {

    List<TaskListEntity> queryDailyList();

    List<TaskListEntity> queryNoviceList();
}
