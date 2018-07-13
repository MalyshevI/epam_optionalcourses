package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;

import java.util.List;

public interface FeedbackDao {

    Feedback getFeedbackByUserAndCourse(User user, Course course);

    List<Feedback> getFeedbacksByUser(User user);

    List<Feedback> getFeedbacksByCourse(Course course);

    List<Feedback> getAllFeedbacks();

    boolean addFeedback (Feedback feedback);

    boolean deleteFeedback (Feedback feedback);

    Feedback updateFeedback (Feedback feedback);
}
