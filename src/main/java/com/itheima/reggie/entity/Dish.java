package com.itheima.reggie.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description @Author dw @Date 2024-05-21
 */
@Data
public class Dish implements Serializable {

  private static final long serialVersionUID = 4582763725601262228L;

  /** 主键 */
  private Long id;

  /** 菜品名称 */
  private String name;

  /** 菜品分类id */
  private Long categoryId;

  /** 菜品价格 */
  private BigDecimal price;

  /** 商品码 */
  private String code;

  /** 图片 */
  private String image;

  /** 描述信息 */
  private String description;

  /** 0 停售 1 起售 */
  private Integer status;

  /** 顺序 */
  private Integer sort;

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
