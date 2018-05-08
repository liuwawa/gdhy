package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.AppLogEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户日志
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-19 14:46:43
 */
@Mapper
public interface AppLogDao extends BaseDao<AppLogEntity> {
	
}
