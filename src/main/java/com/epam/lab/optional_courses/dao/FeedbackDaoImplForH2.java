package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * That class implements Data Access Object for Feedback
 *
 * @author Anton Kulaga
 */
public class FeedbackDaoImplForH2 implements FeedbackDao {

    private static final String GET_BY_USER_AND_COURSE = "SELECT * FROM feedback WHERE user_id=? AND course_id=?";
    private static final String GET_BY_USER = "SELECT * FROM feedback WHERE user_id=?";
    private static final String GET_BY_COURSE = "SELECT * FROM feedback WHERE course_id=?";
    private static final String GET_ALL = "SELECT * FROM feedback";
    private static final String ADD = "INSERT INTO feedback(user_id, course_id, feedback_body, grade) VALUES(?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM feedback WHERE user_id=? AND course_id=?";
    private static final String UPDATE = "UPDATE feedback SET feedback=?, grade=? WHERE user_id=? AND course_id=?";

    private Connection getConnection(){

    }

    /**
     * Return Feedback object from DataBase corresponding to given User and Course
     *
     * @param user   - given User object
     * @param course - given Course object
     * @return Feedback object
     */
    @Override
    public Feedback getFeedbackByUserAndCourse(User user, Course course) {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_USER_AND_COURSE);
            statement.setInt(1, user.getId());
            statement.setInt(2, course.getId());

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Feedback resultFeedback = new Feedback();
                resultFeedback.setUser(user);
                resultFeedback.setCourse(course);
                resultFeedback.setGrade(resultSet.getInt("grade"));
                resultFeedback.setFeedbackBody(resultSet.getString("feedback_body"));
                return resultFeedback;
            }else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Return list of Feedback  objects from DataBase corresponding to given User
     *
     * @param user - given User object
     * @return list of Feedback objects
     */
    @Override
    public List<Feedback> getFeedbacksByUser(User user) {
        return null;
    }

    /**
     * Return list of Feedback  objects from DataBase corresponding to given Course
     *
     * @param course - given Course object
     * @return list of Feedback objects
     */
    @Override
    public List<Feedback> getFeedbacksByCourse(Course course) {
        return null;
    }

    /**
     * Return list of all exist Feedback objects
     *
     * @return list of Feedback objects
     */
    @Override
    public List<Feedback> getAllFeedbacks() {
        return null;
    }

    /**
     * Add given Feedback object to DataBase
     *
     * @param feedback - given Feedback object
     * @return - true if adding is successful
     */
    @Override
    public boolean addFeedback(Feedback feedback) {
        return false;
    }

    /**
     * Delete given Feedback object to DataBase
     *
     * @param feedback - given Feedback object
     * @return -  true if deleting is successful
     */
    @Override
    public boolean deleteFeedback(Feedback feedback) {
        return false;
    }

    /**
     * Update Feedback in database with new Feedback object
     *
     * @param feedback - given Feedback object
     * @return - previous Feedback object from DB
     */
    @Override
    public Feedback updateFeedback(Feedback feedback) {
        return null;
    }
}
