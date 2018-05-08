package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.FaqEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问题列表
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-20 16:43:30
 */
@Mapper
public interface FaqDao extends BaseDao<FaqEntity> {

    List<FaqEntity> queryByType(@Param("type") Integer type,@Param("a") Integer a,@Param("b")  Integer b);
}
