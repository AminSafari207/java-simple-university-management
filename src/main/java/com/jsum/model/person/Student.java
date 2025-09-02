package com.jsum.model.person;

import com.jsum.model.base.Person;
import com.jsum.model.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends Person<Student> {
    private String major;

    private List<Course> enrolledCourses = new ArrayList<>();

    @Transient
    private boolean isActive;

    @Transient
    private Double gpa;

    public String getMajor() {
        return major;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public boolean isActive() {
        return isActive;
    }

    public Double getGpa() {
        return gpa;
    }

    public Student major(String major) {
        this.major = major;
        return this;
    }

    public Student enrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
        return this;
    }

    public Student isActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public Student gpa(Double gpa) {
        this.gpa = gpa;
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nMajor: " + major +
                "\nEnrolled Course: " + enrolledCourses.stream().map(c -> (c.getId() + ", ")).toList();
    }
}
