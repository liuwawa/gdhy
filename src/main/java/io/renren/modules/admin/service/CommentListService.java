package io.renren.modules.admin.service;

import io.renren.common.utils.Query;
import io.renren.modules.admin.entity.CommentListEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户反馈信息
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-29 16:21:04
 */
public interface CommentListService {
	
	CommentListEntity queryObject(Long id);
	
	List<CommentListEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CommentListEntity commentList);
	
	void update(Query query);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
