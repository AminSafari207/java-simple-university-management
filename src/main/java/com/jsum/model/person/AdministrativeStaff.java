package com.jsum.model.person;

import com.jsum.model.base.Person;
import jakarta.persistence.Entity;

@Entity
public class AdministrativeStaff extends Person<AdministrativeStaff> {
    private String position;

    public String getPosition() {
        return position;
    }

    public AdministrativeStaff position(String position) {
        this.position = position;
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nPosition: " + position;
    }
}
