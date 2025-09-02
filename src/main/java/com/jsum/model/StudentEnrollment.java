package com.jsum.model;

import com.jsum.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","course_id","semester"}))
public class StudentEnrollment extends BaseEntity<Long> {
    @Column(name="student_id")
    private Long studentId;

    @Column(name="course_id")
    private Long courseId;

    private String semester;

    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getSemester() {
        return semester;
    }

    public StudentEnrollment studentId(Long studentId) {
        this.studentId = studentId;
        return this;
    }

    public StudentEnrollment courseId(Long courseId) {
        this.courseId = courseId;
        return this;
    }

    public StudentEnrollment semester(String semester) {
        this.semester = semester;
        return this;
    }
}
