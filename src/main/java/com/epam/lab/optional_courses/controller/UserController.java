package com.epam.lab.optional_courses.controller;

import com.epam.lab.optional_courses.dao.CommonDao;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;
import com.epam.lab.optional_courses.service.RegistrationService;
import com.epam.lab.optional_courses.service.SecurityService;
import com.epam.lab.optional_courses.service.UserService;
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

//  Здесь получаем пользователя текущего из сессиии
//        User curUser = (User) request.getSession().getAttribute("user");
//        if(curUser==null){
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
//            requestDispatcher.forward(request, response);
//        }
//        Locale locale = (Locale)request.getSession(false).getAttribute("locale");
        User curUser = getUserById("41");
        request.setAttribute("curUser", curUser);


        //Locale locale = (Locale)request.getSession(false).getAttribute("locale");
        Locale locale = Locale.US;
        request.setAttribute("locale", locale);

        ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);

        //Получаем строку адресную
        String pathInfo = request.getPathInfo();

        //задаем нулевой оффсет и вытаскиваем его из строки , если есть такой параметр
        Long offset = 0L;
        String offsetStr;

        //распарсиваем строку
        if (pathInfo == null) {
            // пустая строка com/user
            offsetStr = request.getParameter("offset");
            if (offsetStr != null) {
                try {
                    offset = Long.parseLong(offsetStr);
                } catch (NumberFormatException ignored) {
                }
            }
            //передаем оффсет
            request.setAttribute("offset", offset);

            //засовываем title
            request.setAttribute("titleWithName", curUser.getFirstName());
            request.setAttribute("entityType", Common.EntityType.COURSE);

            //засовываем число курсов для текущего юзера
            request.setAttribute("countEntity", countCoursesByUser(curUser));

            //засовываем юзера, для которого ищем страницу, в данном случае, который зашел
            request.setAttribute("pageUser", curUser);

            //ищем список курсов для конкретного юзера
            List<Course> courseList = getCoursesByUser(curUser, Common.limit, offset);
            request.setAttribute("list", courseList);

            //ищем конкретный список булевых значених на которые записан юзер для отображения кнопок
            List<Boolean> coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(curUser, courseList);
            request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

            //находим фидбэки для конкретного юзера и сетим их
            List<Feedback> feedbackForUserCourses = getFeedbackForUserCourses(courseList, curUser);
            request.setAttribute("feedbackForUserCourses", feedbackForUserCourses);


            //находим поля юзера, которые будем выводить и добавляем их в сущность, которую будем сетить
            List<EntryKV> entries = new ArrayList<>();
            entries.add(new EntryKV("common.name", curUser.getFirstName()));
            entries.add(new EntryKV("common.lastname", curUser.getLastName()));
            entries.add(new EntryKV("common.email", curUser.getEmail()));
            request.setAttribute("entryList", entries);


            RequestDispatcher requestDispatcher = request.getRequestDispatcher("card.jsp");
            requestDispatcher.forward(request, response);
        } else {

            //задаем переменные для всех скоупов
            List<Feedback> feedbackForUserCourses;
            List<EntryKV> entries;
            List<Boolean> coursesEnrolledByCurUser;
            List<Course> courseList;
            List<User> userList;
            RequestDispatcher requestDispatcher;
            User pageUser;


            //делим адресную стркоу на части
            String[] splitedPath = pathInfo.toLowerCase().split("/");


            switch (splitedPath.length) {
                case 0:
                    // строка com/user/
                    offsetStr = request.getParameter("offset");
                    if (offsetStr != null) {
                        try {
                            offset = Long.parseLong(offsetStr);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    //передаем оффсет
                    request.setAttribute("offset", offset);

                    //засовываем title
                    request.setAttribute("titleWithName", curUser.getFirstName());
                    request.setAttribute("entityType", Common.EntityType.COURSE);

                    //засовываем число курсов для текущего юзера
                    request.setAttribute("countEntity", countCoursesByUser(curUser));

                    //засовываем юзера, для которого ищем страницу, в данном случае, который зашел
                    request.setAttribute("pageUser", curUser);

                    //ищем список курсов для конкретного юзера
                    courseList = getCoursesByUser(curUser, Common.limit, offset);
                    request.setAttribute("list", courseList);

                    //ищем конкретный список булевых значених на которые записан юзер для отображения кнопок
                    coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(curUser, courseList);
                    request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

                    //находим фидбэки для конкретного юзера и сетим их
                    feedbackForUserCourses = getFeedbackForUserCourses(courseList, curUser);
                    request.setAttribute("feedbackForUserCourses", feedbackForUserCourses);


                    //находим поля юзера, которые будем выводить и добавляем их в сущность, которую будем сетить
                    entries = new ArrayList<>();
                    entries.add(new EntryKV("common.name", curUser.getFirstName()));
                    entries.add(new EntryKV("common.lastname", curUser.getLastName()));
                    entries.add(new EntryKV("common.email", curUser.getEmail()));
                    request.setAttribute("entryList", entries);


                    requestDispatcher = request.getRequestDispatcher("card.jsp");
                    requestDispatcher.forward(request, response);//  строка com/user/

                    break;


                case 2:

                    switch (splitedPath[1]) {
                        case "add":

                            //строка com/user/add

                            if (curUser.isAdmin()) {
                                //засовываем title
                                request.setAttribute("title", "title.addUser");

                                entries = new ArrayList<>();
                                entries.add(new EntryKV("common.name", ""));
                                entries.add(new EntryKV("common.lastname", ""));
                                entries.add(new EntryKV("common.email", ""));
                                entries.add(new EntryKV("common.password", ""));
                                request.setAttribute("entryList", entries);

                                requestDispatcher = request.getRequestDispatcher("/edit.jsp");
                                requestDispatcher.forward(request, response);
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            }
                            break;


                        case "edit":

                            //строка com/user/edit

                            pageUser = getUserById(request.getParameter("userId"));
                            if (curUser.isAdmin() || curUser.equals(pageUser)) {
                                //title сетим
                                request.setAttribute("title", "title.editUser");

                                entries = new ArrayList<>();
                                entries.add(new EntryKV("common.name", pageUser.getFirstName()));
                                entries.add(new EntryKV("common.lastname", pageUser.getLastName()));
                                entries.add(new EntryKV("common.email", pageUser.getEmail()));
                                entries.add(new EntryKV("common.password", ""));
                                request.setAttribute("entryList", entries);

                                requestDispatcher = request.getRequestDispatcher("/edit.jsp");
                                requestDispatcher.forward(request, response);

                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            }
                            break;


                        case "all":

                            // строка com/user/all

                            if (curUser.isAdmin()) {

                                offsetStr = request.getParameter("offset");
                                if (offsetStr != null) {
                                    try {
                                        offset = Long.parseLong(offsetStr);
                                    } catch (NumberFormatException ignored) {
                                    }
                                }

                                //передаем оффсет
                                request.setAttribute("offset", offset);

                                //засовываем title
                                request.setAttribute("titleWithName", "title.allUsers");

                                //засовываем сущность
                                request.setAttribute("entityType", Common.EntityType.USER);

                                //засовываем число юзеров
                                request.setAttribute("countEntity", countAllUsers());

                                //ищем список юзеров
                                userList = getAllUsers(Common.limit, offset);
                                request.setAttribute("list", userList);

                                requestDispatcher = request.getRequestDispatcher("/table.jsp");
                                requestDispatcher.forward(request, response);//  строка com/user/all
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            }


                            break;


                        default:

                            // либо верная строка com/user/125
                            // либо неверная строка com/user/авп

                            //проверяем есть ли такой юзер по id
                            pageUser = getUserById(splitedPath[1]);
                            if (pageUser != null) {
                                //если есть:
                                offsetStr = request.getParameter("offset");
                                if (offsetStr != null) {
                                    try {
                                        offset = Long.parseLong(offsetStr);
                                    } catch (NumberFormatException ignored) {
                                    }
                                }

                                //передаем оффсет
                                request.setAttribute("offset", offset);

                                //засовываем title
                                request.setAttribute("title", pageUser.getFirstName());
                                request.setAttribute("entityType", Common.EntityType.COURSE);

                                //засовываем число курсов для запрашиваемого юзера
                                request.setAttribute("countEntity", countCoursesByUser(pageUser));

                                //засовываем юзера, для которого ищем страницу
                                request.setAttribute("pageUser", pageUser);

                                //ищем список курсов для конкретного юзера
                                courseList = getCoursesByUser(pageUser, Common.limit, offset);
                                request.setAttribute("list", courseList);

                                //ищем конкретный список булевых значених на которые записан юзер для отображения кнопок
                                coursesEnrolledByCurUser = getCoursesEnrolledByCurUser(pageUser, courseList);
                                request.setAttribute("coursesEnrolledByCurUser", coursesEnrolledByCurUser);

                                //находим фидбэки для конкретного юзера и сетим их
                                feedbackForUserCourses = getFeedbackForUserCourses(courseList, pageUser);
                                request.setAttribute("feedbackForUserCourses", feedbackForUserCourses);


                                //находим поля юзера, которые будем выводить и добавляем их в сущность, которую будем сетить
                                entries = new ArrayList<>();
                                entries.add(new EntryKV("common.name", pageUser.getFirstName()));
                                entries.add(new EntryKV("common.lastname", pageUser.getLastName()));
                                entries.add(new EntryKV("common.email", pageUser.getEmail()));
                                request.setAttribute("entryList", entries);

                                requestDispatcher = request.getRequestDispatcher("card.jsp");
                                requestDispatcher.forward(request, response);//

                            } else {
                                //если нет, то 404 еррор
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //берем юзера текущего
        User curUser = getUserById("41");// (User) request.getSession(false).getAttribute("user");
        if (curUser == null) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
            requestDispatcher.forward(request, response);
        }

        //сетим локаль
        Locale locale = Locale.US;//  (Locale) request.getSession(false).getAttribute("locale");
        request.setAttribute("locale", locale);

        //парсим строку
        String pathInfo = request.getPathInfo();

        //если не пустая
        if (pathInfo != null) {
            //делим на части
            String[] splitedPath = pathInfo.toLowerCase().split("/");
            //чекаем случаи

            String userIdStr = null;
            String answer = null;
            String symbol = (request.getHeader("referer").contains("?") ? "&" : "?");


            if (splitedPath.length == 2) {
                switch (splitedPath[1]) {
                    case "delete":
                        userIdStr = request.getParameter("userId");
                        if (userIdStr != null) {
                            User givenUser = getUserById(userIdStr);
                            if (givenUser != null) {
                                if (curUser.isAdmin()) {
                                    if (deleteUser(givenUser)) {
                                        answer = "common.userDeleted";
                                    } else {
                                        answer = "common.userDeletedFail";
                                    }
                                    response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                } else if (givenUser.equals(curUser)) {
                                    if (deleteUser(givenUser)) {
                                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Login.jsp");
                                        requestDispatcher.forward(request, response);
                                    } else {
                                        answer = "common.userDeletedFail";
                                        response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                    }
                                } else {
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                }
                            }
                        }

                    case "edit":


                        if(curUser.isAdmin()){
                            userIdStr = request.getParameter("userId");
                            if (userIdStr != null){
                                User givenUser = getUserById(userIdStr)){
                                    if (givenUser != null){

                                    }
                                }
                            }

                        }



                        if (curUser.getId())


                        User user = new User();

                        user.setFirstName(request.getParameter("common.name"));
                        user.setLastName(request.getParameter("common.lastname"));

                        //проверить емэил
                        user.setEmail(request.getParameter("common.email"));
                        user.setPassword(request.getParameter("common.password"));


                        user.
                        CommonDao.userDao.updateUser()


                        break;

                    case "add":

                        break;
                }
            }
        }
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
