package io.renren.modules.admin.service;



import io.renren.modules.admin.entity.CommentEntity;
import io.renren.modules.admin.entity.CommentFabulos;
import io.renren.modules.admin.entity.Fabulous;
import io.renren.modules.admin.entity.vo.CommAndFabulousVo;

import java.util.List;
import java.util.Map;

/**
 * Created by hc on 2018/3/21.
 */

public interface CommentService {

    void saveComment(CommentEntity usercomment);

    Fabulous queryFabulous(Fabulous fabulous);

    void saveFab(Fabulous fabulous);

    List<CommAndFabulousVo> queryCommend(CommAndFabulousVo vo);

    int queryCommentTotalByAid(String aid);

    int queryFabulousTotalByAid(String aid);

    CommentEntity queryObject(Integer cid);

    List<CommentEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(CommentEntity usercomment);

    void update(CommentEntity usercomment);

    void delete(Integer cid);

    void deleteBatch(Integer[] cids);

    void releaseBatch(Integer[] cids);

    CommentFabulos findCommFabByUserIdCid(CommentFabulos commentFabulos);

    void saveCommFab(CommentFabulos comfab);


    void addOneCommFab(Long cid);

    List<Fabulous> findInfoFabuTotal(Fabulous fabulous);

}
