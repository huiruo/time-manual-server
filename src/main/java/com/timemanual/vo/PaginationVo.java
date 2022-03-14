package com.timemanual.vo;

import lombok.Data;

import java.util.List;

@Data
public class PaginationVo<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private List<T> result;

    public PaginationVo() {
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
