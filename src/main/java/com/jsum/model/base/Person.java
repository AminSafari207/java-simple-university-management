package com.jsum.model.base;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person<T extends Person<T>> extends BaseEntity<Long> {
    private String name;

    @Column(unique = true)
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public T name(String name) {
        this.name = name;
        return self();
    }

    public T email(String email) {
        this.email = email;
        return self();
    }

    private T self() {
        return (T) this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nName: " + name +
                "\nEmail: " + email;
    }
}
