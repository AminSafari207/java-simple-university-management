package com.jsum.model;

import com.jsum.model.base.BaseEntity;
import com.jsum.model.person.Professor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course extends BaseEntity<Long> {
    @Column(name="course_id", unique=true)
    private Long courseId;

    private String name;
    private int credits;

    @Transient
    private List<Professor> professors = new ArrayList<>();

    public Long getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public Course courseId(Long id) {
        this.courseId = id;
        return this;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public Course credits(int credits) {
        this.credits = credits;
        return this;
    }

    public Course professors(List<Professor> professors) {
        this.professors = professors;
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nCourse ID: " + id +
                "\nName: " + name +
                "\nCredits: " + credits +
                "\nProfessors: " + professors.stream().map(c -> (c.getName() + ", ")).toList();
    }
}
