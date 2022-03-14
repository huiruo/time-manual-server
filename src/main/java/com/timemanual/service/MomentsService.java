package com.timemanual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.timemanual.entity.Moments;
import com.timemanual.vo.PaginationVo;

public interface MomentsService extends IService<Moments> {
   PaginationVo queryMoments(Integer pageNum, Integer pageSize);
   Boolean add(Moments moments);
}
