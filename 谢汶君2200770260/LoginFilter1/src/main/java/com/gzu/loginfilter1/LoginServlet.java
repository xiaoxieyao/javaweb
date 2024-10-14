package com.gzu.loginfilter1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
用于将 LoginServlet 映射到 /login 路径。
即，当客户端向 /login 路径发出请求时，会由这个 LoginServlet 来处理请求。
*/
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        req.getParameter("username") 和 req.getParameter("password") 用于从 HttpServletRequest 对象中获取表单中 username 和 password 的值。
        这些参数通常来自登录页面的 HTML 表单
        */
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println("username: " + username +", password" + password);
        if("abc".equals(username) && "123456".equals(password)){
            /*
             这段代码将输入的 username 和 password 与预设的字符串 "abc" 和 "123456" 进行比较。
            如果用户名为 "abc" 且密码为 "123456"，则登录成功，并执行以下操作：
            将 username 存储在当前会话 (HttpSession) 中，键名为 "user"。
            使用 resp.sendRedirect("welcome.html") 重定向客户端到欢迎页面 welcome.html。
            如果用户名或密码不匹配，则登录失败，使用 resp.sendRedirect("login.html") 将用户重定向回登录页面 login.html。
            */
            req.getSession().setAttribute("user", username);
            resp.sendRedirect("welcome.html");
    }else {
            resp.sendRedirect("login.html");
        }
    }

}
