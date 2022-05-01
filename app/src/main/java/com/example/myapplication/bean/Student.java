package com.example.myapplication.bean;

public class Student {
    private int id;
    private String name;
    private String number;

    public Student(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String PrintMyself()
    {
        String str = this.getId() + " " + this.name + " " + this.getNumber();
        return str;
    }
}
