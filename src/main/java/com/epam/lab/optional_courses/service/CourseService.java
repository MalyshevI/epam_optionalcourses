package com.epam.lab.optional_courses.service;

import com.epam.lab.optional_courses.controller.Common;
import com.epam.lab.optional_courses.dao.*;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.Feedback;
import com.epam.lab.optional_courses.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

public class CourseService {

    public static List<Course> getAllCourses(long limit, long offset) {
        return CommonDao.courseDao.getAllCourses(limit, offset);
    }

    public static Course getCourseById(String input) {
        try {
            int id = Integer.parseInt(input);
            return CommonDao.courseDao.getCourseById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public static Feedback getFeedbackByUserAndCourse(User user, Course course) {
        return CommonDao.feedbackDao.getFeedbackByUserAndCourse(user, course);
    }

    public static boolean isUserOnCourse(User user, Course course) {
        return CommonDao.courseDao.isUserOnCourse(user, course);
    }

    public static List<Boolean> getCoursesEnrolledByCurUser(User curUser, List<Course> allCourses) {
        List<Boolean> resultList = new ArrayList<>();
        for (Course curCourse: allCourses) {
            if(CommonDao.courseDao.isUserOnCourse(curUser, curCourse)){
                resultList.add(true);
            }else{
                resultList.add(false);
            }
        }
        return resultList;
    }

    public static boolean deleteCourse(Course course){
        return CommonDao.courseDao.deleteCourse(course);
    }

    public static boolean leaveCourse(Course course, User user){
        return CommonDao.courseDao.leaveCourse(course, user);
    }

    public static long countCourses(){
        return  CommonDao.courseDao.countCourses();
    }

    public static List<User> getUsersOnCourse(Course course, long limit, long offset){
        return CommonDao.courseDao.getUsersOnCourse(course, limit, offset);
    }

    public static long countUsersOnCourse(Course course){
        return CommonDao.courseDao.countUsersOnCourse(course);
    }

    public static List<Long> countUsersOnCourseList(List<Course> courseList){
        List<Long> resultList = new ArrayList<>();
        for (Course course: courseList) {
            resultList.add(countUsersOnCourse(course));
        }
        return resultList;
    }

    public static boolean enrollUserOnCourse(Course course, User user){
        return CommonDao.courseDao.enrollUserOnCourse(course, user);
    }

    public static LocalDate dateFromStr(String date){
        String[] splittedDate = date.split("-");
        LocalDate result = null;
        if(splittedDate.length==3){
            try {
                result = LocalDate.of(Integer.parseInt(splittedDate[0]), Integer.parseInt(splittedDate[1]), Integer.parseInt(splittedDate[2]));
            }catch (NumberFormatException e){

            }
        }
        return result;
    }

    public static boolean addCourse(Course course){
        return CommonDao.courseDao.addCourse(course);
    }

    public static boolean editCourse(Course course){
        return CommonDao.courseDao.updateCourse(course);
    }
}
