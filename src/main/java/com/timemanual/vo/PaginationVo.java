package com.timemanual.vo;

import lombok.Data;

import java.util.List;

@Data
public class PaginationVo<T> {
    private Integer current;
    private Integer size;
    private Long total;
    private List<T> data;

    public PaginationVo() {
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
