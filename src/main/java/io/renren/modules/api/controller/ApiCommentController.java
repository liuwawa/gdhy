package io.renren.modules.api.controller;

import io.renren.common.utils.R;

import io.renren.common.utils.TestScan.TextAntispamScanSample;
import io.renren.common.validator.Assert;
import io.renren.modules.admin.entity.CommentFabulos;
import io.renren.modules.api.annotation.Login;
import io.renren.modules.api.annotation.LoginUser;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.admin.entity.CommentEntity;
import io.renren.modules.admin.entity.Fabulous;
import io.renren.modules.admin.entity.vo.CommAndFabulousVo;
import io.renren.modules.admin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论管理
 * Created by hc on 2018/3/21.
 */
@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {

    //评论服务接口
    @Autowired
    private CommentService commentService;



    /**
     * 保存提交的评论
     * @param aid
     * @param comment
     * @return
     */
    @Login
    @PostMapping(value="/save")
    public R save(@RequestParam String aid,
                  @RequestParam String comment,
                  @LoginUser UserEntity user) throws Exception{

        Assert.isBlank(comment,"不能提交空评论");


//        String label = TextAntispamScanSample.TextScan(comment);
//        if(label.equals("spam")){
//            return R.error("你提交的内容含有垃圾信息");
//        }else if(label.equals("ad")){
//            return R.error("你提交的内容不能含有广告信息");
//        }else if(label.equals("politics") || label.equals("terrorism") || label.equals("abuse") || label.equals("porn") || label.equals("customized")){
//            return R.error("你提交的内容包含非法信息");
//        }else if(label.equals("flood")){
//            return R.error("你提交的内容存在灌水信息");
//        }else if(label.equals("contraband")){
//            return R.error("你提交的内容存在违禁信息");
//        }

        CommentEntity usercomment = new CommentEntity();

        usercomment.setAid(aid);
        usercomment.setUid(user.getUserId());
        usercomment.setuName(user.getUsername());
        usercomment.setHeadImg(user.getAvatar());
        usercomment.setComment(comment);

        commentService.saveComment(usercomment);

        return R.ok("评论成功");
    }



    /**
     * 移动端获取评论
     */
    @PostMapping(value="/getCommAndFabulous")
    public R getCommAndFabulous(@RequestParam String aid,
                                Integer page,
                                Integer size,
                                 @RequestAttribute(required = false) Long userId){

        CommAndFabulousVo vo = new CommAndFabulousVo();
        vo.setAid(aid);
        vo.setPage(page);
        vo.setSize(size);
        if(userId != null){
            vo.setUid(userId);
        }

        Map<String,Object> commentAndFabulous = new HashMap<>();
        //查找该文章下的所有评论和点赞数
        List<CommAndFabulousVo> commentList = commentService.queryCommend(vo);

        //查找该文章下的评论条数
        int commentTotal = commentService.queryCommentTotalByAid(aid);
        //查找该文章下的点赞数
        int fabTotal = commentService.queryFabulousTotalByAid(aid);

        commentAndFabulous.put("commentList",commentList);
        commentAndFabulous.put("commentTotal",commentTotal);
        commentAndFabulous.put("fabTotal",fabTotal);
        return R.ok().put("commentAndFabulous",commentAndFabulous);
    }


    /**
     * WEB网页获取评论
     */
    @CrossOrigin
    @PostMapping(value="/findCommAndFabulous")
    public R findCommAndFabulous(@RequestParam String aid,
                                 Integer page,
                                 Integer size){
        return getCommAndFabulous(aid,page,size,null);
    }

    /**
     * 保存对某条评论的点赞
     */
    @Login
    @PostMapping(value="/saveCommentFabulos")
    public R saveCommentFabulos(@RequestParam long cid,@RequestParam String aid,@LoginUser UserEntity user){



        CommentFabulos comfab = new CommentFabulos();
        comfab.setCid(cid);
        comfab.setUid(user.getUserId());
        comfab.setAid(aid);
        comfab.setFlag(true);
        //判断之前是否已经点过赞
        CommentFabulos commentFabulos = commentService.findCommFabByUserIdCid(comfab);
        if(commentFabulos != null){
           return R.error("你已经点赞过了");
        }
        //保存对该评论的点赞
        commentService.saveCommFab(comfab);
        //对该评论下的点赞数加1
        commentService.addOneCommFab(cid);

        return R.ok("点赞成功");
    }





    /**
     * 保存资讯点赞
     * @return
     */
    @Login
    @PostMapping(value="/saveFab")
    public R saveFab(@LoginUser UserEntity user,@RequestParam String aid){

        Fabulous fabulous = new Fabulous();
        fabulous.setUid(user.getUserId());
        fabulous.setAid(aid);
        Fabulous fabulousEntity = commentService.queryFabulous(fabulous);
        if(fabulousEntity != null){
            return R.error("你已经点赞了");
        }

        commentService.saveFab(fabulous);
        return R.ok("已点赞");
    }

    /**
     * 获取资讯点赞总数,暂时不需要
     */
    @CrossOrigin
    @PostMapping(value="/findInfoFabuTotal")
    public R findInfoFabuTotal(@RequestParam String aid,@LoginUser UserEntity user){

        Fabulous fabulous = new Fabulous();
        fabulous.setAid(aid);
        if(user != null){
            fabulous.setUid(user.getUserId());
        }
        List<Fabulous> fabulousList = commentService.findInfoFabuTotal(fabulous);
        return R.ok();
    }


}
