package com.timemanual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
* 后台角色表
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole {
    private Integer id;
    // 角色名
    private String role_name;
    // 是否有效  1有效  2无效
    private String delete_status;
    private Date create_time;
    private Date update_time;
}
