package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.dao.connectionPools.ConnectionPool;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * That class implements Data Access Object for Feedback
 *
 * @author Anton Kulaga
 */
public class FeedbackDaoImpl implements FeedbackDao {

    private static final Logger log = LogManager.getLogger(FeedbackDaoImpl.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String GET_BY_USER_AND_COURSE;
    private static final String GET_BY_USER;
    private static final String GET_BY_COURSE;
    private static final String GET_ALL;
    private static final String ADD;
    private static final String DELETE;
    private static final String UPDATE;

    private UserDao userDao;
    private CourseDao courseDao;


    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/sql_request_body_Mysql.properties"));
            log.log(Level.INFO, "SQL request bodies for feedbacks loaded successfully");
        } catch (IOException e) {
            log.log(Level.ERROR, "Can't load SQL request bodies for feedbacks", e);
        }
        GET_BY_USER_AND_COURSE = properties.getProperty("GET_FEEDBACK_BY_USER_AND_COURSE");
        GET_BY_USER = properties.getProperty("GET_FEEDBACK_BY_USER");
        GET_BY_COURSE = properties.getProperty("GET_FEEDBACK_BY_COURSE");
        GET_ALL = properties.getProperty("GET_ALL_FEEDBACKS");
        ADD = properties.getProperty("ADD_FEEDBACK");
        DELETE = properties.getProperty("DELETE_FEEDBACK");
        UPDATE = properties.getProperty("UPDATE_FEEDBACK");
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
            connection = connectionPool.getConnection();
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
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(GET_BY_USER);
            statement.setInt(1, user.getId());
            courseDao = new CourseDaoImpl();

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback resultFeedback = new Feedback();
                resultFeedback.setUser(user);
                resultFeedback.setCourse(courseDao.getCourseById((resultSet.getInt("course_id"))));
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
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(GET_BY_COURSE);
            statement.setInt(1, course.getId());
            userDao = new UserDaoImpl();

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback resultFeedback = new Feedback();
                resultFeedback.setUser(userDao.getUserById((resultSet.getInt("user_id"))));
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
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(GET_ALL);
            courseDao = new CourseDaoImpl();
            userDao = new UserDaoImpl();

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback resultFeedback = new Feedback();
                resultFeedback.setUser(userDao.getUserById((resultSet.getInt("user_id"))));
                resultFeedback.setCourse(courseDao.getCourseById((resultSet.getInt("course_id"))));
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
            connection = connectionPool.getConnection();
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
            connection = connectionPool.getConnection();
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
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(UPDATE);
            statement.setString(1, feedback.getFeedbackBody());
            statement.setInt(2, feedback.getGrade());
            statement.setInt(3, feedback.getUser().getId());
            statement.setInt(4, feedback.getCourse().getId());

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
