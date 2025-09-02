package com.jsum.model;

import com.jsum.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"course_id","professor_id"}))
public class ProfessorEnrollment extends BaseEntity<Long> {
    @Column(name="course_id")
    private Long courseId;

    @Column(name="professor_id")
    private Long professorId;

    public Long getCourseId() {
        return courseId;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public ProfessorEnrollment courseId(Long courseId) {
        this.courseId = courseId;
        return this;
    }

    public ProfessorEnrollment professorId(Long professorId) {
        this.professorId = professorId;
        return this;
    }
}
