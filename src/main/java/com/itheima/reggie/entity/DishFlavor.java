package com.itheima.reggie.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Description @Author dw @Date 2024-06-17
 */
@Data
public class DishFlavor implements Serializable {

  private static final long serialVersionUID = 5526665174436410175L;

  /** 主键 */
  private Long id;

  /** 菜品 */
  private Long dishId;

  /** 口味名称 */
  private String name;

  /** 口味数据list */
  private String value;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @TableField(fill = FieldFill.INSERT)
  private Long createUser;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Long updateUser;

  /** 是否删除 */
  private Integer isDeleted;
}
