package com.epam.lab.optional_courses.service;

import com.epam.lab.optional_courses.dao.CommonDao;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;

import java.util.ArrayList;
import java.util.List;

public class FeedbackService {

    public static List<Feedback> getFeedbackForUsersOnCourse(List<User> userList, Course course) {
        List<Feedback> resultList = new ArrayList<>();
        for (User curUser: userList) {
            resultList.add(CommonDao.feedbackDao.getFeedbackByUserAndCourse(curUser, course));
        }
        return resultList;
    }

    public static List<Feedback> getFeedbackForUserCourses(List<Course> courseList, User user) {
        List<Feedback> resultList = new ArrayList<>();
        for (Course curCourse: courseList) {
            resultList.add(CommonDao.feedbackDao.getFeedbackByUserAndCourse(user, curCourse));
        }
        return resultList;
    }
}
