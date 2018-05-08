package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.AppUpdateEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 版本更新
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-07 11:06:17
 */
@Mapper
public interface AppUpdateDao extends BaseDao<AppUpdateEntity> {

    AppUpdateEntity getLastVersion(String appId);
}
