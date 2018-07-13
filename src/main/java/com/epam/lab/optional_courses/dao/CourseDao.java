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
     * @return list of Course objects
     */
    List<Course> getAllCourses();

    /**
     * Returns the list of courses that the specified user is leading
     * @param tutor - given object User
     * @return list of Course objects
     */
    List<Course> getCourseByTutor(User tutor);

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
    Course updateCourse(Course course);
}