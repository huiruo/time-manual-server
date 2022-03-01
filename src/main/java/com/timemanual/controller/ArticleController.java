package com.timemanual.controller;

import com.timemanual.entity.Article;
import com.timemanual.vo.ReqVo;
import com.timemanual.service.ArticleService;
import com.timemanual.vo.PaginationVo;
import com.timemanual.vo.PageParamVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ArticleController {

    @Autowired
    ArticleService articleService;

    // @RequiresPermissions("article:list")
    // @RequestMapping("/article/query")
    // @RequiresAuthentication
    @RequestMapping("/user/article/query")
    public ReqVo<PaginationVo> queryArticle(@RequestBody PageParamVo pageParamVo){
        PaginationVo paginationVo = articleService.queryArticle(pageParamVo.getCurrentPage(), pageParamVo.getPageSize());
        return new ReqVo<>(paginationVo);
    }

    // @RequestMapping("/query/id")
    // 不必带token也能请求到内容, 因为在shiro中配置了过滤规则
    @RequestMapping("/user/article/query/id")
    public ReqVo<Article> queryArticleByID(@RequestParam("id") String articleId){
        try{
            Article article = articleService.queryArticleById(articleId);
            return new ReqVo<>(article);
        }catch (Exception e){
            log.debug("queryArticleByID:{}",e.getMessage());
            return new ReqVo<>(0, e.getMessage());
        }
    }

    @RequestMapping("/article/add")
    public ReqVo<Boolean> addArticle(@RequestBody Article article){
        Boolean isSaveSucceed = articleService.add(article);
        if(isSaveSucceed){
            return new ReqVo<>(200, "发布文章成功");
        }else {
            return new ReqVo<>(0, "发布文章错误");
        }
    }

    @RequestMapping("/article/delete")
    public ReqVo<Boolean> deleteArticle(@RequestParam("id") String articleId){
        try{
            Boolean isSucceed = articleService.deleteArticle(articleId);
            if(isSucceed){
                return new ReqVo<>(200, "删除文章成功");
            }else {
                return new ReqVo<>(0, "删除文章失败");
            }
        }catch (Exception e){
            return new ReqVo<>(0, e.getMessage());
        }
    }

    @RequestMapping("/article/edit")
    public ReqVo<Boolean> editArticle(@RequestBody Article article){
        Boolean isSaveSucceed = articleService.edit(article);
        if(isSaveSucceed){
            return new ReqVo<>(200, "编辑文章成功");
        }else {
            return new ReqVo<>(0, "编辑文章错误");
        }
    }
}
