package com.epam.lab.optional_courses.controller;


import com.epam.lab.optional_courses.service.RegistrationService;
import com.epam.lab.optional_courses.service.SecurityService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Registration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegistrationController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(AuthController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", Locale.US);
        request.setAttribute("title", "title.reg");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Registration.jsp");
        requestDispatcher.forward(request, response);
        System.out.println("Registration doGet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Locale locale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
        System.out.println("Regcontroller doPost");
        String name = request.getParameter("name");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
        System.out.println(parameterNames);
        if (!email.isEmpty() &&
                !password.isEmpty()&&
                !name.isEmpty()&&
                !lastName.isEmpty()) {
            if (!RegistrationService.checkEmail(email)) {
                password = SecurityService.hash(password);
                if (!RegistrationService.insertUser(name, lastName, email, password)) {
                    System.out.println("Something went wrong");
                } else {
                    log.log(Level.INFO, "User registered");
                    response.sendRedirect("/auth");
                }
            } else {
                request.setAttribute("ErrorMessage", bundle.getString("reg.emailIsPresent"));
                RequestDispatcher dispatcher = request.getRequestDispatcher("/Registration.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("ErrorMessage", bundle.getString("reg.emptyFields"));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Registration.jsp");
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
