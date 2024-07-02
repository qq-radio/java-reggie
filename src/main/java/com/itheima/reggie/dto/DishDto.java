package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
  /**
   * dto, data transfer object 数据传输对象，一般用于展示层与服务层之间的数据传输 或者直接说前端发送接口，dto就用来接收传过来的这些入参
   *
   * <p>如果接口的参数与实体类entity属性一一对应就不需要dto了，否则就需要
   */
  private List<DishFlavor> flavors = new ArrayList<>();

  private String categoryName;
}
