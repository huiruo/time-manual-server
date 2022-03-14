package com.timemanual.controller;

import com.timemanual.entity.Moments;
import com.timemanual.vo.ReqVo;
import com.timemanual.service.MomentsService;
import com.timemanual.vo.PaginationVo;
import com.timemanual.vo.PageParamVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/moments")
@Slf4j
public class MomentsController {
    @Autowired
    MomentsService momentsService;

    @RequestMapping("/query")
    public ReqVo<PaginationVo> queryMoments(@RequestBody PageParamVo pageParamVo){
        PaginationVo paginationVo = momentsService.queryMoments(pageParamVo.getPageNum(), pageParamVo.getPageSize());
        return new ReqVo<>(paginationVo);
    }

    @RequestMapping("/add")
    public ReqVo<Boolean> addMoments(@RequestBody Moments moment){
        Boolean isSaveSucceed = momentsService.add(moment);
        if(isSaveSucceed){
            return new ReqVo<>(200, "发布成功");
        }else {
            return new ReqVo<>(0, "发布错误");
        }
    }
}
