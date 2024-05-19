package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLIntegrityConstraintViolationException;

/** 全局异常处理 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

  /**
   * 异常处理方法
   *
   * @return
   */
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
    log.error("错误拦截，打印在控制台看，我 {}", ex.getMessage());

    if (ex.getMessage().contains("Duplicate entry")) {
      String[] split = ex.getMessage().split(" ");
      String msg = split[2] + "已存在";
      return Result.error(msg);
    }

    if (ex.getMessage().contains("cannot be null")) {
      String[] split = ex.getMessage().split(" ");
      String msg = split[1] + "不能为空";
      return Result.error(msg);
    }

    return Result.error("未知错误，现在测试这里应该是会不会在这里拦截到错误");
  }

  /**
   * 异常处理方法
   *
   * @return
   */
  @ExceptionHandler(CustomException.class)
  public Result<String> exceptionHandler(CustomException ex) {
    log.error("自定义的错误拦截 看下，我 {}", ex.getMessage());

    return Result.error(ex.getMessage());
  }
}
