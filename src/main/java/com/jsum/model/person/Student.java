package com.jsum.model.person;

import com.jsum.model.base.Person;
import com.jsum.model.Course;
import jakarta.persistence.Entity;

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

    @Override
    public String toString() {
        return super.toString() +
                "\nMajor: " + major +
                "\nEnrolled Course: " + enrolledCourses.stream().map(c -> (c.getCourseId() + ", ")).toList();
    }
}
