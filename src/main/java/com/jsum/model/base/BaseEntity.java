package com.jsum.model.base;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity<ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BaseEntityGenerator")
    @SequenceGenerator(name = "BaseEntityGenerator", sequenceName = "base-seq", allocationSize = 1)
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
