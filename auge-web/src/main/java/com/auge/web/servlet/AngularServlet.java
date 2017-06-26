package com.auge.web.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by lixun on 2017/6/16.
 */
public class AngularServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONObject obj = new JSONObject();
        obj.put("Name", "yiguo");
        obj.put("Url", "www.google.com");
        obj.put("Country", "USA");
        JSONArray array = new JSONArray();
        array.add(obj);

        obj = new JSONObject();
        obj.put("Name", "Facebook");
        obj.put("Url", "www.facebook.com");
        obj.put("Country", "CN");
        array.add(obj);
        JSONObject data = new JSONObject();
        data.put("sites", array);
        PrintWriter out = resp.getWriter();
        out.print(data);
        out.close();
    }
}
