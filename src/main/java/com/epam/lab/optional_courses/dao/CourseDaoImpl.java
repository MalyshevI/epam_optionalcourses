package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.dao.connectionPools.ConnectionPool;
import com.epam.lab.optional_courses.entity.Course;
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
 * The class describes methods which cooperate with database
 *
 * @author Ilia_Malyshev
 */
public class CourseDaoImpl implements CourseDao {

    private static final Logger log = LogManager.getLogger(CourseDaoImpl.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String GET_BY_ID;
    private static final String GET_ALL;
    private static final String GET_BY_TUTOR;
    private static final String GET_BY_USER;
    private static final String ADD_COURSE;
    private static final String DELETE;
    private static final String UPDATE;
    private static final String ENROLL_USER;
    private static final String COUNT_COURSES;
    private static final String IS_USER_ON_COURSE;
    private static final String LEAVE_COURSE;
    private static final String GET_USERS_ON_COURSE;
    private static final String COUNT_USERS_ON_COURSE;
    private static final String COUNT_COURSES_BY_USER;


    static {
        Properties properties = new Properties();
        try {
            properties.load(CourseDaoImpl.class.getClassLoader().getResourceAsStream("sql_request_body_Mysql.properties"));
            log.log(Level.INFO, "SQL request bodies for courses loaded successfully ");
        } catch (IOException e) {
            log.log(Level.ERROR, "Can't load SQL request bodies for courses", e);
        }
        GET_BY_ID = properties.getProperty("GET_COURSE_BY_ID");
        GET_ALL = properties.getProperty("GET_ALL_COURSES");
        GET_BY_TUTOR = properties.getProperty("GET_COURSES_BY_TUTOR");
        GET_BY_USER = properties.getProperty("GET_COURSES_BY_USER");
        ADD_COURSE = properties.getProperty("ADD_COURSE");
        DELETE = properties.getProperty("DELETE_COURSE");
        UPDATE = properties.getProperty("UPDATE_COURSE");
        ENROLL_USER = properties.getProperty("ENROLL_USER_ON_COURSE");
        COUNT_COURSES = properties.getProperty("COUNT_COURSES");
        IS_USER_ON_COURSE = properties.getProperty("IS_USER_ON_COURSE");
        LEAVE_COURSE = properties.getProperty("LEAVE_COURSE");
        GET_USERS_ON_COURSE = properties.getProperty("GET_USERS_ON_COURSE");
        COUNT_USERS_ON_COURSE = properties.getProperty("COUNT_USERS_ON_COURSE");
        COUNT_COURSES_BY_USER = properties.getProperty("COUNT_COURSES_BY_USER");

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

            connection = connectionPool.getConnection();
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
                resultCourse.setTutor(CommonDao.userDao.getUserById(resultSet.getInt("tutor_id")));
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
     * @param limit  - given limit
     * @param offset - given offset
     * @return list of Course objects
     */
    @Override
    public List<Course> getAllCourses(long limit, long offset) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Course> resultList = new ArrayList<>();

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(GET_ALL);
            statement.setLong(1, limit);
            statement.setLong(2, offset);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Course resultCourse = new Course();
                resultCourse.setId(resultSet.getInt("course_id"));
                resultCourse.setCourseName(resultSet.getString("course_name"));
                resultCourse.setStartDate(new java.util.Date(resultSet.getDate("start_date").getTime()));
                resultCourse.setFinishDate(new java.util.Date(resultSet.getDate("finish_date").getTime()));
                resultCourse.setTutor(CommonDao.userDao.getUserById(resultSet.getInt("tutor_id")));
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
     * @param limit  - given limit
     * @param offset - given offset
     * @param tutor  - given object User
     * @return list of Course objects
     */
    @Override
    public List<Course> getCoursesByTutor(User tutor, long limit, long offset) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Course> resultList = new ArrayList<>();

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(GET_BY_TUTOR);
            statement.setInt(1, tutor.getId());
            statement.setLong(2, limit);
            statement.setLong(3, offset);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Course resultCourse = new Course();
                resultCourse.setId(resultSet.getInt("course_id"));
                resultCourse.setCourseName(resultSet.getString("course_name"));
                resultCourse.setStartDate(new java.util.Date(resultSet.getDate("start_date").getTime()));
                resultCourse.setFinishDate(new java.util.Date(resultSet.getDate("finish_date").getTime()));
                resultCourse.setTutor(tutor);
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
     * @param limit  - given limit
     * @param offset - given offset
     * @param user   - given object User
     * @return list of Course objects
     */
    @Override
    public List<Course> getCoursesByUser(User user, long limit, long offset) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Course> resultList = new ArrayList<>();

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(GET_BY_USER);

            statement.setInt(1, user.getId());
            statement.setLong(2, limit);
            statement.setLong(3, offset);
            resultSet = statement.executeQuery();


            while (resultSet.next()) {

                Course resultCourse = new Course();
                resultCourse.setId(resultSet.getInt("course_id"));
                resultCourse.setCourseName(resultSet.getString("course_name"));
                resultCourse.setStartDate(new java.util.Date(resultSet.getDate("start_date").getTime()));
                resultCourse.setFinishDate(new java.util.Date(resultSet.getDate("finish_date").getTime()));
                resultCourse.setTutor(CommonDao.userDao.getUserById(resultSet.getInt("tutor_id")));
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
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(ADD_COURSE, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, course.getCourseName());
            statement.setDate(2, new java.sql.Date(course.getStartDate().getTime()));
            statement.setDate(3, new java.sql.Date(course.getFinishDate().getTime()));
            statement.setInt(4, course.getTutor().getId());
            statement.setInt(5, course.getCapacity());

            rowNumber = statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                course.setId(resultSet.getInt(1));
            }
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
            connection = connectionPool.getConnection();
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
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(UPDATE);

            statement.setString(1, course.getCourseName());
            statement.setDate(2, new java.sql.Date(course.getStartDate().getTime()));
            statement.setDate(3, new java.sql.Date(course.getFinishDate().getTime()));
            statement.setInt(4, course.getTutor().getId());
            statement.setInt(5, course.getCapacity());
            statement.setInt(6, course.getId());

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
     * Enroll user on course
     *
     * @param course - given course that user choose
     * @param user   - given user
     * @return true if user enroll to the course
     */
    @Override
    public boolean enrollUserOnCourse(Course course, User user) {
        Connection connection = null;
        PreparedStatement statement = null;

        int rowNumber;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(ENROLL_USER);

            statement.setInt(1, user.getId());
            statement.setInt(2, course.getId());

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
     * Return count of Courses in database
     *
     * @return number of courses
     */
    @Override
    public long countCourses() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long count = 0;
        try {

            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(COUNT_COURSES);

            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
            return count;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return count;
    }

    /**
     * Return count of Courses applied by user
     *
     * @return number of users
     */
    @Override
    public long countCoursesByUser(User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long count = 0;
        try {

            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(COUNT_COURSES_BY_USER);

            statement.setInt(1,user.getId());
            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
            return count;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return count;
    }

    /**
     * return true if user enrolled on course
     *
     * @param user   - given user
     * @param course - given course
     * @return return true if user enrolled on course
     */
    @Override
    public boolean isUserOnCourse(User user, Course course) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(IS_USER_ON_COURSE);
            statement.setInt(1, user.getId());
            statement.setInt(2, course.getId());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return false;
    }

    /**
     * Leaving course by user
     *
     * @param course - given course
     * @param user   - given user
     * @return - resutl of deleting
     */
    @Override
    public boolean leaveCourse(Course course, User user) {
        Connection connection = null;
        PreparedStatement statement = null;

        int rowNumber;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(LEAVE_COURSE);
            statement.setInt(1, user.getId());
            statement.setInt(2, course.getId());

            rowNumber = statement.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
            return false;
        } finally {
            closeResources(statement, null, connection);
        }
        return rowNumber > 0;
    }

    @Override
    public List<User> getUsersOnCourse(Course course, long limit, long offset) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<User> resultList = new ArrayList<>();
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(GET_USERS_ON_COURSE);
            statement.setInt(1, course.getId());
            statement.setLong(2, limit );
            statement.setLong(3, offset);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setAdmin(resultSet.getBoolean("is_admin"));
                resultList.add(user);
            }
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return resultList;
    }

    @Override
    public long countUsersOnCourse(Course course) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long count = 0;
        try {

            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(COUNT_USERS_ON_COURSE);
            statement.setInt(1, course.getId());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
            return count;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(statement, resultSet, connection);
        }
        return count;
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