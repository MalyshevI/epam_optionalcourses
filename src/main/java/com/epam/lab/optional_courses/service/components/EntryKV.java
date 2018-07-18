package com.epam.lab.optional_courses.service.components;

public class EntryKV {
    private String name;
    private String value;

    public EntryKV(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
