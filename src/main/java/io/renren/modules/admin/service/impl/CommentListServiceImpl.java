package io.renren.modules.admin.service.impl;

import io.renren.common.utils.Query;
import io.renren.modules.admin.dao.CommentListDao;
import io.renren.modules.admin.entity.CommentListEntity;
import io.renren.modules.admin.service.CommentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("commentListService")
public class CommentListServiceImpl implements CommentListService {
	@Autowired
	private CommentListDao commentListDao;
	
	@Override
	public CommentListEntity queryObject(Long id){
		return commentListDao.queryObject(id);
	}
	
	@Override
	public List<CommentListEntity> queryList(Map<String, Object> map){
		return commentListDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return commentListDao.queryTotal(map);
	}
	
	@Override
	public void save(CommentListEntity commentList){
		commentListDao.save(commentList);
	}
	
	@Override
	public void update(Query query){
		commentListDao.update(query);
	}
	
	@Override
	public void delete(Long id){
		commentListDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		commentListDao.deleteBatch(ids);
	}
	
}
