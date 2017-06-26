package com.auge.web.server;

import com.auge.web.servlet.AngularServlet;
import com.auge.web.servlet.JobListServlet;
import com.auge.web.servlet.LoginServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by lixun on 2017/6/15.
 */
public class WebServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(DefaultServlet.class, "/");
        servletContextHandler.setResourceBase("auge-web/webapp");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet()), "/loginServlet");
        servletContextHandler.addServlet(new ServletHolder(new AngularServlet()), "/angularServlet");
        servletContextHandler.addServlet(new ServletHolder(new JobListServlet()), "/jobListServlet");
        server.setHandler(servletContextHandler);
        server.start();
    }
}
