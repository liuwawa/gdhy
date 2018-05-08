package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.DataDictionariesEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据字典
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-13 15:01:38
 */
@Mapper
public interface DataDictionariesDao extends BaseDao<DataDictionariesEntity> {

    String queryByKey(@Param("key") String key,@Param("version") String version);
}
