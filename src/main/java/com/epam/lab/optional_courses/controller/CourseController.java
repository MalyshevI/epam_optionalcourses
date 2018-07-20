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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.epam.lab.optional_courses.service.CourseService.*;
import static com.epam.lab.optional_courses.service.FeedbackService.*;
import static com.epam.lab.optional_courses.service.UserService.getUserById;

@WebServlet(loadOnStartup = 1)
public class CourseController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CourseController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User curUser = (User) request.getSession(false).getAttribute("user");
        if(curUser==null){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
            requestDispatcher.forward(request, response);
        }
        Locale locale = (Locale)request.getSession(false).getAttribute("locale");
        if(locale== null){
            locale = Locale.US;
        }
        request.setAttribute("locale", locale);
        //User curUser = getUserById("44"); //current logined user
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
            request.setAttribute("countUsersOnCourseList",countUsersOnCourseList(allCourses));
            //System.out.println("DATA WENT TO JSP" + new Date() );
            request.setAttribute("title", "title.table");
            requestDispatcher = request.getRequestDispatcher("/table.jsp");
            requestDispatcher.forward(request, response);
        } else {
            String[] splitedPath = pathInfo.toLowerCase().split("/");
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
                    request.setAttribute("countUsersOnCourseList",countUsersOnCourseList(allCourses));
                    //System.out.println("DATA WENT TO JSP" + new Date() );
                    request.setAttribute("title", "title.table");
                    requestDispatcher = request.getRequestDispatcher("/table.jsp");
                    requestDispatcher.forward(request, response);
                    break;
                case 2:
                    if (splitedPath[1].equals("add")) {
                        if (curUser.isAdmin()) {
                            entries.add(new EntryKV("common.courseName", ""));
                            entries.add(new EntryKV("course.startDate", ""));
                            entries.add(new EntryKV("course.finishDate", ""));
                            entries.add(new EntryKV("common.tutor", ""));
                            entries.add(new EntryKV("course.capacity", ""));
                            request.setAttribute("title", "title.addCourse");
                            request.setAttribute("entryList", entries);
                            requestDispatcher = request.getRequestDispatcher("/edit.jsp");                  //BUTTONS ADD!
                            requestDispatcher.forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        }
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

                        entries.add(new EntryKV("common.courseName", course.getCourseName()));
                        entries.add(new EntryKV("course.startDate", course.getStartDate().toString()));
                        entries.add(new EntryKV("course.finishDate", course.getFinishDate().toString()));
                        entries.add(new EntryKV("common.tutor", Integer.toString(course.getTutor().getId())));
                        entries.add(new EntryKV("course.capacity", Integer.toString(course.getCapacity())));
                        request.setAttribute("entryList", entries);
                        request.setAttribute("titleWithName", course.getCourseName());
                        requestDispatcher = request.getRequestDispatcher("/card.jsp");
                        requestDispatcher.forward(request, response);

                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;
                case 3:
                    if (splitedPath[2].equals("edit")) {
                        course = getCourseById(splitedPath[1]);
                        if (course != null) {
                            if (curUser.isAdmin()) {
                                entries.add(new EntryKV("common.courseName", course.getCourseName()));
                                entries.add(new EntryKV("course.startDate", course.getStartDate().toString()));
                                entries.add(new EntryKV("course.finishDate", course.getFinishDate().toString()));
                                entries.add(new EntryKV("common.tutor", Integer.toString(course.getTutor().getId())));
                                entries.add(new EntryKV("course.capacity", Integer.toString(course.getCapacity())));
                                request.setAttribute("title", "title.editCourse");
                                request.setAttribute("entryList", entries);
                                request.setAttribute("pageCourse", course);
                                requestDispatcher = request.getRequestDispatcher("/edit.jsp");                  //BUTTONS EDIT!
                                requestDispatcher.forward(request, response);
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            }
                        }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
                    }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
                    break;
                case 4:
                    course = getCourseById(splitedPath[1]);
                    if (course != null) {
                        User givenUser = getUserById(splitedPath[3]);
                        if (givenUser != null && isUserOnCourse(givenUser, course)) {
                            Feedback feedback = getFeedbackByUserAndCourse(givenUser, course);
                            switch (splitedPath[2]) {
                                case "getfeedback":
                                    if (feedback != null) {
                                        if (curUser.equals(givenUser) || curUser.equals(course.getTutor()) || curUser.isAdmin()) {
                                            entries.add(new EntryKV("common.student", givenUser.getFirstName() + givenUser.getLastName()));
                                            entries.add(new EntryKV("common.course", course.getCourseName()));
                                            entries.add(new EntryKV("feedback.grade", Integer.toString(feedback.getGrade())));
                                            entries.add(new EntryKV("feedback.feedbackBody", feedback.getFeedbackBody()));
                                            request.setAttribute("title", "title.feedback");
                                            request.setAttribute("entryList", entries);
                                            request.setAttribute("pageFeedback", feedback);
                                            requestDispatcher = request.getRequestDispatcher("/feedback.jsp");
                                            requestDispatcher.forward(request, response);
                                        } else {
                                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                        }
                                    }
                                    break;
                                case "editfeedback":
                                    if (feedback != null) {
                                        if (curUser.equals(course.getTutor()) || curUser.isAdmin()) {
                                            entries.add(new EntryKV("common.student", givenUser.getFirstName() + givenUser.getLastName()));
                                            entries.add(new EntryKV("common.course", course.getCourseName()));
                                            request.setAttribute("grade", feedback.getGrade());
                                            request.setAttribute("feedbackBody", feedback.getFeedbackBody());
                                            request.setAttribute("title", "title.editFeedback");
                                            request.setAttribute("entryList", entries);
                                            request.setAttribute("pageFeedback", feedback);
                                            requestDispatcher = request.getRequestDispatcher("/editFeedback.jsp");              //BUTTONS@!!
                                            requestDispatcher.forward(request, response);
                                        } else {
                                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                        }{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
                                    }
                                    break;
                                case "createfeedback":
                                    if (feedback == null) {
                                        if (curUser.equals(course.getTutor()) || curUser.isAdmin()) {
                                            entries.add(new EntryKV("common.student", givenUser.getFirstName() + givenUser.getLastName()));
                                            entries.add(new EntryKV("common.course", course.getCourseName()));
                                            request.setAttribute("grade", "");
                                            request.setAttribute("feedbackBody", "");
                                            request.setAttribute("title", "title.createFeedback");
                                            request.setAttribute("entryList", entries);
                                            requestDispatcher = request.getRequestDispatcher("/editFeedback.jsp");              //BUTTONS@!!
                                            requestDispatcher.forward(request, response);
                                        } else {
                                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                        }
                                    }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
                                    break;
                            }
                        }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
                    }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}

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
        User curUser = (User) request.getSession(false).getAttribute("user");
        if(curUser==null){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
            requestDispatcher.forward(request, response);
        }
        Locale locale = (Locale)request.getSession(false).getAttribute("locale");
        if(locale== null){
            locale = Locale.US;
        }
        request.setAttribute("locale", locale);
        request.setAttribute("curUser", curUser);
        String pathInfo = request.getPathInfo();
        String symbol = (request.getHeader("referer").contains("?") ? "&" : "?");
        String answer = null;
        if (pathInfo != null) {
            String[] splitedPath = pathInfo.toLowerCase().split("/");
            if (splitedPath.length == 2) {
                if (splitedPath[1].equals("add")) {
                    if (curUser.isAdmin()) {
                        String courseName = request.getParameter("common.courseName");
                        LocalDate startDate = dateFromStr(request.getParameter("course.startDate"));
                        if (startDate != null) {
                            LocalDate finishDate = dateFromStr(request.getParameter("course.finishDate"));
                            if (finishDate != null) {
                                String tutorId = request.getParameter("common.tutor");
                                User tutor = getUserById(tutorId);
                                if (tutor != null) {
                                    String capacityStr = request.getParameter("course.capacity");
                                    int capacity;
                                    try {
                                        capacity = Integer.parseInt(capacityStr);
                                    } catch (NumberFormatException e) {
                                        capacity = -1;
                                    }
                                    if (capacity > 0) {
                                        Course newCourse = new Course(12, courseName, startDate, finishDate, tutor, capacity);
                                        if (addCourse(newCourse)) {
                                            response.sendRedirect("/course/" + newCourse.getId());
                                        } else {
                                            answer = "course.error";
                                            response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                        }

                                    } else {
                                        answer = "course.invalidCapacity";
                                        response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                    }
                                } else {
                                    answer = "course.invalidTutor";
                                    response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                }
                            } else {
                                answer = "course.invalidFinishDate";
                                response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                            }
                        } else {
                            answer = "course.invalidStartDate";
                            response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                        }

                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            if (splitedPath.length > 1) {
                Course course = getCourseById(splitedPath[1]);
                if (course != null) {
                    String userIdStr = request.getParameter("userId");
                    User givenUser = getUserById(userIdStr);
                    switch (splitedPath.length) {                                     //parsing by URL length
                        case 3:
                            switch (splitedPath[2]) {                                //parsing by URL body
                                case "delete":
                                    if (userIdStr != null) {
                                        //delete user from course
                                        if (givenUser != null) {
                                            if (curUser.isAdmin() || givenUser.equals(curUser)) {
                                                if (leaveCourse(course, givenUser)) {
                                                    answer = "common.userLeftCourse";
                                                } else {
                                                    answer = "common.userLeftCourseFail";
                                                }
                                                response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
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
                                            response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                        } else {
                                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                        }
                                    }
                                    break;

                                case "deletefeedback":
                                    //delete feedback
                                    userIdStr = request.getParameter("userId");
                                    if (userIdStr != null) {
                                        if (givenUser != null) {
                                            Feedback feedback = getFeedbackByUserAndCourse(givenUser, course);
                                            if (feedback != null) {
                                                if (curUser.isAdmin() || curUser.equals(course.getTutor())) {
                                                    if (deleteFeedback(feedback)) {
                                                        answer = "common.feedbackDeleted";
                                                    } else {
                                                        answer = "common.feedbackDeletedFail";
                                                    }
                                                    response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                                } else {
                                                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                                }
                                            }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
                                        }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
                                    }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
                                    break;

                                case "apply":
                                    //apply user on course
                                    if (givenUser != null) {
                                        if (curUser.isAdmin() || givenUser.equals(curUser)) {
                                            if (enrollUserOnCourse(course, givenUser)) {
                                                answer = "course.userApplyCourse";
                                            } else {
                                                answer = "course.userApplyCourseFail";
                                            }
                                            response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                        } else {
                                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                        }
                                    }else{
                                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                                    }
                                    break;
                                case "edit":
                                    //edit course
                                    String courseName = request.getParameter("common.courseName");
                                    LocalDate startDate = dateFromStr(request.getParameter("course.startDate"));
                                    if (startDate != null) {
                                        LocalDate finishDate = dateFromStr(request.getParameter("course.finishDate"));
                                        if (finishDate != null) {
                                            String tutorId = request.getParameter("common.tutor");
                                            User tutor = getUserById(tutorId);
                                            if (tutor != null) {
                                                String capacityStr = request.getParameter("course.capacity");
                                                int capacity;
                                                try {
                                                    capacity = Integer.parseInt(capacityStr);
                                                } catch (NumberFormatException e) {
                                                    capacity = -1;
                                                }
                                                if (capacity > 0) {
                                                    course.setCourseName(courseName);
                                                    course.setStartDate(startDate);
                                                    course.setFinishDate(finishDate);
                                                    course.setTutor(tutor);
                                                    course.setCapacity(capacity);
                                                    if (editCourse(course)) {
                                                        response.sendRedirect("/course/" + course.getId());
                                                    } else {
                                                        answer = "course.error";
                                                        response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                                    }

                                                } else {
                                                    answer = "course.invalidCapacity";
                                                    response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                                }
                                            } else {
                                                answer = "course.invalidTutor";
                                                response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                            }
                                        } else {
                                            answer = "course.invalidFinishDate";
                                            response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                        }
                                    } else {
                                        answer = "course.invalidStartDate";
                                        response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                    }
                                    break;
                            }
                            break;

                        case 4:
                            givenUser = getUserById(splitedPath[3]);
                            if (givenUser != null && isUserOnCourse(givenUser, course)) {
                            String gradeStr = request.getParameter("grade");
                            String feedbackBody = request.getParameter("feedbackBody");
                            int grade;
                            switch (splitedPath[2]) {                                //parsing by URL body
                                case "createfeedback":
                                    //create feedback
                                    try {
                                        grade = Integer.parseInt(gradeStr);
                                    } catch (NumberFormatException e) {
                                        grade = -1;
                                    }
                                    if (0 < grade && grade < 11) {
                                        Feedback feedback = new Feedback(givenUser, course, grade, feedbackBody);
                                        if (addFeedback(feedback)) {
                                            response.sendRedirect("/course/" + course.getId() + "/getFeedback/" + givenUser.getId());
                                        } else {
                                            answer = "feedback.error";
                                            response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                        }
                                    } else {
                                        answer = "feedback.invalidGrade";
                                        response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                    }
                                    break;

                                case "editfeedback":
                                    //edit feedback
                                    try {
                                        grade = Integer.parseInt(gradeStr);
                                    } catch (NumberFormatException e) {
                                        grade = -1;
                                    }
                                    if (0 < grade && grade < 11) {

                                        Feedback feedback = getFeedbackByUserAndCourse(givenUser, course);
                                        feedback.setFeedbackBody(feedbackBody);
                                        feedback.setGrade(grade);
                                        if (editFeedback(feedback)) {
                                            response.sendRedirect("/course/" + course.getId() + "/getFeedback/" + givenUser.getId());
                                        } else {
                                            answer = "feedback.error";
                                            response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                        }
                                    } else {
                                        answer = "feedback.invalidGrade";
                                        response.sendRedirect(request.getHeader("referer") + symbol + "answer=" + answer);
                                    }
                                    break;
                            }
                        }
                        break;
                    }
                }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
            }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
        }else{response.sendError(HttpServletResponse.SC_NOT_FOUND);}
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
