package com.gzu.loginfilter1;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")  // 应用该过滤器于所有 URL 路径
public class LoginFilter implements Filter {
    // 定义静态资源扩展名列表
    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
           ".css", ".js", ".jpg", ".png", ".gif", ".ico", ".html", ".jsp", "/login"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 转换请求和响应为 HTTP 类型
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /*
         静态资源判断
         使用 anyMatch 来确定请求的 URI 是否包含其中的任何一个扩展名 之前错误写成all
        */
        boolean isStaticResource = STATIC_EXTENSIONS.stream().anyMatch(ext -> {
            System.out.println("ext:"+ext);
            return request.getRequestURI().contains(ext);
        });

        if (isStaticResource) {
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            /* 用户登录检查 */
            String username = (String)request.getSession().getAttribute("user");
             if( username == null) {
                 // 在实际情况下，不建议将空字符串作为有效的登录用户名，因此可以去掉 "" 的检查，仅保留 null。
                response.sendRedirect(request.getContextPath() + "/login.html");
            }else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();

    }
}
