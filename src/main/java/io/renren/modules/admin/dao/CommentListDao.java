package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.CommentListEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户反馈信息
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-29 16:21:04
 */
@Mapper
public interface CommentListDao extends BaseDao<CommentListEntity> {
	
}
