package com.epam.lab.optional_courses.dao;

public class CommonDao {
    public static final CourseDao courseDao = new CourseDaoImpl();
    public static final UserDao userDao = new UserDaoImpl();
    public static final FeedbackDao feedbackDao = new FeedbackDaoImpl();
}
