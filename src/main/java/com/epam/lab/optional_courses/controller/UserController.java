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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.epam.lab.optional_courses.service.CourseService.*;
import static com.epam.lab.optional_courses.service.UserService.*;
import static com.epam.lab.optional_courses.service.FeedbackService.*;

@WebServlet(loadOnStartup = 1)
public class UserController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(UserController.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*User curUser = (User) request.getSession().getAttribute("user");

        if(curUser==null){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
            requestDispatcher.forward(request, response);
        }
        Locale locale = (Locale)request.getSession(false).getAttribute("locale");
*/
        //Locale locale = (Locale)request.getSession(false).getAttribute("locale");
        Locale locale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
        User curUser = getUserById("41");
        String pathInfo = request.getPathInfo();
        Long offset = 0L;
        String offsetStr;

        if (pathInfo == null) {
            offsetStr = request.getParameter("offset");
            if (offsetStr != null) {
                try {
                    offset = Long.parseLong(offsetStr);
                } catch (NumberFormatException ignored) {
                }
            }
            List<Course> courseList = getCoursesByUser(curUser, Common.limit, offset);
            List<Boolean> coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(curUser, courseList);
            List<Feedback> feedbackForUserCourses = getFeedbackForUserCourses(courseList, curUser);

            List<EntryKV> entries = new ArrayList<>();
            entries.add(new EntryKV("common.name",curUser.getFirstName()));
            entries.add(new EntryKV("common.lastname",curUser.getLastName()));
            entries.add(new EntryKV("common.email",curUser.getEmail()));

            request.setAttribute("title", curUser.getFirstName());
            request.setAttribute("locale", locale);
            request.setAttribute("curUser", curUser);
            request.setAttribute("pageUser", curUser);
            request.setAttribute("entryList",entries);
            request.setAttribute("offset", offset);
            request.setAttribute("list", courseList);
            request.setAttribute("entityType", Common.EntityType.COURSE);
            request.setAttribute("countEntity", countCoursesByUser(curUser));
            request.setAttribute("feedbackForUserCourses",feedbackForUserCourses);
            request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("card.jsp");
            requestDispatcher.forward(request, response);
        } else {


            String[] splitedPath = pathInfo.toLowerCase().split("/");
            User pageUser;
            List<Course> courseList;
            List<Boolean> coursesEnrolledByCurUser;
            RequestDispatcher requestDispatcher;
            List<Feedback> feedbackForUserCourses;
            List<EntryKV> entries;

            switch (splitedPath.length) {
                case 0:
                    offsetStr = request.getParameter("offset");
                    if (offsetStr != null) {
                        try {
                            offset = Long.parseLong(offsetStr);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    courseList = getCoursesByUser(curUser, Common.limit, offset);
                    coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(curUser, courseList);
                    feedbackForUserCourses = getFeedbackForUserCourses(courseList, curUser);

                    entries = new ArrayList<>();
                    entries.add(new EntryKV("common.name",curUser.getFirstName()));
                    entries.add(new EntryKV("common.lastname",curUser.getLastName()));
                    entries.add(new EntryKV("common.email",curUser.getEmail()));

                    request.setAttribute("title", curUser.getFirstName());
                    request.setAttribute("locale", locale);
                    request.setAttribute("curUser", curUser);
                    request.setAttribute("pageUser", curUser);
                    request.setAttribute("entryList",entries);
                    request.setAttribute("offset", offset);
                    request.setAttribute("list", courseList);
                    request.setAttribute("entityType", Common.EntityType.COURSE);
                    request.setAttribute("countEntity", countCoursesByUser(curUser));
                    request.setAttribute("feedbackForUserCourses",feedbackForUserCourses);
                    request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

                    requestDispatcher = request.getRequestDispatcher("/card.jsp");
                    requestDispatcher.forward(request, response);
                    break;


                case 2:

                    switch (splitedPath[1]) {
                        case "add":
                            if (curUser.isAdmin()){
                                entries = new ArrayList<>();
                                entries.add(new EntryKV("common.name",""));
                                entries.add(new EntryKV("common.lastname",""));
                                entries.add(new EntryKV("common.email",""));
                                entries.add(new EntryKV("common.password",""));

                                request.setAttribute("locale", locale);
                                request.setAttribute("entryList",entries);
                                request.setAttribute("title", curUser.getFirstName());
                                request.setAttribute("curUser", curUser);
                                request.setAttribute("pageUser", curUser);

                                requestDispatcher = request.getRequestDispatcher("/edit.jsp");
                                requestDispatcher.forward(request, response);

                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            }
                            break;

                        case "delete":

                            //show page delete
                            break;

                        case "edit":
                            if (curUser.isAdmin()){
                                entries = new ArrayList<>();
                                entries.add(new EntryKV("common.name",curUser.getFirstName()));
                                entries.add(new EntryKV("common.lastname",curUser.getLastName()));
                                entries.add(new EntryKV("common.email",curUser.getEmail()));
                                entries.add(new EntryKV("common.password",""));

                                request.setAttribute("locale", locale);
                                request.setAttribute("entryList",entries);
                                request.setAttribute("title", curUser.getFirstName());
                                request.setAttribute("curUser", curUser);
                                request.setAttribute("pageUser", curUser);

                                requestDispatcher = request.getRequestDispatcher("/edit.jsp");
                                requestDispatcher.forward(request, response);

                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            }
                            break;


                        default:
                            pageUser = getUserById(splitedPath[1]);
                            if (pageUser != null) {
                                offsetStr = request.getParameter("offset");
                                if (offsetStr != null) {
                                    try {
                                        offset = Long.parseLong(offsetStr);
                                    } catch (NumberFormatException ignored) {
                                    }
                                }

                                courseList = getCoursesByUser(pageUser, Common.limit, offset);
                                coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(pageUser, courseList);
                                feedbackForUserCourses = getFeedbackForUserCourses(courseList, pageUser);

                                request.setAttribute("countEntity", countCoursesByUser(pageUser));
                                request.setAttribute("entityType", Common.EntityType.COURSE);
                                request.setAttribute("list", courseList);
                                request.setAttribute("curUser", curUser);
                                request.setAttribute("locale", locale);
                                request.setAttribute("feedbackForUserCourses",feedbackForUserCourses);
                                request.setAttribute("pageUser", curUser);
                                request.setAttribute("offset", offset);
                                request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

                                //System.out.println("DATA WENT TO JSP" + new Date());
                                entries = new ArrayList<>();
                                entries.add(new EntryKV(bundle.getString("common.name"),curUser.getFirstName()));
                                entries.add(new EntryKV(bundle.getString("common.lastname"),curUser.getLastName()));
                                entries.add(new EntryKV(bundle.getString("common.email"),curUser.getEmail()));

                                request.setAttribute("entryList",entries);

                                requestDispatcher = request.getRequestDispatcher("card.jsp");
                                requestDispatcher.forward(request, response);
                                break;
                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            }

                            break;
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



        response.getWriter().println("Hello World!");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
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
