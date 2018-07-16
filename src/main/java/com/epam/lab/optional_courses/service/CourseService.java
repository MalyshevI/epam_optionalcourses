package com.epam.lab.optional_courses.service;

import com.epam.lab.optional_courses.dao.*;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;

import java.util.List;

public class CourseService {
    private static CourseDao courseDao = new CourseDaoImpl();
    private static UserDao userDao = new UserDaoImpl();
    private static FeedbackDao feedbackDao = new FeedbackDaoImpl();

    public static List<Course> getAllCourses(long limit, long offset){
        return courseDao.getAllCourses(limit, offset);
    }

    public static Course getCourseById(String input){
        try {
            int id = Integer.parseInt(input);
            return courseDao.getCourseById(id);
        }catch (NumberFormatException e){
            return null;
        }
    }

    public static User getUserById(String input){
        try {
            int id = Integer.parseInt(input);
            return userDao.getUserById(id);
        }catch (NumberFormatException e){
            return null;
        }
    }

    public static Feedback getFeedbackByUserAndCourse(User user, Course course){
        return feedbackDao.getFeedbackByUserAndCourse(user, course);
    }

    public static boolean isUserOnCourse(User user, Course course){
        return courseDao.isUserOnCourse(user, course);
    }
}
