package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Easing;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestGraphActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_graph);
        LineChart lineChart = (LineChart) findViewById(R.id.LineChar);

        lineChart.setData(getLineData());
        lineChart.setDragEnabled(true);
        //Axis setting
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        //Hide grid
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        //Description
        lineChart.getDescription().setTextSize(15);
        lineChart.getDescription().setText("xx数据的图表");
        lineChart.getDescription().setTextColor(Color.RED);

        //X Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(10);
        xAxis.setGranularity(1f);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        lineChart.animateXY(3000,4000);

        button = (Button) findViewById(R.id.movebutton);
        button.setOnClickListener(this);

//        xAxis.setAxisMinimum(1);
//        xAxis.setAxisMaximum(10);
//        xAxis.setLabelCount(10);
//        xAxis.setValueFormatter(new ValueFormatter() {
//            private String[] strs = new String[]{"str0", "str1", "str2", "str3", "str4"};
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                if(value < strs.length)
//                    return strs[(int) value];
//                else
//                    return "null";
//            }
//        });
    }


    private LineData getLineData()
    {
        List<Entry> list = new ArrayList<Entry>();
        list.add(new Entry(1,2));
        list.add(new Entry(2,3));
        list.add(new Entry(3,4));
        list.add(new Entry(4,5));
        Collections.sort(list, new EntryXComparator());
        List<Entry> list2 = new ArrayList<Entry>();
        list2.add(new Entry(2,5));
        list2.add(new Entry(1,4));
        list2.add(new Entry(3,3));
        list2.add(new Entry(4,1));
        Collections.sort(list2, new EntryXComparator());

        LineDataSet dataSet1 = new LineDataSet(list, "company1");
        dataSet1.setColor(Color.YELLOW);
        dataSet1.setCircleColor(Color.BLACK);

        LineDataSet dataSet2 = new LineDataSet(list2, "company2");
        dataSet2.setColor(Color.GREEN);
        dataSet2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getPointLabel(Entry entry) {
                String str = entry.getY() + "--" + entry.getY();
                return str;
            }
        });

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);
        LineData lineData = new LineData(dataSets);
        return lineData;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.movebutton:
                LineChart lineChart = (LineChart) findViewById(R.id.LineChar);
                lineChart.moveViewToX(2);
                break;
            default:
                break;
        }
    }
}