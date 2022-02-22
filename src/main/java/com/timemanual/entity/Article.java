package com.timemanual.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "article")
public class Article implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private String tag;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableField(value = "created_time",fill = FieldFill.INSERT_UPDATE)
    private Date createdTime;
}
