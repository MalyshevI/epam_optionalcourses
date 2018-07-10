package com.epam.lab.optional_courses.entity;


import java.util.Date;

/**
 * The class describes objects which handle a Academic courses
 * @author Nikolai Tikhonov
 */
public class Course {
    private int id;
    private CourseType type;
    private User tutor;
    private Date startDate;
    private Date finishDate;

    public Course(int id, CourseType type, User tutor, Date startDate, Date finishDate) {
        this.id = id;
        this.type = type;
        this.tutor = tutor;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourseType getType() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
