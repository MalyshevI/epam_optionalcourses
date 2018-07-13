package com.epam.lab.optional_courses.entity;

import java.util.Objects;

/**
 * This class describes user entity
 *
 * @Author Dolgov Vladimir
 */

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private char[] password;
    private boolean isAdmin;
    private int groupId;

    public User(int id, String firstName, String lastName, String email, char[] password, boolean isAdmin, int groupId){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.groupId = groupId;
    }

    public User() {
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public char[] getPassword(){
        return password;
    }

    public void setPassword(char[] password){
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User that = (User) obj;
        return id == that.id &&
                email == that.email &&
                password == that.password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
