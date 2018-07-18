package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers(long offset, long limit);

    long countAllUsers();

    //List<User> getUserByName(String name);

    User getUserById(int id);

    boolean checkForEmailAndPassword(String email, String password);

    boolean checkForEmail(String email);

    User getByEmailAndPassword(String email, String password);

    boolean addUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(User user);
}
