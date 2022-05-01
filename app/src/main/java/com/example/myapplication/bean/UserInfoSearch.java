package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.List;

public class UserInfoSearch implements Serializable {
    int sex;
    int age;

    public UserInfoSearch(int sex, int age) {
        this.sex = sex;
        this.age = age;
    }

    public static UserInfoSearch BuildData(List<String> prams)
    {
        String sex = prams.get(0).trim();
        int pram1 = 0, pram2 = Integer.parseInt(prams.get(1));

        if (sex.equals("男"))
            pram1 = 1;
        else
            pram1 = 2;

        UserInfoSearch object = new UserInfoSearch(pram1, pram2);
        return object;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
