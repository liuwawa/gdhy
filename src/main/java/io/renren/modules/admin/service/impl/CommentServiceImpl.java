package io.renren.modules.admin.service.impl;


import io.renren.modules.admin.dao.CommentDao;
import io.renren.modules.admin.entity.CommentEntity;
import io.renren.modules.admin.entity.CommentFabulos;
import io.renren.modules.admin.entity.Fabulous;
import io.renren.modules.admin.entity.vo.CommAndFabulousVo;
import io.renren.modules.admin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hc on 2018/3/21.
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao cDao;

    @Override
    public void saveComment(CommentEntity usercomment) {

        Date date = new Date();
        usercomment.setCommentDate(date);

        cDao.saveComment(usercomment);
    }

    @Override
    public Fabulous queryFabulous(Fabulous fabulous) {

        return cDao.queryFabulous(fabulous);
    }

    @Override
    public void saveFab(Fabulous fabulous) {

         cDao.saveFab(fabulous);
    }

    @Override
    public List<CommAndFabulousVo> queryCommend(CommAndFabulousVo vo) {

        if(vo.getSize() == null){
            vo.setSize(100);
        }
        if(vo.getPage() == null){
            vo.setPage(1);
        }
        vo.setBegin((vo.getPage() - 1) * vo.getSize());

        return cDao.queryCommend(vo);
    }


    @Override
    public int queryCommentTotalByAid(String aid) {

        return cDao.queryCommentTotalByAid(aid);
    }

    @Override
    public int queryFabulousTotalByAid(String aid) {

        return cDao.queryFabulousTotalByAid(aid);
    }






    @Override
    public CommentEntity queryObject(Integer cid) {
        return cDao.queryObject(cid);
    }

    @Override
    public List<CommentEntity> queryList(Map<String, Object> map) {
        return cDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return cDao.queryTotal(map);
    }

    @Override
    public void save(CommentEntity usercomment) {
        cDao.save(usercomment);
    }

    @Override
    public void update(CommentEntity usercomment) {
        cDao.update(usercomment);
    }

    @Override
    public void delete(Integer cid) {
        cDao.delete(cid);
    }

    @Override
    public void deleteBatch(Integer[] cids) {
        cDao.deleteBatch(cids);
    }

    @Override
    public void releaseBatch(Integer[] cids) {
        cDao.releaseBatch(cids);
    }

    @Override
    public CommentFabulos findCommFabByUserIdCid(CommentFabulos commentFabulos) {

        return cDao.findCommFabByUserIdCid(commentFabulos);
    }

    @Override
    public void saveCommFab(CommentFabulos comfab) {
        cDao.saveCommFab(comfab);
    }

    @Override
    public void addOneCommFab(Long cid) {
        cDao.addOneCommFab(cid);
    }

    @Override
    public List<Fabulous> findInfoFabuTotal(Fabulous fabulous) {

        return cDao.findInfoFabuTotal(fabulous);
    }
}
