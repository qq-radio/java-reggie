package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Result;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/dish")
@RestController
public class DishController {
  @Autowired private DishService dishService;

  @Autowired private CategoryService categoryService;

  @GetMapping("/page")
  public Result<Page> page(int page, int pageSize, String name) {

    Page<Dish> dishPage = new Page<>(page, pageSize);
    Page<DishDto> dishDtoPage = new Page<>();

    LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.like(name != null, Dish::getName, name);
    lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

    dishService.page(dishPage, lambdaQueryWrapper);

    /**
     * copyProperties(Object source, Object target, String... ignoreProperties) 用法是：Copy the
     * property values of the given source bean into the given target bean, ignoring the given
     * "ignoreProperties".
     */
    BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

    List<Dish> records = dishPage.getRecords();

    List<DishDto> list =
        records.stream()
            .map(
                item -> {
                  DishDto dishDto = new DishDto();
                  BeanUtils.copyProperties(item, dishDto);

                  Long categoryId = item.getCategoryId();
                  Category category = categoryService.getById(categoryId);

                  /**
                   * 可能一开始会觉得这里判断category != null是不是多此一举
                   * 但其实不是，因为有可能dish还关联着category，而category那里已经把它删了
                   * 那这时候category.getName就会报错，相当于前端的null.name取不到值 所以这里的category != null非常有必要
                   */
                  if (category != null) {
                    dishDto.setCategoryName(category.getName());
                  }

                  return dishDto;
                })
            .collect(Collectors.toList());

    dishDtoPage.setRecords(list);

    return Result.success(dishDtoPage);
  }

  @PostMapping
  public Result<String> save(@RequestBody DishDto dishDto) {
    dishService.saveWithFlavors(dishDto);
    return Result.success("新增成功了");
  }

  @PutMapping
  public Result<String> update(@RequestBody DishDto dishDto) {
    dishService.updateWithFlavors(dishDto);
    return Result.success("编辑了，成功了");
  }

  @GetMapping("/{id}")
  public Result<DishDto> get(@PathVariable Long id) {
    DishDto result = dishService.getWithFlavors(id);
    return Result.success(result);
  }

  @PostMapping("/status/{status}")
  public Result<String> updateStatus(@PathVariable int status, @RequestParam String ids) {
    List<Long> idList =
        Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

    /** qq：gpt告诉我这段代码实现没有问题，但是for多次操作数据库，性能上是不好的，但我也不知道咋搞啊 */
    for (Long id : idList) {
      Dish dish = new Dish();
      dish.setStatus(status);
      dish.setId(id);
      dishService.updateById(dish);
    }

    return Result.success("成功");
  }

  @DeleteMapping
  public Result<String> delete(@RequestParam String ids) {
    List<Long> idList =
        Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

    dishService.removeByIds(idList);

    return Result.success("现在 是删除成功的提升");
  }
}
