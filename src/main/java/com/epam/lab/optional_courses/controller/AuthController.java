package com.epam.lab.optional_courses.controller;

import com.epam.lab.optional_courses.entity.User;
import com.epam.lab.optional_courses.service.SecurityService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(loadOnStartup = 1)
public class AuthController extends HttpServlet {

    public AuthController() {
    }

    private static final Logger log = LogManager.getLogger(AuthController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", Locale.US);
        request.setAttribute("title", "title.auth");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Login.jsp");
        requestDispatcher.forward(request, response);
        System.out.println("Authcontroller doGet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        doGet(request, response);
        Locale locale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
        System.out.println("Authcontroller doPost");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
        System.out.println(parameterNames);
        if (SecurityService.login(email, password)) {
            System.out.println("Login +");
            HttpSession session = request.getSession();
            String passwordHash = SecurityService.hash(password);
            User user = new User(SecurityService.getUserByCreds(email, passwordHash));
//            User user = SecurityService.getUserByCreds(email, passwordHash);                  //Maddening bug
//            User user = new User(999, "first_name", "last_name", "email", "pass", false);
//            System.out.println(SecurityService.getUserByCreds(email, passwordHash));
            session.setAttribute("user", user);
            session.setAttribute("locale", locale);
            response.sendRedirect("/user");  //Further redirect
        } else {
            System.out.println("Login -");
            request.setAttribute("ErrorMessage", bundle.getString("login.error"));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Login.jsp");
            dispatcher.forward(request, response);
        }
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
