package com.timemanual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.timemanual.entity.Article;
import com.timemanual.vo.PaginationVo;

public interface ArticleService extends IService<Article> {
    PaginationVo queryArticle(Integer currentPage, Integer pageSize);
    Boolean add(Article article);
    Boolean edit(Article article);
    Article queryArticleById(String articleId);
    Boolean deleteArticle(String articleId);
}
