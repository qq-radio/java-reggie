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
public class Setmeal implements Serializable {

  private static final long serialVersionUID = 9186278974943540856L;

  /** 主键 */
  private Long id;

  /** 菜品分类id */
  private Long categoryId;

  /** 套餐名称 */
  private String name;

  /** 套餐价格 */
  private BigDecimal price;

  /** 状态 0:停用 1:启用 */
  private Integer status;

  /** 编码 */
  private String code;

  /** 描述信息 */
  private String description;

  /** 图片 */
  private String image;

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
