package com.epam.lab.optional_courses.controller;


import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;
import com.epam.lab.optional_courses.service.components.EntryKV;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.epam.lab.optional_courses.service.CourseService.*;
import static com.epam.lab.optional_courses.service.FeedbackService.deleteFeedback;
import static com.epam.lab.optional_courses.service.FeedbackService.getFeedbackForUsersOnCourse;

@WebServlet(loadOnStartup = 1)
public class CourseController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CourseController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //System.out.println("CONTROLLER STARTED" + new Date() );
//        User curUser = (User) request.getSession().getAttribute("user");
//        if(curUser==null){
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
//            requestDispatcher.forward(request, response);
//        }
        //Locale locale = (Locale)request.getSession().getAttribute("locale");
        User curUser = getUserById("41"); //current logined user
        //request.setAttribute("locale", locale);
        Locale locale = Locale.US;
        request.setAttribute("locale", locale);
        request.setAttribute("curUser", curUser);
        String pathInfo = request.getPathInfo();
        long offset = 0L;
        String offsetStr = request.getParameter("offset");
        if (offsetStr != null) {
            try {
                offset = Long.parseLong(offsetStr);
            } catch (NumberFormatException ignored) {
            }
        }
        request.setAttribute("offset", offset);
        List<EntryKV> entries = new ArrayList<>();
        RequestDispatcher requestDispatcher = null;
        if (pathInfo == null) {
            //System.out.println("STARTED DAO" + new Date() );
            List<Course> allCourses = getAllCourses(Common.limit, offset);
            //System.out.println("getAllCourses()" + new Date() );
            List<Boolean> coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(curUser, allCourses);
            //System.out.println("getCoursesEnrolledByCurUser()" + new Date() );
            request.setAttribute("countEntity", countCourses());
            request.setAttribute("entityType", Common.EntityType.COURSE);
            request.setAttribute("list", allCourses);
            request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

            //System.out.println("DATA WENT TO JSP" + new Date() );
            request.setAttribute("title", "title.table");
            requestDispatcher = request.getRequestDispatcher("/table.jsp");
            requestDispatcher.forward(request, response);
        } else {
            String[] splitedPath = pathInfo.toLowerCase().split("/");
            System.out.println(pathInfo);
            System.out.println(splitedPath.length);
            Course course;
            switch (splitedPath.length) {
                case 0:
                    //System.out.println("STARTED DAO" + new Date() );
                    List<Course> allCourses = getAllCourses(Common.limit, offset);
                    //System.out.println("getAllCourses()" + new Date() );
                    List<Boolean> coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(curUser, allCourses);
                    //System.out.println("getCoursesEnrolledByCurUser()" + new Date() );
                    request.setAttribute("countEntity", countCourses());
                    request.setAttribute("entityType", Common.EntityType.COURSE);
                    request.setAttribute("list", allCourses);
                    request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

                    //System.out.println("DATA WENT TO JSP" + new Date() );
                    request.setAttribute("title", "title.table");
                    requestDispatcher = request.getRequestDispatcher("/table.jsp");
                    requestDispatcher.forward(request, response);
                    break;
                case 2:
                    if (splitedPath[1].equals("add")) {
                        entries.add(new EntryKV("common.courseName",""));
                        entries.add(new EntryKV("course.startDate",""));
                        entries.add(new EntryKV("course.finishDate",""));
                        entries.add(new EntryKV("common.tutor",""));
                        entries.add(new EntryKV("course.capacity",""));
                        request.setAttribute("title", "title.addCourse");
                        request.setAttribute("entryList", entries);
                        requestDispatcher = request.getRequestDispatcher("/edit.jsp");
                        requestDispatcher.forward(request, response);
                    }
                    course = getCourseById(splitedPath[1]);
                    if (course != null) {
                        request.setAttribute("entityType", Common.EntityType.USER);
                        request.setAttribute("countEntity", countUsersOnCourse(course));
                        List<User> usersOnCourse = getUsersOnCourse(course, Common.limit, offset);
                        request.setAttribute("list", usersOnCourse);
                        request.setAttribute("pageCourse", course);
                        request.setAttribute("feedbackForUsersOnCourse", getFeedbackForUsersOnCourse(usersOnCourse, course));
                        request.setAttribute("title", "title.courseCard");

                        entries.add(new EntryKV("common.courseName",course.getCourseName()));
                        entries.add(new EntryKV("course.startDate",course.getStartDate().toString()));
                        entries.add(new EntryKV("course.finishDate",course.getFinishDate().toString()));
                        entries.add(new EntryKV("common.tutor", new Integer(course.getTutor().getId()).toString()));
                        entries.add(new EntryKV("course.capacity",new Integer(course.getCapacity()).toString()));
                        request.setAttribute("entryList", entries);
                        requestDispatcher = request.getRequestDispatcher("/card.jsp");
                        requestDispatcher.forward(request,response);

                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;
                case 3:
                    if (splitedPath[2].equals("edit")) {
                        course = getCourseById(splitedPath[1]);
                        if (course != null) {
                            if (curUser.isAdmin()) {
                                // show edit course
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            }
                        }
                    }
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
                case 4:
                    course = getCourseById(splitedPath[1]);
                    if (course != null) {
                        User givenUser = getUserById(splitedPath[3]);
                        if (givenUser != null) {
                            Feedback feedback = getFeedbackByUserAndCourse(givenUser, course);
                            switch (splitedPath[2]) {
                                case "getfeedback":
                                    if (feedback != null) {
                                        if (curUser.equals(givenUser)) {
                                            // SHOW FEEDBACK TO USER
                                        } else if (curUser.equals(course.getTutor()) || curUser.isAdmin()) {
                                            // SHOW FEEDBACK TO TUTOR
                                        } else {
                                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                        }
                                    }
                                    break;
                                case "editfeedback":
                                    if (feedback != null) {
                                        if (curUser.equals(course.getTutor()) || curUser.isAdmin()) {
                                            // SHOW FEEDBACK TO TUTOR EDIT
                                        } else {
                                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                        }
                                    }
                                    break;
                                case "createfeedback":
                                    if (curUser.equals(course.getTutor()) || curUser.isAdmin()) {
                                        // SHOW FEEDBACK TO TUTOR CREATE
                                    } else {
                                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                    }
                                    break;
                            }
                        }
                    }
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
        System.out.println("coursecontroller dodelete");
        User curUser = getUserById("41");// (User) request.getSession(false).getAttribute("user");
        if (curUser == null) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
            requestDispatcher.forward(request, response);
        }
        Locale locale = Locale.US;//  (Locale) request.getSession(false).getAttribute("locale");
        request.setAttribute("locale", locale);
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            String[] splitedPath = pathInfo.toLowerCase().split("/");
            Course course = getCourseById(splitedPath[1]);
            if(course!=null) {
                String userIdStr = null;
                String answer = null;
                String symbol = (request.getHeader("referer").contains("?")?"&":"?");
                System.out.println(symbol);
                switch (splitedPath[2]) {
                    case "delete":
                        userIdStr = request.getParameter("userId");
                        if (userIdStr != null) {
                            //delete user from course
                            User givenUser = getUserById(userIdStr);
                            if (givenUser != null) {
                                if (curUser.isAdmin() || givenUser.equals(curUser)) {
                                    if (leaveCourse(course, givenUser)) {
                                        answer = "common.userLeftCourse";
                                    } else {
                                        answer = "common.userLeftCourseFail";
                                    }
                                    response.sendRedirect(request.getHeader("referer")+ symbol +"answer=" + answer);
                                } else {
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                }
                            }
                        } else {
                            //delete course
                            if (curUser.isAdmin()) {
                                if (deleteCourse(course)) {
                                    answer = "common.courseDeleted";
                                } else {
                                    answer = "common.courseDeletedFail";
                                }
                                response.sendRedirect(request.getHeader("referer")+ symbol +"answer=" + answer);
                            }else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            }
                        }
                        break;
                    case "deletefeedback":
                        userIdStr = request.getParameter("userId");
                        if (userIdStr != null) {
                            User givenUser = getUserById(userIdStr);
                            if (givenUser != null) {
                                Feedback feedback = getFeedbackByUserAndCourse(givenUser, course);
                                if(feedback!= null){
                                    if(curUser.isAdmin() || curUser.equals(course.getTutor())){
                                        if (deleteFeedback(feedback)) {
                                            answer = "common.feedbackDeleted";
                                        } else {
                                            answer = "common.feedbackDeletedFail";
                                        }
                                        response.sendRedirect(request.getHeader("referer")+ symbol +"answer=" + answer);
                                    }else {
                                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        }
        //response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
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
