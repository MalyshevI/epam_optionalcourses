package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.dao.connectionPools.ConnectionPool;
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
    private static final Logger log = LogManager.getLogger(FeedbackDaoH2Impl.class);

    private static final String GET_BY_USER_AND_COURSE = "SELECT user_id, course_id, feedback_body, grade FROM mydb.feedback WHERE user_id=? AND course_id=?";
    private static final String GET_BY_USER = "SELECT user_id, course_id, feedback_body, grade FROM mydb.feedback WHERE user_id=?";
    private static final String GET_BY_COURSE = "SELECT user_id, course_id, feedback_body, grade FROM mydb.feedback WHERE course_id=?";
    private static final String GET_ALL = "SELECT user_id, course_id, feedback_body, grade FROM mydb.feedback";
    private static final String ADD = "INSERT INTO mydb.feedback(user_id, course_id, feedback_body, grade) VALUES(?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM mydb.feedback WHERE user_id=? AND course_id=?";
    private static final String UPDATE = "UPDATE mydb.feedback SET feedback=?, grade=? WHERE user_id=? AND course_id=?";

    private Connection getConnection() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        return connectionPool.getConnection();
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
        Connection connection = null;
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
            return resultFeedback;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
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
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Feedback> resultList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_BY_USER);
            statement.setInt(1, user.getId());
            courseDaoH2 = new CourseDaoH2Impl();

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback resultFeedback = new Feedback();
                resultFeedback.setUser(user);
                resultFeedback.setCourse(courseDaoH2.getCourseById((resultSet.getInt("course_id"))));
                resultFeedback.setGrade(resultSet.getInt("grade"));
                resultFeedback.setFeedbackBody(resultSet.getString("feedback_body"));
                resultList.add(resultFeedback);
            }
            return resultList;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return resultList;
    }

    /**
     * Return list of Feedback  objects from DataBase corresponding to given Course
     *
     * @param course - given Course object
     * @return list of Feedback objects
     */
    @Override
    public List<Feedback> getFeedbacksByCourse(Course course) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Feedback> resultList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_BY_COURSE);
            statement.setInt(1, course.getId());
            userDaoH2 = new UserDaoH2Impl();

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback resultFeedback = new Feedback();
                resultFeedback.setUser(userDaoH2.getUserById((resultSet.getInt("user_id"))));
                resultFeedback.setCourse(course);
                resultFeedback.setGrade(resultSet.getInt("grade"));
                resultFeedback.setFeedbackBody(resultSet.getString("feedback_body"));
                resultList.add(resultFeedback);
            }
            return resultList;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return resultList;
    }

    /**
     * Return list of all exist Feedback objects
     *
     * @return list of Feedback objects
     */
    @Override
    public List<Feedback> getAllFeedbacks() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Feedback> resultList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_ALL);
            courseDaoH2 = new CourseDaoH2Impl();
            userDaoH2 = new UserDaoH2Impl();

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback resultFeedback = new Feedback();
                resultFeedback.setUser(userDaoH2.getUserById((resultSet.getInt("user_id"))));
                resultFeedback.setCourse(courseDaoH2.getCourseById((resultSet.getInt("course_id"))));
                resultFeedback.setGrade(resultSet.getInt("grade"));
                resultFeedback.setFeedbackBody(resultSet.getString("feedback_body"));
                resultList.add(resultFeedback);
            }
            return resultList;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return resultList;
    }

    /**
     * Add given Feedback object to DataBase
     *
     * @param feedback - given Feedback object
     * @return - true if adding is successful
     */
    @Override
    public boolean addFeedback(Feedback feedback) {
        Connection connection = null;
        PreparedStatement statement = null;
        int rowNumber;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(ADD);
            statement.setInt(1, feedback.getUser().getId());
            statement.setInt(2, feedback.getCourse().getId());
            statement.setString(3, feedback.getFeedbackBody());
            statement.setInt(4, feedback.getGrade());

            rowNumber = statement.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
            return false;
        } finally {
            closeResources(statement, null, connection);
        }
        return rowNumber > 0;
    }

    /**
     * Delete given Feedback object to DataBase
     *
     * @param feedback - given Feedback object
     * @return -  true if deleting is successful
     */
    @Override
    public boolean deleteFeedback(Feedback feedback) {
        Connection connection = null;
        PreparedStatement statement = null;
        int rowNumber;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(DELETE);
            statement.setInt(1, feedback.getUser().getId());
            statement.setInt(2, feedback.getCourse().getId());

            rowNumber = statement.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
            return false;
        } finally {
            closeResources(statement, null, connection);
        }
        return rowNumber > 0;
    }

    /**
     * Update Feedback in database with new Feedback object
     *
     * @param feedback - given Feedback object
     * @return - previous Feedback object from DB
     */
    @Override
    public boolean updateFeedback(Feedback feedback) {
        Connection connection = null;
        PreparedStatement statement = null;
        int rowNumber = 0;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(UPDATE);
            statement.setInt(1, feedback.getUser().getId());
            statement.setInt(2, feedback.getCourse().getId());

            rowNumber = statement.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, null, connection);
        }
        return rowNumber > 0;
    }

    private void closeResources(PreparedStatement statement, ResultSet resultSet, Connection connection) {
        try {
            if (statement != null) statement.close();
            if (resultSet != null) resultSet.close();
            if (connection != null) connection.close();
        } catch (SQLException e1) {
            log.log(Level.ERROR, e1);
            e1.printStackTrace();
        }
    }
}
