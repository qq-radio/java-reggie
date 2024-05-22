package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

  @Autowired DishService dishService;

  @Autowired SetmealService setmealService;

  @Override
  public void remove(Long id) {
    LambdaQueryWrapper<Dish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
    lambdaQueryWrapper1.eq(Dish::getCategoryId, id);
    int dn = dishService.count(lambdaQueryWrapper1);

    if (dn > 0) {
      throw new CustomException("关联了dish 菜品那张表，不能删，加油，其实只是时间问题  你本来就想做， 但你一点要去坚持");
    }

    LambdaQueryWrapper<Setmeal> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
    lambdaQueryWrapper2.eq(Setmeal::getCategoryId, id);
    int sn = setmealService.count(lambdaQueryWrapper2);

    if (sn > 0) {
      throw new CustomException("关联了 setmeal  套餐表 ，不能删，加油 ！！！加油");
    }

    super.removeById(id);
  }
}
