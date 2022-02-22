package com.timemanual.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageParamVo {
    //  description = "页码", defaultValue =  1
    private Integer currentPage = 1;

    //	description = "页数", defaultValue = 10
    private Integer pageSize = 10;

    private Integer totalCount = 0;

    //	description = "排序", example = "id desc"
    //    private String orderBy;
}
