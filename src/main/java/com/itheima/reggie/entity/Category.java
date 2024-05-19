package com.itheima.reggie.entity;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;


/**
 * @Description  
 * @Author  dw
 * @Date 2024-05-19 
 */
@Data
public class Category implements Serializable {

	private static final long serialVersionUID =  6151618960822170453L;

	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 类型   1 菜品分类 2 套餐分类
	 */
	private Integer type;

	/**
	 * 分类名称
	 */
	private String name;

	/**
	 * 顺序
	 */
	private Integer sort;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 创建人
	 */
	private Integer createUser;

	/**
	 * 修改人
	 */
	private Integer updateUser;

}
