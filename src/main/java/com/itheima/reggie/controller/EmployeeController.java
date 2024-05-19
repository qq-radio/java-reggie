package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Result;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired private EmployeeService employeeService;

  @PostMapping("/login")
  public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

    // 1、将页面提交的密码password进行md5加密处理
    String password = employee.getPassword();
    password = DigestUtils.md5DigestAsHex(password.getBytes());

    // 2、根据页面提交的用户名username查询数据库
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Employee::getUsername, employee.getUsername());
    Employee emp = employeeService.getOne(queryWrapper);

    // 3、如果没有查询到则返回登录失败结果
    if (emp == null) {
      return Result.error("账号不存在");
    }

    // 4、密码比对，如果不一致则返回登录失败结果
    if (!emp.getPassword().equals(password)) {
      return Result.error("密码错误");
    }

    // 5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
    if (emp.getStatus() == 0) {
      return Result.error("账号已禁用");
    }

    // 6、登录成功，将员工id存入Session并返回登录成功结果
    request.getSession().setAttribute("employee", emp.getId());
    return Result.success(emp);
  }

  @PostMapping("/logout")
  public Result<String> logout(HttpServletRequest request) {
    request.getSession().removeAttribute("employee");
    return Result.success("退出登录成功");
  }

  @GetMapping("/page")
  public Result<Page> page(int page, int pageSize, String name) {
    Page p = new Page(page, pageSize);

    LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
    lambdaQueryWrapper.like(StringUtils.isNotBlank(name), Employee::getName, name);
    lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

    employeeService.page(p, lambdaQueryWrapper);

    return Result.success(p);
  }

  @PostMapping
  public Result<String> add(HttpServletRequest request, @RequestBody Employee employee) {
    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

    //    采用MyMetaObjecthandler自动填充
    //    employee.setCreateTime(LocalDateTime.now());
    //    employee.setUpdateTime(LocalDateTime.now());
    //
    //    Long empId = (Long) request.getSession().getAttribute("employee");
    //
    //    employee.setCreateUser(empId);
    //    employee.setUpdateUser(empId);

    employeeService.save(employee);

    return Result.success("新增成功了");
  }

  @GetMapping("/{id}")
  public Result<Employee> query(@PathVariable Long id) {
    Employee employee = employeeService.getById(id);

    if (employee != null) {
      return Result.success(employee);
    }

    return Result.error("没有查到员工信息");
  }

  @PutMapping
  public Result<String> update(HttpServletRequest request, @RequestBody Employee employee) {
    //    采用MyMetaObjecthandler自动填充
    //    employee.setUpdateTime(LocalDateTime.now());
    //
    //    Long empId = (Long) request.getSession().getAttribute("employee");
    //    employee.setUpdateUser(empId);

    employeeService.updateById(employee);

    return Result.success("更新成成功了");
  }
}
