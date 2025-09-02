package com.jsum.model.base;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity<ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    public ID getId() {
        return id;
    }

    protected void setId(ID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID: " + id;
    }
}
