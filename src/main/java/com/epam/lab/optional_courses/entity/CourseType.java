package com.epam.lab.optional_courses.entity;

import java.util.Objects;

/**
 * The class describes objects which handle a types of courses
 * @author Nikolai Tikhonov
 */
public class CourseType {
    private int id;
    private int duration;
    private String name;

    public CourseType(int id, int duration, String name) {
        this.id = id;
        this.duration = duration;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseType that = (CourseType) o;
        return id == that.id &&
                duration == that.duration &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, duration, name);
    }
}
