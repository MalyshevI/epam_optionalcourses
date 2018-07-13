package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.User;

import java.util.List;

/**
 * The class describes methods which cooperate with database
 *
 * @author Ilia_Malyshev
 */
public class CourseDaoImpl implements CourseDao {
    /**
     * Return Course object from DataBase corresponding to given id
     *
     * @param id - given id of Course
     * @return Course object
     */
    @Override
    public Course getCourseByID(int id) {
        return null;
    }

    /**
     * Return list of all exist Course objects
     *
     * @return list of Course objects
     */
    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    /**
     * Returns the list of courses that the specified user is leading
     *
     * @param tutor - given object User
     * @return list of Course objects
     */
    @Override
    public List<Course> getCourseByTutor(User tutor) {
        return null;
    }

    /**
     * Add given Course object to DataBase
     *
     * @param course - given Course object
     * @return - true if adding is successful
     */
    @Override
    public boolean addCourse(Course course) {
        return false;
    }

    /**
     * Delete given Course object in DataBase
     *
     * @param course - given Course object
     * @return - true if deleting is successful
     */
    @Override
    public boolean deleteCourse(Course course) {
        return false;
    }

    /**
     * Update Course in database with new Course object
     *
     * @param course - given Course object
     * @return - previous Course object from DB
     */
    @Override
    public Course updateCourse(Course course) {
        return null;
    }
}
