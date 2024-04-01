package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.Result;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
  public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    // 获取本次请求的uri
    String requestUri = request.getRequestURI();
    log.info("拦截到请求: {}", requestUri);

    // 定义不需要处理的请求路径
    String[] urls =
        new String[] {
          "/employee/login", "/employee/logout", "/front/**", "/backend/**",
        };

    // 判断本次请求是否需要处理
    boolean check = check(urls, requestUri);
    if (check) {
      log.info("本次请求{}不需要处理", requestUri);
      filterChain.doFilter(request, response);
      return;
    }

    // 判断登录状态，如果已登录，就放行
    if (request.getSession().getAttribute("employee") != null) {
      log.info("用户已登录，用户id为:{}", request.getSession().getAttribute("employee"));
      filterChain.doFilter(request, response);
      return;
    }

    log.info("用户未登录");
    response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));

    return;
  }

  public boolean check(String[] urls, String requestUri) {
    for (String url : urls) {
      boolean match = PATH_MATCHER.match(url, requestUri);
      if (match) {
        return true;
      }
    }
    return false;
  }
}
