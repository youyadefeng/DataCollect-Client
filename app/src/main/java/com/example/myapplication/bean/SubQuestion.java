package com.example.myapplication.bean;

import java.io.Serializable;

public class SubQuestion implements Serializable {
    int questionId;
    String description;
    QuestionType type;
    public enum QuestionType
    {
        Option,
        Input;
    }

    public SubQuestion(int questionId, String description, QuestionType type) {
        this.questionId = questionId;
        this.description = description;
        this.type = type;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }
}
