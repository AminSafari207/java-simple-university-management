package com.jsum.model.entity;

import com.jsum.model.base.Person;

public class AdministrativeStaff extends Person<AdministrativeStaff> {
    private String position;

    public String getPosition() {
        return position;
    }

    public AdministrativeStaff position(String position) {
        this.position = position;
        return this;
    }
}
