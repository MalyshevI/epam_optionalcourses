package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.User;

import java.util.List;

/**
 * The class describes methods which cooperate with database
 *
 * @author Ilia_Malyshev
 */
public interface  CourseDao {

    /**
     * Return Course object from DataBase corresponding to given id
     * @param id - given id of Course
     * @return Course object
     */
    Course getCourseById(int id);

    /**
     * Return list of all exist Course objects
     * @param limit - given limit
     * @param offset - given offset
     * @return list of Course objects
     */
    List<Course> getAllCourses(long limit, long offset);

    /**
     * Returns the list of courses that the specified user is leading
     * @param tutor - given object User
     * @param limit - given limit
     * @param offset - given offset
     * @return list of Course objects
     */
    List<Course> getCoursesByTutor(User tutor, long limit, long offset);

    /**
     * Returns the list of courses that the specified user is leading
     * @param user - given object User
     * @param limit - given limit
     * @param offset - given offset
     * @return list of Course objects
     */
    List<Course> getCoursesByUser(User user, long limit, long offset);

    /**
     * Add given Course object to DataBase
     * @param course - given Course object
     * @return - true if adding is successful
     */
    boolean addCourse(Course course);

    /**
     * Delete given Course object in DataBase
     * @param course - given Course object
     * @return - true if deleting is successful
     */
    boolean deleteCourse(Course course);

    /**
     * Update Course in database with new Course object
     * @param course - given Course object
     * @return - previous Course object from DB
     */
    boolean updateCourse(Course course);

    /**
     * Enroll user on course
     * @param course - given course that user choose
     * @param user - given user
     * @return true if user enroll to the course
     */
    boolean enrollUserOnCourse(Course course, User user);

    /**
     * Return count of Courses in database
     * @return number of courses
     */
    long countCourses();

    /**
     * return true if user enrolled on course
     * @param user - given user
     * @param course - given course
     * @return return true if user enrolled on course
     */
    boolean isUserOnCourse(User user, Course course);

    /**
     * Leaving course by user
     * @param course - given course
     * @param user - given user
     * @return - resutl of deleting
     */
    boolean leaveCourse(Course course, User user);
}