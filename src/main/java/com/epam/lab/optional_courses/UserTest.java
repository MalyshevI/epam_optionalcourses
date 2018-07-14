package com.epam.lab.optional_courses;
import com.epam.lab.optional_courses.dao.FeedbackDaoH2Impl;
import com.epam.lab.optional_courses.dao.UserDaoH2Impl;

public class UserTest {
    public static void main(String[] args) {
        FeedbackDaoH2Impl feedbackDaoH2 = new FeedbackDaoH2Impl();
        UserDaoH2Impl userDaoH2 = new UserDaoH2Impl();
        System.out.println(feedbackDaoH2.getAllFeedbacks().size());
    }
}