package com.jsum.model.entity;

import com.jsum.model.base.Person;
import jakarta.persistence.Entity;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends Person<Student> {
    private String major;

    private List<Course> enrolledCourses = new ArrayList<>();

    public String getMajor() {
        return major;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public Student major(String major) {
        this.major = major;
        return this;
    }

    public Student enrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
        return this;
    }
}
