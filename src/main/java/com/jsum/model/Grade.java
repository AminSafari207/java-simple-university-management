package com.jsum.model;

import com.jsum.model.base.BaseEntity;
import com.jsum.enums.GradeType;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","course_id","semester"}))
public class Grade extends BaseEntity<Long> {
    @Column(name="student_id")
    private Long studentId;

    @Column(name="course_id")
    private Long courseId;

    private String semester;

    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    private Double numericValue;
    private String letterValue;

    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getSemester() {
        return semester;
    }

    public GradeType getGradeType() {
        return gradeType;
    }

    public Double getNumericValue() {
        return numericValue;
    }

    public String getLetterValue() {
        return letterValue;
    }

    public Grade studentId(Long studentId) {
        this.studentId = studentId;
        return this;
    }

    public Grade courseId(Long courseId) {
        this.courseId = courseId;
        return this;
    }

    public Grade semester(String semester) {
        this.semester = semester;
        return this;
    }

    public Grade gradeType(GradeType gradeType) {
        this.gradeType = gradeType;
        return this;
    }

    public Grade numericValue(Double numericValue) {
        this.numericValue = numericValue;
        return this;
    }

    public Grade letterValue(String letterValue) {
        this.letterValue = letterValue;
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nGrade ID: " + super.getId() +
                "\nStudent ID: " + studentId +
                "\nCourse ID: " + courseId +
                "\nSemester: " + semester +
                "\nNumeric Grade: " + numericValue +
                "\nLetter Grade: " + letterValue;
    }
}
