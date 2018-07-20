package com.epam.lab.optional_courses.service;

import com.epam.lab.optional_courses.dao.CommonDao;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.User;

import java.util.List;

public class UserService {
    public static List<Course> getCoursesByUser(User user, long limit, long offset){
        return CommonDao.courseDao.getCoursesByUser(user, limit, offset);
    }

    public static long countCoursesByUser(User user){
        return CommonDao.courseDao.countCoursesByUser(user);
    }

    public static User getUserById(String input) {
        try {
            int id = Integer.parseInt(input);
            return CommonDao.userDao.getUserById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean updateUser(User user){
        return CommonDao.userDao.updateUser(user);
    }

    public static long countAllUsers(){
        return CommonDao.userDao.countAllUsers();
    }

    public static List<User> getAllUsers(long limit, long offset){
        return CommonDao.userDao.getAllUsers(limit, offset);
    }

    public static boolean deleteUser(User user){
        return CommonDao.userDao.deleteUser(user);
    }

}
