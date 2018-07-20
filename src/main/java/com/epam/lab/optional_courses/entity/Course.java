package com.epam.lab.optional_courses.entity;


import java.time.LocalDate;
import java.util.Date;

/**
 * The class describes objects which handle a Academic courses
 *
 * @author Nikolai Tikhonov
 * @author Ilia_Malyshev
 */
public class Course {
    private int id;
    private String courseName;
    private LocalDate startDate;
    private LocalDate finishDate;
    private User tutor;
    private int capacity;


    public Course(int id, String courseName, LocalDate startDate, LocalDate finishDate, User tutor, int capacity) {
        this.id = id;
        this.courseName = courseName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.tutor = tutor;
        this.capacity = capacity;
    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", tutor=" + tutor +
                ", capacity=" + capacity +
                '}';
    }
}