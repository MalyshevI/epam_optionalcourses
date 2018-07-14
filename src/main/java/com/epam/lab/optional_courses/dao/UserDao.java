package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUsers();

    //List<User> getUserByName(String name);

    User getUserById(int id);

    //User getByEmailAndPassword(String email, String password);

    boolean addUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(User user);
}
