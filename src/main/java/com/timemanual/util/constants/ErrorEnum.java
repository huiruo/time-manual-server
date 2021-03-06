package com.timemanual.util.constants;

public enum ErrorEnum {
    E_400(400, "请求处理异常，请稍后再试"),
    E_401(401, "请登陆"),
    E_403(403, "权限不足"),
    E_500(500, "服务器内部错误"),
    E_501(501, "请求路径不存在"),
    E_502(502, "错误网关"),
    E_10008(10008, "角色删除失败,尚有用户属于此角色"),
    E_10009(10009, "账户已存在"),
    E_10010(10010, "密码错误"),
    E_10011(10012, "账号错误"),
    E_20011(20011, "登陆已过期,请重新登陆"),
    E_90003(90003, "缺少必填参数");

    private int errorCode;

    private String errorMsg;

    ErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
