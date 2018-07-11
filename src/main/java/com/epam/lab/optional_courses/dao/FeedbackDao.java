package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Feedback;

import java.util.List;

public interface FeedbackDao {

    Feedback getFeedbackById(int feedbackId);

    List<Feedback> getFeedbacksByUserId(int userId);

    List<Feedback> getFeedbacksByTutorId(int tutorId);

    List<Feedback> getAllFeedbacks();

}
