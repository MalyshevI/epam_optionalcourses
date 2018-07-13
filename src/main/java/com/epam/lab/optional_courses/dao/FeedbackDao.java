package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;

import java.util.List;

/**
 * That interface represent Data Access Object for Feedback
 *
 * @author Anton Kulaga
 */

public interface FeedbackDao {

    /**
     *  Return Feedback object from DataBase corresponding to given User and Course
     *
     * @param user - given User object
     * @param course - given Course object
     * @return Feedback object
     */
    Feedback getFeedbackByUserAndCourse(User user, Course course);

    /**
     * Return list of Feedback  objects from DataBase corresponding to given User
     *
     * @param user - given User object
     * @return list of Feedback objects
     */
    List<Feedback> getFeedbacksByUser(User user);

    /**
     * Return list of Feedback  objects from DataBase corresponding to given Course
     *
     * @param course - given Course object
     * @return list of Feedback objects
     */
    List<Feedback> getFeedbacksByCourse(Course course);

    /**
     * Return list of all exist Feedback objects
     *
     * @return list of Feedback objects
     */
    List<Feedback> getAllFeedbacks();

    /**
     * Add given Feedback object to DataBase
     *
     * @param feedback - given Feedback object
     * @return - true if adding is successful
     */
    boolean addFeedback (Feedback feedback);

    /**
     * Delete given Feedback object to DataBase
     *
     * @param feedback - given Feedback object
     * @return -  true if deleting is successful
     */
    boolean deleteFeedback (Feedback feedback);

    /**
     * Update Feedback in database with new Feedback object
     * @param feedback - given Feedback object
     * @return - previous Feedback object from DB
     */
    Feedback updateFeedback (Feedback feedback);
}
