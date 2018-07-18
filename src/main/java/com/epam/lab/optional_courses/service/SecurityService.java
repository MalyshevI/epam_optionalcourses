package com.epam.lab.optional_courses.service;

import com.epam.lab.optional_courses.dao.UserDaoImpl;
import com.epam.lab.optional_courses.dao.connectionPools.ConnectionPool;
import com.epam.lab.optional_courses.entity.User;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class SecurityService {
    private static final Logger log = LogManager.getLogger(ConnectionPool.class);
    private static UserDaoImpl userDao = new UserDaoImpl();

    public static String hash(String password){
        Properties properties = new Properties();
        try {
            properties.load(SecurityService.class.getClassLoader().getResourceAsStream("security.properties"));
        } catch (IOException e) {
            log.log(Level.ERROR, "Can't open the authorization properties", e);
        }
        String salt = properties.getProperty("salt");
        String passwordSalt = password + salt;
        HashFunction hashFunction = Hashing.md5();

        return hashFunction.hashString(passwordSalt).toString();
    }

    public static boolean login(String email, String password) {
        String passwordHash = hash(password);
        return userDao.checkForEmailAndPassword(email, passwordHash);

    }

    public static User getUserByCreds(String email, String password) {
        return userDao.getByEmailAndPassword(email, password);
    }
}
