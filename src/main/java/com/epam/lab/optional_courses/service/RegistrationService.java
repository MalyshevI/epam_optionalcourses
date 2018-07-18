package com.epam.lab.optional_courses.service;

import com.epam.lab.optional_courses.dao.UserDaoImpl;
import com.epam.lab.optional_courses.dao.connectionPools.ConnectionPool;
import com.epam.lab.optional_courses.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrationService {
    private static final Logger log = LogManager.getLogger(ConnectionPool.class);
    private static UserDaoImpl userDao = new UserDaoImpl();

    public static boolean checkEmail(String email){
        return userDao.checkForEmail(email);
    }

    public static boolean insertUser(String name, String lastName, String email, String password){
        User user = new User (name, lastName, email, password);
        userDao.addUser(user);
        return userDao.checkForEmailAndPassword(email, password);
    }
}
