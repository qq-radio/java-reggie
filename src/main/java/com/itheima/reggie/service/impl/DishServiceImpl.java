package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
  @Autowired private DishFlavorService dishFlavorService;

  /**
   * qq：这里新增菜品，由于要同时操作2张表 -> dish、dish_flavor，因此必须加上@Transactional来保证事务的一致性
   * 我一开始的时候也没有加上@Transactional，然后就报莫名其妙的错，后来加上就可以了
   */
  @Transactional
  public void saveWithFlavors(DishDto dishDto) {
    this.save(dishDto);

    Long dishId = dishDto.getId();

    List<DishFlavor> flavors = dishDto.getFlavors();

    flavors =
        flavors.stream()
            .map(
                flavor -> {
                  flavor.setDishId(dishId);
                  return flavor;
                })
            .collect(Collectors.toList());

    dishFlavorService.saveBatch(flavors);
  }

  /** qq：千万要记住，只要是同时操作多张表的，一定要加上@Transactional */
  @Transactional
  public void updateWithFlavors(DishDto dishDto) {
    this.updateById(dishDto);

    /** qq：先把对应dishId的菜品口味dish_flavor清掉后，在更新菜品口味 */
    LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

    dishFlavorService.remove(lambdaQueryWrapper);

    /** qq：上面已经dishId对应的菜品口味dish_flavor，这里在把新的存进去 */
    List<DishFlavor> flavors = dishDto.getFlavors();
    flavors =
        flavors.stream()
            .map(
                f -> {
                  f.setDishId(dishDto.getId());
                  return f;
                })
            .collect(Collectors.toList());

    dishFlavorService.saveBatch(flavors);
  }

  public DishDto getWithFlavors(Long id) {
    Dish dish = this.getById(id);

    DishDto dishDto = new DishDto();
    BeanUtils.copyProperties(dish, dishDto);

    LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(DishFlavor::getDishId, id);

    List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);

    dishDto.setFlavors(flavors);

    return dishDto;
  }
}
