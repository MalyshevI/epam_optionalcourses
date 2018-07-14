package com.epam.lab.optional_courses.entity;

import java.util.Objects;

/**
 * The class describes groups, including students from one class.
 *
 * @author Ilia_Malyshev
 */
public class Group {
    private int id;
    private User head;
    private String groupName;

    public Group(int id, User head, String groupName) {
        this.id = id;
        this.head = head;
        this.groupName = groupName;
    }

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getHead() {
        return head;
    }

    public void setHead(User head) {
        this.head = head;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", head=" + head +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
