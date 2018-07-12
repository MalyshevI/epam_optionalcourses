package com.epam.lab.optional_courses.entity;

import java.util.Objects;

/**
 * That class represent feedback
 *
 * @author Anton Kulaga
 */


public class Feedback {
    private int userId;
    private int courseId;
    private int grade;
    private String feedbackBody;

    public Feedback(int userId, int courseId, int grade, String feedbackBody) {
        this.userId = userId;
        this.courseId = courseId;
        this.grade = grade;
        this.feedbackBody = feedbackBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return (userId == feedback.userId) && (courseId == feedback.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, courseId);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getFeedbackBody() {
        return feedbackBody;
    }

    public void setFeedbackBody(String feedbackBody) {
        this.feedbackBody = feedbackBody;
    }
}
