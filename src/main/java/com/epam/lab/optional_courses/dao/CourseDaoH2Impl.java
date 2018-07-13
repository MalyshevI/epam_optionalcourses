package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.dao.connectionPools.ConnectionPool;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The class describes methods which cooperate with database
 *
 * @author Ilia_Malyshev
 */
public class CourseDaoH2Impl implements CourseDao {

    private UserDaoH2Impl userDaoH2;
    private CourseDaoH2Impl courseDaoH2;


    private final Logger log = LogManager.getLogger(CourseDaoH2Impl.class);

    private static final String GET_BY_ID = "SELECT course_id, course_name, start_date, finish_date, tutor_id, capacity FROM courses WHERE course_id=?";
    private static final String GET_ALL = "SELECT course_id, course_name, start_date, finish_date, tutor_id, capacity FROM courses";
    private static final String GET_BY_TUTOR = "SELECT course_id, course_name, start_date, finish_date, tutor_id, capacity FROM courses WHERE tutor_id=?";
    private static final String GET_BY_USER = "SELECT course_id, course_name, start_date, finish_date, tutor_id, capacity FROM courses WHERE tutor_id=?";

    private static final String ADD_COURSE = "INSERT INTO courses(course_name, start_date, finish date, tutor_id, capacity) VALUES(?, ?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM courses WHERE course_id=?";
    private static final String UPDATE = "UPDATE courses SET course_name=?, start_date=?, finish_date=?, tutor_id=?, capacity=? WHERE course_id=?";

    private Connection getConnection() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        return connectionPool.getConnection();
    }

    /**
     * Return Course object from DataBase corresponding to given id
     *
     * @param id - given id of Course
     * @return Course object
     */
    @Override
    public Course getCourseById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {

            connection = getConnection();

            statement = connection.prepareStatement(GET_BY_ID);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            Course resultCourse;

            if (resultSet.next()) {
                resultCourse = new Course();
                resultCourse.setId(id);
                resultCourse.setCourseName(resultSet.getString("course_name"));
                resultCourse.setStartDate(new java.util.Date(resultSet.getDate("start_date").getTime()));
                resultCourse.setFinishDate(new java.util.Date(resultSet.getDate("finish_date").getTime()));
                resultCourse.setTutor(userDaoH2.getUserById(resultSet.getInt("tutor_id")));
                resultCourse.setCapacity(resultSet.getInt("capacity"));
            } else {
                resultCourse = null;
            }
            return resultCourse;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return null;
    }

    /**
     * Return list of all exist Course objects
     *
     * @return list of Course objects
     */
    @Override
    public List<Course> getAllCourses() {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Course> resultList = new ArrayList<>();

        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_ALL);
            userDaoH2 = new UserDaoH2Impl();

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Course resultCourse = new Course();
                resultCourse.setId(resultSet.getInt("course_id"));
                resultCourse.setCourseName(resultSet.getString("course_name"));
                resultCourse.setStartDate(new java.util.Date(resultSet.getDate("start_date").getTime()));
                resultCourse.setFinishDate(new java.util.Date(resultSet.getDate("finish_date").getTime()));
                resultCourse.setTutor(userDaoH2.getUserById(resultSet.getInt("tutor_id")));
                resultCourse.setCapacity(resultSet.getInt("capacity"));

                resultList.add(resultCourse);
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
     * Returns the list of courses that the specified user is leading
     *
     * @param tutor - given object User
     * @return list of Course objects
     */
    @Override
    public List<Course> getCoursesByTutor(User tutor) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Course> resultList = new ArrayList<>();

        try {

            statement = connection.prepareStatement(GET_BY_TUTOR);
            userDaoH2 = new UserDaoH2Impl();

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Course resultCourse = new Course();
                resultCourse.setId(resultSet.getInt("course_id"));
                resultCourse.setCourseName(resultSet.getString("course_name"));
                resultCourse.setStartDate(new java.util.Date(resultSet.getDate("start_date").getTime()));
                resultCourse.setFinishDate(new java.util.Date(resultSet.getDate("finish_date").getTime()));
                resultCourse.setTutor(userDaoH2.getUserById(resultSet.getInt("tutor_id")));
                resultCourse.setCapacity(resultSet.getInt("capacity"));

                resultList.add(resultCourse);
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
     * Returns the list of courses that the specified user is leading
     *
     * @param user - given object User
     * @return list of Course objects
     */
    @Override
    public List<Course> getCoursesByUser(User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Course> resultList = new ArrayList<>();

        try {

            statement = connection.prepareStatement(GET_BY_USER);
            userDaoH2 = new UserDaoH2Impl();

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Course resultCourse = new Course();
                resultCourse.setId(resultSet.getInt("course_id"));
                resultCourse.setCourseName(resultSet.getString("course_name"));
                resultCourse.setStartDate(new java.util.Date(resultSet.getDate("start_date").getTime()));
                resultCourse.setFinishDate(new java.util.Date(resultSet.getDate("finish_date").getTime()));
                resultCourse.setTutor(userDaoH2.getUserById(resultSet.getInt("tutor_id")));  //wa
                resultCourse.setCapacity(resultSet.getInt("capacity"));

                resultList.add(resultCourse);
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
     * Add given Course object to DataBase
     *
     * @param course - given Course object
     * @return - true if adding is successful
     */
    @Override
    public boolean addCourse(Course course) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int rowNumber;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(ADD_COURSE, Statement.RETURN_GENERATED_KEYS);


            statement.setString(1, course.getCourseName());
            statement.setDate(2, new java.sql.Date(course.getStartDate().getTime()));
            statement.setDate(3, new java.sql.Date(course.getFinishDate().getTime()));
            statement.setInt(4, course.getTutor().getId());
            statement.setInt(5, course.getCapacity());

            rowNumber = statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            course.setId(resultSet.getInt("course_id"));
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
            return false;
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return rowNumber > 0;
    }

    /**
     * Delete given Course object in DataBase
     *
     * @param course - given Course object
     * @return - true if deleting is successful
     */
    @Override
    public boolean deleteCourse(Course course) {
        Connection connection = null;
        PreparedStatement statement = null;

        int rowNumber;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(DELETE);

            statement.setInt(1, course.getId());

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
     * Update Course in database with new Course object
     *
     * @param course - given Course object
     * @return - previous Course object from DB
     */
    @Override
    public boolean updateCourse(Course course) {
        Connection connection = null;
        PreparedStatement statement = null;

        int rowNumber;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(UPDATE);


            statement.setString(1, course.getCourseName());
            statement.setDate(2, new java.sql.Date(course.getStartDate().getTime()));
            statement.setDate(3, new java.sql.Date(course.getFinishDate().getTime()));
            statement.setInt(4, course.getTutor().getId());
            statement.setInt(5, course.getCapacity());

            rowNumber = statement.executeUpdate();


        } catch (SQLException e) {
            log.log(Level.ERROR, e);
            return false;
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