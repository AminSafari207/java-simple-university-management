package com.jsum.model.course;

import com.jsum.model.person.Professor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Course courseId(Long courseId) {
        this.courseId = courseId;
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
}
