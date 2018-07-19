package com.epam.lab.optional_courses.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

public class MainController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(MainController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String loc = request.getParameter("lang");
        String referer = request.getHeader("referer");
        System.out.println(loc);

        HttpSession session = request.getSession(false);
        boolean flag = false;
        Locale locale;
        if (loc == null) {
            if (session == null) {
                response.sendRedirect("/auth");
            } else {
                response.sendRedirect("/user");
            }
        } else {
            switch (loc) {
                case "ru": {
                    locale = Locale.US;
                    break;
                }
                case "en": {
                    locale = new Locale("ru");
                    break;
                }
                default: {
                    locale = Locale.US;
                }
            }
            session.setAttribute("locale", locale);
            response.sendRedirect(referer);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("Hello World!");
    }

    @Override
    public void init() throws ServletException {
        log.log(Level.INFO, "Servlet " + this.getServletName() + " has started");
    }

    @Override
    public void destroy() {
        log.log(Level.INFO, "Servlet " + this.getServletName() + " has stopped");
    }

}
