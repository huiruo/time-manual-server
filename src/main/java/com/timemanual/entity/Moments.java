package com.timemanual.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "moments")
public class Moments implements Serializable {
    private Integer id;
    private String content;
    @TableField(value = "share_url",fill = FieldFill.INSERT_UPDATE)
    private String shareUrl;
    @TableField(value = "img_url",fill = FieldFill.INSERT_UPDATE)
    private String imgUrl;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 例如：@TableField(.. , update="%s+1") 其中 %s 会填充为字段
    // 输出 SQL 为：update 表 set 字段=字段+1 where ...
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_time",fill = FieldFill.INSERT_UPDATE)
    private Date createdTime;
}
