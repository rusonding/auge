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
 * Created by lixun on 2017/6/15.
 */
public class JobListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject obj = new JSONObject();
        obj.put("id", 1);
        obj.put("name", "test");
        obj.put("date", "2017-06-16");
        obj.put("status", "ok");
        JSONArray array = new JSONArray();
        array.add(obj);

        obj = new JSONObject();
        obj.put("id", 2);
        obj.put("name", "google");
        obj.put("date", "2017-06-17");
        obj.put("status", "error");
        array.add(obj);

        obj = new JSONObject();
        obj.put("id", 3);
        obj.put("name", "facebook");
        obj.put("date", "2017-06-14");
        obj.put("status", "error");
        array.add(obj);

        obj = new JSONObject();
        obj.put("id", 4);
        obj.put("name", "baidu");
        obj.put("date", "2017-06-10");
        obj.put("status", "ok");
        array.add(obj);

        JSONObject data = new JSONObject();
        data.put("result", array);
        PrintWriter out = resp.getWriter();
        out.print(data);
        out.close();
    }
}
