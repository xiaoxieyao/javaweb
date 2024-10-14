# 第三次作业Listener
谢汶君2200770260
### RequestListener代码
```
package com.gzu.rl;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import jakarta.servlet.http.HttpServletRequest; // 导入 HttpServletRequest 类
import java.util.logging.Logger;

@WebListener
public class RequestListener implements ServletContextListener, HttpSessionListener, ServletRequestListener {

    private static final Logger logger = Logger.getLogger(RequestListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("totalVisitors", 0);
        sce.getServletContext().setAttribute("currentVisitors", 0);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 关闭应用程序时执行的逻辑
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int totalVisitors = (int) se.getSession().getServletContext().getAttribute("totalVisitors") + 1;
        int currentVisitors = (int) se.getSession().getServletContext().getAttribute("currentVisitors") + 1;
        se.getSession().getServletContext().setAttribute("totalVisitors", totalVisitors);
        se.getSession().getServletContext().setAttribute("currentVisitors", currentVisitors);
        System.out.println("网站访问量: " + totalVisitors + ", 在线人数: " + currentVisitors);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int totalVisitors = (int) se.getSession().getServletContext().getAttribute("totalVisitors");
        int currentVisitors = (int) se.getSession().getServletContext().getAttribute("currentVisitors") - 1;
        se.getSession().getServletContext().setAttribute("currentVisitors", currentVisitors);
        System.out.println("网站访问量: " + totalVisitors + ", 在线人数: " + currentVisitors);
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        // 将 ServletRequest 强制转换为 HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        // 获取并记录请求的相关信息
        String clientIp = request.getRemoteAddr();
        String requestMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString() == null ? "" : request.getQueryString();
        String userAgent = request.getHeader("User-Agent");

        // 打印日志记录请求的详细信息
        logger.info(String.format("请求信息: [请求时间: %d, 客户端 IP: %s, 请求方法: %s, 请求 URI: %s, 查询字符串: %s, User-Agent: %s]",
                startTime, clientIp, requestMethod, requestUri, queryString, userAgent));
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // 将 ServletRequest 强制转换为 HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        // 获取请求的开始时间
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        // 记录处理时间
        logger.info(String.format("请求处理完成, 处理时间: %d 毫秒", processingTime));
    }
}

```
### TestServlet代码
```
package com.gzu.rl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/testListener")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应内容类型
        resp.setContentType("text/html;charset=UTF-8");

        // 获取请求开始时间（由监听器设置）
        long startTime = (long) req.getAttribute("startTime");

        // 获取请求结束时间并计算处理时间
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        // 获取请求相关信息
        String clientIp = req.getRemoteAddr();
        String requestMethod = req.getMethod();
        String requestUri = req.getRequestURI();
        String queryString = req.getQueryString() == null ? "" : req.getQueryString();
        String userAgent = req.getHeader("User-Agent");

        // 向客户端返回详细的请求信息
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Request Listener Test</h1>");
        out.println("<ul>");
        out.println("<li>请求时间: " + startTime + "</li>");
        out.println("<li>客户端 IP 地址: " + clientIp + "</li>");
        out.println("<li>请求方法: " + requestMethod + "</li>");
        out.println("<li>请求 URI: " + requestUri + "</li>");
        out.println("<li>查询字符串: " + queryString + "</li>");
        out.println("<li>User-Agent: " + userAgent + "</li>");
        out.println("<li>请求处理时间: " + processingTime + " 毫秒</li>");
        out.println("</ul>");
        out.println("</body></html>");


        // 关闭输出流
        out.close();
    }
}
```
#### 最终页面显示如下
![alt text](image.png)
#### mvn-clean
![alt text](image-1.png)