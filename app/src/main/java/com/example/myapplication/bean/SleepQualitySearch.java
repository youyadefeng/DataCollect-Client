package com.example.myapplication.bean;

import com.example.myapplication.helper.NetWorkTask;

import java.io.Serializable;
import java.util.List;

public class SleepQualitySearch implements Serializable {
    int sleepEnough;
    int sleepTime;
    int sleepDifficulty;

    public SleepQualitySearch(int sleepEnough, int sleepTime, int sleepDifficulty) {
        this.sleepEnough = sleepEnough;
        this.sleepTime = sleepTime;
        this.sleepDifficulty = sleepDifficulty;
    }

    public static SleepQualitySearch BuildData(List<String> prams)
    {
        String sleepEnough = prams.get(0).trim();
        String sleepTime = prams.get(1).trim();
        String sleepDifficulty = prams.get(2).trim();

        int pram1 = 0, pram2 = 0, pram3 = 0;
        switch (sleepEnough)
        {
            case "过多":
            {
                pram1 = 1;
                break;
            }
            case "正好":
            {
                pram1 = 2;
                break;
            }
            case "不够":
            {
                pram1 = 3;
                break;
            }
        }

        switch (sleepTime)
        {
            case "大于8小时":
            {
                pram2 = 1;
                break;
            }
            case "7-8小时":
            {
                pram2 = 2;
                break;
            }
            case "5-6小时":
            {
                pram2 = 3;
                break;
            }
            case "小于5小时":
            {
                pram2 = 4;
                break;
            }
        }

        switch (sleepDifficulty)
        {
            case "很少":
            {
                pram3 = 1;
                break;
            }
            case "有时":
            {
                pram3 = 2;
                break;
            }
            case "经常":
            {
                pram3 = 3;
                break;
            }
            case "总是":
            {
                pram3 = 4;
                break;
            }
        }

        NetWorkTask.log("pram1:", pram1 + " ");
        SleepQualitySearch object = new SleepQualitySearch(pram1, pram2, pram3);
        return object;
    }

    public int getSleepEnough() {
        return sleepEnough;
    }

    public void setSleepEnough(int sleepEnough) {
        this.sleepEnough = sleepEnough;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getSleepDifficulty() {
        return sleepDifficulty;
    }

    public void setSleepDifficulty(int sleepDifficulty) {
        this.sleepDifficulty = sleepDifficulty;
    }
}
