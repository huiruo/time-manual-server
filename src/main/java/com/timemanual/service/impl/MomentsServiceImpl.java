package com.timemanual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timemanual.dao.MomentsDao;
import com.timemanual.entity.Moments;
import com.timemanual.service.MomentsService;
import com.timemanual.vo.PaginationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MomentsServiceImpl  extends ServiceImpl<MomentsDao, Moments> implements MomentsService {

    @Autowired
    MomentsDao momentsDao;

    @Override
    public PaginationVo queryMoments(Integer pageNum, Integer pageSize) {
        PaginationVo paginationVo = new PaginationVo<>();
        paginationVo.setPageNum(pageNum);
        paginationVo.setPageSize(pageSize);

        QueryWrapper<Moments> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created_time");

        // 指定排序
        Page page = new Page(pageNum, pageSize);
        IPage<Moments> iPage = momentsDao.selectPage(page, queryWrapper);
        // 不指定排序
        // IPage<Moments> iPage = momentsDao.selectPage(page, new QueryWrapper<Moments>());

        paginationVo.setTotal(iPage.getTotal());
        paginationVo.setResult(iPage.getRecords());

        return paginationVo;
    }

    @Override
    public Boolean add(Moments moments) {
        return this.save(moments);
    }
}
