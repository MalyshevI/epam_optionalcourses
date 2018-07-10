package com.epam.lab.optional_courses.entity;

import lombok.*;

/**
 * That class represent feedback
 *
 * @author Anton Kulaga
 */

@Getter
@Setter
@AllArgsConstructor
public class Feedback {
    private int feedbackId;
    private int userId;
    private int tutorId;
    private int grade;
    private String feedbackBody;

}
