package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.FaqTypeEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 问题分类
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-20 16:43:30
 */
@Mapper
public interface FaqTypeDao extends BaseDao<FaqTypeEntity> {
}
