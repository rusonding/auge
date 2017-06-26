package com.auge.web.servlet;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by lixun on 2017/6/15.
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pwd = req.getParameter("pwd");
        String email = req.getParameter("email");
        System.out.println("pwd="+pwd+", email="+email);

        Map<String, String[]> parameterMap = req.getParameterMap();
        JSONObject obj = new JSONObject();
        obj.put("data", true);
        PrintWriter out = resp.getWriter();
        out.print(obj);
        out.close();
    }
}
