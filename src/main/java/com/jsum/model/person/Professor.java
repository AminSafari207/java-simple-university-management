package com.jsum.model.entity;

import com.jsum.model.base.Person;

public class Professor extends Person<Professor> {
    private Double salary;

    public Double getSalary() {
        return salary;
    }

    public Professor salary(Double salary) {
        this.salary = salary;
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nSalary: " + salary;
    }
}
