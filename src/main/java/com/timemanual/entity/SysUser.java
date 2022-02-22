package com.timemanual.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
* 用户表
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    // 角色ID
    private Integer role_id;
    // 是否有效  1有效  2无效
    private String delete_status;
    private Date create_time;
    private Date update_time;
}
