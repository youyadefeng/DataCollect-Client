package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.List;

public class EpidemicInfoSearch implements Serializable {
    int inRiskArea;
    int temperature;
    int isFever;
    int isContact;

    public EpidemicInfoSearch(int inRiskArea, int temperature, int isFever, int isContact) {
        this.inRiskArea = inRiskArea;
        this.temperature = temperature;
        this.isFever = isFever;
        this.isContact = isContact;
    }

    public static EpidemicInfoSearch BuildData(List<String> prams)
    {
        String riskArea = prams.get(0).trim();
        int temp = Integer.parseInt(prams.get(1));
        String isFever = prams.get(2).trim();
        String isContact = prams.get(3).trim();

        int pram1 = 0, pram2 = temp, pram3 = 0, pram4 = 0;
        if (riskArea.equals("否"))
            pram1 = 1;
        else
            pram1 = 2;

        if (isFever.equals("否"))
            pram3 = 1;
        else
            pram3 = 2;

        if(isContact.equals("否"))
            pram4 = 1;
        else
            pram4 = 2;

        EpidemicInfoSearch object = new EpidemicInfoSearch(pram1, pram2, pram3, pram4);
        return object;
    }

    public int getInRiskArea() {
        return inRiskArea;
    }

    public void setInRiskArea(int inRiskArea) {
        this.inRiskArea = inRiskArea;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getIsFever() {
        return isFever;
    }

    public void setIsFever(int isFever) {
        this.isFever = isFever;
    }

    public int getIsContact() {
        return isContact;
    }

    public void setIsContact(int isContact) {
        this.isContact = isContact;
    }
}
