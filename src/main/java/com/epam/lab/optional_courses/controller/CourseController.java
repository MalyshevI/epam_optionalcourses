package com.epam.lab.optional_courses.controller;


import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.epam.lab.optional_courses.service.CourseService.*;

public class CourseController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CourseController.class);
    private static long limit = 20;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        /*
                AUTH - > USER
        */
        User curUser = new User(); //current logined user
        if (pathInfo == null) {
            List<Course> allCourses = getAllCourses(limit, 0);
            // SENT TO JSP PREPROC - > (String) HTML
        } else {
            String[] splitedPath = pathInfo.toLowerCase().split("/");
            Course course;
            switch (splitedPath.length) {
                case 0:
                    List<Course> allCourses = getAllCourses(limit, 0);
                    // SENT TO JSP PREPROC - > (String) HTML
                    break;
                case 2:
                    course = getCourseById(splitedPath[1]);
                    if (course != null) {
                        if(curUser.equals(course.getTutor())){
                            //show COURSE TO TUTOR
                        }else if(isUserOnCourse(curUser, course)){
                            //show course to user enrolled on course
                        }else {
                            // SHOW COURSE to not enrolled user
                        }
                    } else {
                        // return 404
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
                                        //return 404
                                    }
                                } else {
                                    //return 404
                                }

                            } else {
                                // return 404
                            }
                        }else{
                            //return 404
                        }
                    }else {
                        //return 404
                    }

                    break;
                default:
                    //return 404
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
