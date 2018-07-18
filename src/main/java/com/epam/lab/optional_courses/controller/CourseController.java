package com.epam.lab.optional_courses.controller;


import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.epam.lab.optional_courses.service.CourseService.*;

@WebServlet(loadOnStartup = 1)
public class CourseController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CourseController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //System.out.println("CONTROLLER STARTED" + new Date() );
        User curUser = (User) request.getSession().getAttribute("user");
        if(curUser==null){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
            requestDispatcher.forward(request, response);
        }
        Locale locale = (Locale)request.getSession().getAttribute("locale");
        curUser = getUserById("41"); //current logined user
        request.setAttribute("locale", locale);
        String pathInfo = request.getPathInfo();
        long offset = 0L;
        String offsetStr;
        if (pathInfo == null) {
            offsetStr = request.getParameter("offset");
            if (offsetStr != null) {
                try {
                    offset = Long.parseLong(offsetStr);
                } catch (NumberFormatException ignored) {
                }
            }
            //System.out.println("STARTED DAO" + new Date() );
            List<Course> allCourses = getAllCourses(Common.limit, offset);
            //System.out.println("getAllCourses()" + new Date() );
            List<Boolean> coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(curUser, allCourses);
            //System.out.println("getCoursesEnrolledByCurUser()" + new Date() );
            request.setAttribute("countEntity", countCourses());
            request.setAttribute("entityType", Common.EntityType.COURSE);
            request.setAttribute("list", allCourses);
            request.setAttribute("curUser", curUser);
            request.setAttribute("offset", offset);
            request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

            //System.out.println("DATA WENT TO JSP" + new Date() );
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("list.jsp");
            requestDispatcher.forward(request, response);
        } else {
            String[] splitedPath = pathInfo.toLowerCase().split("/");
            Course course;
            switch (splitedPath.length) {
                case 0:
                    offsetStr = request.getParameter("offset");
                    if (offsetStr != null) {
                        try {
                            offset = Long.parseLong(offsetStr);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    List<Course> allCourses = getAllCourses(Common.limit, offset);
                    List<Boolean> coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(curUser, allCourses);
                    request.setAttribute("countEntity", countCourses());
                    request.setAttribute("entityType", Common.EntityType.COURSE);
                    request.setAttribute("list", allCourses);
                    request.setAttribute("curUser", curUser);
                    request.setAttribute("offset", offset);
                    request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

                    System.out.println("DATA WENT TO JSP" + new Date());
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("src/main/webapp/list.jsp");
                    requestDispatcher.forward(request, response);
                    break;
                case 2:
                    course = getCourseById(splitedPath[1]);
                    if (course != null) {
                        if (curUser.equals(course.getTutor())) {
                            //show COURSE TO TUTOR
                        } else if (isUserOnCourse(curUser, course)) {
                            //show course to user enrolled on course

                        } else {
                            // SHOW COURSE to not enrolled user
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;
                case 4:
                    course = getCourseById(splitedPath[1]);
                    if (course != null) {
                        if (splitedPath[2].equals("getfeedback")) {
                            User givenUser = getUserById(splitedPath[3]);
                            if (givenUser != null) {
                                Feedback feedback = getFeedbackByUserAndCourse(curUser, course);
                                if (feedback != null) {
                                    if (curUser.equals(givenUser)) {
                                        // SHOW FEEDBACK TO USER
                                    } else if (curUser.equals(course.getTutor())) {
                                        // SHOW FEEDBACK TO TUTOR
                                    } else {

                                    }
                                } else {
                                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                                }

                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }

                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        }

        // response.getWriter().println("Hello World!");  PUT HTML BODY HERE
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public void init() throws ServletException {
        log.log(Level.INFO, "Servlet " + this.getServletName() + " has started");
    }

    @Override
    public void destroy() {
        log.log(Level.INFO, "Servlet " + this.getServletName() + " has stopped");
    }

    /*private List<Course> show() {

    }*/

}
