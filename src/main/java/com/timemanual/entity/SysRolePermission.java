package com.timemanual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
* 角色-权限关联表
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRolePermission {
    private Integer id;
    // 角色id
    private Integer role_id;
    // 权限id
    private String permission_id;
    // 是否有效 1有效 2无效
    private String delete_status;
    private Date create_time;
    private Date update_time;
}
