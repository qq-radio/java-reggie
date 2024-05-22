package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Result;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/category")
@RestController
public class CategoryController {
  @Autowired CategoryService categoryService;

  @GetMapping("/page")
  public Result<Page> page(int page, int pageSize) {
    Page<Category> p = new Page<>(page, pageSize);

    LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.orderByAsc(Category::getSort);

    categoryService.page(p, lambdaQueryWrapper);

    return Result.success(p);
  }

  @PostMapping
  public Result<String> add(@RequestBody Category category) {
    categoryService.save(category);
    return Result.success("add成功 加油");
  }

  @PutMapping
  public Result<String> update(@RequestBody Category category) {
    categoryService.updateById(category);
    return Result.success("edit应该也成功了，一定要加油");
  }

  @DeleteMapping
  public Result<String> delete(Long id) {
    categoryService.remove(id);
    return Result.success("删除成功 这次");
  }
}
