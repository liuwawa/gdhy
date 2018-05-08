package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.CommentEntity;
import io.renren.modules.admin.entity.CommentFabulos;
import io.renren.modules.admin.entity.Fabulous;
import io.renren.modules.admin.entity.vo.CommAndFabulousVo;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by hc on 2018/3/21.
 */
@Mapper
public interface CommentDao extends BaseDao<CommentEntity>{

    void saveComment(CommentEntity usercomment);

    Fabulous queryFabulous(Fabulous fabulous);

    void saveFab(Fabulous fabulous);

    List<CommAndFabulousVo> queryCommend(CommAndFabulousVo vo);

    int queryCommentTotalByAid(String aid);

    int queryFabulousTotalByAid(String aid);

    void releaseBatch(Integer[] cids);

    CommentFabulos findCommFabByUserIdCid(CommentFabulos commentFabulos);

    void saveCommFab(CommentFabulos comfab);

    void addOneCommFab(Long cid);

    List<Fabulous> findInfoFabuTotal(Fabulous fabulous);

}
