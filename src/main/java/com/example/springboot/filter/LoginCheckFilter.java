package com.example.springboot.filter;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.support.json.JSONWriter;
import com.alibaba.fastjson.JSON;
import com.example.springboot.common.BaseContext;
import com.example.springboot.common.R;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String requestURI = request.getRequestURI();
        String[] urls = new String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**",
          "/common/**",
          "/user/senMsg",
          "/user/login"
        };
        boolean check=checkt(urls,requestURI);
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //1.1判断用户登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee")!=null) {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            Long id=(Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request,response);
            return;
        }
        //1.2判断用户登录状态，如果已登录，则直接放行（小程序端）
        if (request.getSession().getAttribute("user")!=null) {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            Long userId=(Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }

        //如果未登录则返回未登录信息
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    private Boolean checkt(String[] urls,String url){
        for (String val:urls){
            boolean match = PATH_MATCHER.match(val, url);
            if (match){
                return true;
            }
        }
        return false;
    }
}
