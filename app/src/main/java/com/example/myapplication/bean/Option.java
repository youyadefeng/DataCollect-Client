package com.example.myapplication.bean;

public class Option {
    int optionId;
    int belongedQuestionId;
    String description;

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getBelongedQuestionId() {
        return belongedQuestionId;
    }

    public void setBelongedQuestionId(int belongedQuestionId) {
        this.belongedQuestionId = belongedQuestionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Option(int optionId, int belongedQuestionId, String description) {
        this.optionId = optionId;
        this.belongedQuestionId = belongedQuestionId;
        this.description = description;
    }
}
