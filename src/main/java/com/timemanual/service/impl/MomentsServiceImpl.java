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
    public PaginationVo queryMoments(Integer currentPage, Integer pageSize) {
        /*
        如果没有添加分页插件，selectPage方法也可以代用。
        但是会把数据数据全部返回。getPage和getTotal获取到总页数和总记录数的值都是0。
        IPage<Moments> page = new Page<>(currentPage, pageSize);
        MomentsVo momentsVo = new MomentsVo();
        IPage<Moments> iPage = momentsDao.selectPage(page,null);

        momentsVo.setCurrent(currentPage);
        momentsVo.setSize(pageSize);
        System.out.println("总页数： "+iPage.getPages());
        System.out.println("总页数： "+iPage.getTotal());
        momentsVo.setTotal(iPage.getTotal());
        momentsVo.setMomentsList(iPage.getRecords());
        */
        Page page = new Page(currentPage, pageSize);
        PaginationVo paginationVo = new PaginationVo<>();
        // 不指定排序
        // IPage<Moments> iPage = momentsDao.selectPage(page, new QueryWrapper<Moments>());

        QueryWrapper<Moments> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created_time");
        IPage<Moments> iPage = momentsDao.selectPage(page, queryWrapper);

        paginationVo.setCurrent(currentPage);
        paginationVo.setSize(pageSize);
        paginationVo.setTotal(iPage.getTotal());
        paginationVo.setData(iPage.getRecords());

        return paginationVo;
    }

    @Override
    public Boolean add(Moments moments) {
        return this.save(moments);
    }
}
