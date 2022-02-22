package com.timemanual.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
* 后台权限表
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission {
    private Integer id;
    // 归属菜单,前端判断并展示菜单使用
    private String menu_code;
    // 菜单的中文释义
    private String menu_name;
    // 权限的代码/通配符,对应代码中@RequiresPermissions 的value
    private String permission_code;
    // 本权限的中文释义
    private String permission_name;
    // 是否本菜单必选权限, 1.必选 2非必选 通常是"列表"权限是必选
    private Boolean required_permission;
    private Date create_time;
    private Date update_time;
}
