package com.jsum.model;

import com.jsum.model.enums.GradeType;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","course_code","semester"}))
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="student_id")
    private Long studentId;

    @Column(name="course_id")
    private Long courseId;

    private String semester;

    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    private Double numericValue;
    private String letterValue;

    public Long getId() {
        return id;
    }

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

    public Grade setId(Long id) {
        this.id = id;
        return this;
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
                "\nGrade ID: " + id +
                "\nStudent ID: " + studentId +
                "\nCourse ID: " + courseId +
                "\nSemester: " + semester +
                "\nNumeric Grade: " + numericValue +
                "\nLetter Grade: " + letterValue;
    }
}
