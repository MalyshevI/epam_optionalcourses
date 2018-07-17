package com.epam.lab.optional_courses.service;

import com.epam.lab.optional_courses.dao.*;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CourseService {

    public static List<Course> getAllCourses(long limit, long offset) {
        return CommonDao.courseDao.getAllCourses(limit, offset);
    }

    public static Course getCourseById(String input) {
        try {
            int id = Integer.parseInt(input);
            return CommonDao.courseDao.getCourseById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static User getUserById(String input) {
        try {
            int id = Integer.parseInt(input);
            return CommonDao.userDao.getUserById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Feedback getFeedbackByUserAndCourse(User user, Course course) {
        return CommonDao.feedbackDao.getFeedbackByUserAndCourse(user, course);
    }

    public static boolean isUserOnCourse(User user, Course course) {
        return CommonDao.courseDao.isUserOnCourse(user, course);
    }

    public static List<Boolean> getCoursesEnrolledByCurUser(User curUser, List<Course> allCourses) {
        List<Boolean> resultList = new ArrayList<>();
        for (Course curCourse: allCourses) {
            if(CommonDao.courseDao.isUserOnCourse(curUser, curCourse)){
                resultList.add(true);
            }else{
                resultList.add(false);
            }
        }
        return resultList;
    }

    public static long countCourses(){
        return  CommonDao.courseDao.countCourses();
    }
}
