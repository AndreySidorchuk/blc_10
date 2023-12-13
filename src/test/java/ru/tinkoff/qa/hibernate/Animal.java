package ru.tinkoff.qa.hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="animal")
public class Animal {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "\"name\"")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "\"type\"")
    private int type;

    @Column(name = "sex")
    private int sex;

    @Column(name = "place")
    private int place;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return sex;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Integer getPlace() {
        return place;
    }
}
