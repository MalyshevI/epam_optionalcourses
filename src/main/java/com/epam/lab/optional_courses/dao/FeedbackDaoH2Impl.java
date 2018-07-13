package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * That class implements Data Access Object for Feedback
 *
 * @author Anton Kulaga
 */
public class FeedbackDaoH2Impl implements FeedbackDao {

    private UserDaoH2Impl userDaoH2;
    private CourseDaoH2Impl courseDaoH2;
    private final Logger log = LogManager.getLogger(FeedbackDaoH2Impl.class);

    private static final String GET_BY_USER_AND_COURSE = "SELECT * FROM feedback WHERE user_id=? AND course_id=?";
    private static final String GET_BY_USER = "SELECT * FROM feedback WHERE user_id=?";
    private static final String GET_BY_COURSE = "SELECT * FROM feedback WHERE course_id=?";
    private static final String GET_ALL = "SELECT * FROM feedback";
    private static final String ADD = "INSERT INTO feedback(user_id, course_id, feedback_body, grade) VALUES(?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM feedback WHERE user_id=? AND course_id=?";
    private static final String UPDATE = "UPDATE feedback SET feedback=?, grade=? WHERE user_id=? AND course_id=?";

    private Connection getConnection() {

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
        Connection connection;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_BY_USER_AND_COURSE);
            statement.setInt(1, user.getId());
            statement.setInt(2, course.getId());

            resultSet = statement.executeQuery();
            Feedback resultFeedback;
            if (resultSet.next()) {
                resultFeedback = new Feedback();
                resultFeedback.setUser(user);
                resultFeedback.setCourse(course);
                resultFeedback.setGrade(resultSet.getInt("grade"));
                resultFeedback.setFeedbackBody(resultSet.getString("feedback_body"));
            } else {
                resultFeedback = null;
            }
            connection.close();
            return resultFeedback;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet);
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
        Connection connection;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_BY_USER);
            statement.setInt(1, user.getId());

            resultSet = statement.executeQuery();
            List<Feedback> resultList = new ArrayList<>();
            while (resultSet.next()) {
                Feedback resultFeedback = new Feedback();
                resultFeedback.setUser(user);
                resultFeedback.setCourse(courseDaoH2.getCourseById((resultSet.getInt("course_id"))));
                resultFeedback.setGrade(resultSet.getInt("grade"));
                resultFeedback.setFeedbackBody(resultSet.getString("feedback_body"));
                resultList.add(resultFeedback);
            }
            connection.close();
            return resultList;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet);
        }
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

    private void closeResources(PreparedStatement statement, ResultSet resultSet) {
        try {
            if (statement != null) statement.close();
            if (resultSet != null) resultSet.close();
        } catch (SQLException e1) {
            log.log(Level.ERROR, e1);
            e1.printStackTrace();
        }
    }
}
