package com.example.myapplication.chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.HashMap;

public class MyValueFormatter extends ValueFormatter {
    static HashMap<Integer, Integer> QuestionIdToMaxValue = new HashMap<>();
    static{
        QuestionIdToMaxValue.put(1,2);
        QuestionIdToMaxValue.put(3,2);
        QuestionIdToMaxValue.put(5,2);
        QuestionIdToMaxValue.put(6,2);
        QuestionIdToMaxValue.put(7,3);
        QuestionIdToMaxValue.put(8,4);
        QuestionIdToMaxValue.put(9,4);
    }

    int questionid;
    MyValueFormatter(int questionid)
    {
        this.questionid = questionid;
    }

    @Override
    public String getFormattedValue(float value) {
        switch (questionid)
        {
            case 1:
            {
                if (value == 1)
                    return "男";
                else if(value == 2)
                    return "女";
                else
                    return "";
            }
            case 3:
            {
                if (value == 1)
                    return "否";
                else if(value == 2)
                    return "是";
                else
                    return "";
            }
            case 5:
            {
                if (value == 1)
                    return "否";
                else if(value == 2)
                    return "是";
                else
                    return "";
            }
            case 6:
            {
                if (value == 1)
                    return "否";
                else if(value == 2)
                    return "是";
                else
                    return "";
            }
            case 7:
            {
                if (value == 1)
                    return "不够";
                else if(value == 2)
                    return "正好";
                else if(value == 3)
                    return "过多";
                else
                    return "";
            }
            case 8:
            {
                if (value == 1)
                    return "小于5小时";
                else if(value == 2)
                    return "5-6小时";
                else if(value == 3)
                    return "7-8小时";
                else if(value == 4)
                    return "大于8小时";
                else
                    return "";
            }
            case 9:
            {
                if (value == 1)
                    return "很少";
                else if(value == 2)
                    return "有时";
                else if(value == 3)
                    return "经常";
                else if(value == 4)
                    return "总是";
                else
                    return "";
            }
            default:
                return super.getFormattedValue(value);
        }
    }

    public String GetPieChartDec(int value)
    {
        return getFormattedValue(value);
    }
}
