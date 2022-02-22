package com.timemanual.vo;

import lombok.Data;

@Data
public class ReqVo<T> {
    private T data;
    private int code;
    private String msg;
    private static final int constCode = 200;

    /**
     * 若没有数据返回，默认状态码为 200，提示信息为“操作成功！”
     */
    public ReqVo() {
        this.code = constCode;
        this.msg = "操作成功！";
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信:错误
     *
     * @param code
     * @param msg
     */
    public ReqVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     *
     * @param data
     */
    public ReqVo(T data) {
        this.data = data;
        this.code = constCode;
        this.msg = "操作成功！";
    }

    /**
     * 有数据返回，状态码为 0
     *
     * @param data
     * @param msg
     */
    public ReqVo(T data, String msg) {
        this.data = data;
        this.code = constCode;
        this.msg = msg;
    }
    // 省略 get 和 set 方法:lombok代替
}
