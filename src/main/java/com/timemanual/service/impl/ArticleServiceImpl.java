package com.timemanual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timemanual.dao.ArticleDao;
import com.timemanual.entity.Article;
import com.timemanual.service.ArticleService;
import com.timemanual.vo.PaginationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    @Autowired
    ArticleDao articleDao;

    @Override
    public PaginationVo queryArticle(Integer currentPage, Integer pageSize) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created_time");

        Page page = new Page(currentPage, pageSize);
        IPage<Article> iPage = articleDao.selectPage(page, queryWrapper);

        PaginationVo paginationVo = new PaginationVo();
        paginationVo.setCurrent(currentPage);
        paginationVo.setSize(pageSize);
        paginationVo.setTotal(iPage.getTotal());
        paginationVo.setData(iPage.getRecords());

        return paginationVo;
    }

    @Override
    public Boolean add(Article article) {
        return this.save(article);
    }

    @Override
    public Boolean edit(Article article) {
        try{
            UpdateWrapper<Article> updateWrapper =new UpdateWrapper<>();
            updateWrapper.eq("id", article.getId());
            return this.update(article,updateWrapper);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Article queryArticleById(String articleId) {
        try{

            if(articleId == null || articleId.equals("")){
                throw new RuntimeException("参数为空");
            }

            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",articleId);
            List<Article> list = articleDao.selectList(queryWrapper);
            if(list.isEmpty()){
                throw new RuntimeException("未查找到数据");
            }

            return  list.get(0);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Boolean deleteArticle(String articleId) {
        try{

            if(articleId == null || articleId.equals("")){
                throw new RuntimeException("参数为空");
            }

            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",articleId);

            int rows = articleDao.delete(queryWrapper);
            if(rows == 0){
                System.out.println("rowsA:"+rows);
                throw new RuntimeException("未查找删除到数据");
            }

            return true;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
