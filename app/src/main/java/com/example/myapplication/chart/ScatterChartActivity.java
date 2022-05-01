
package com.example.myapplication.chart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.SubQuestion;
import com.example.myapplication.chartsbase.DemoBase;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.List;

public class ScatterChartActivity extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private ScatterChart chart;
    private SeekBar seekBarX;
    private TextView tvX;
    XAxis xAxis;
    YAxis yAxis;

    List<Integer> valueList = new ArrayList<>();
    SubQuestion subQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scatterchart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("散点图数据");

        GetData(getIntent());
        //TestData();
        InitChart();
        CustomChart();
    }

    void InitChart()
    {
        tvX = findViewById(R.id.tvXMax);

        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);
        chart.getDescription().setText(subQuestion.getDescription());
        chart.setOnChartValueSelectedListener(this);

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setMaxHighlightDistance(50f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        chart.setMaxVisibleValueCount(200);
        chart.setPinchZoom(true);

        seekBarX.setMax(valueList.size());
        seekBarX.setProgress(valueList.size()/2);

        chart.getLegend().setEnabled(false);
    }


    void InputSetting()
    {

    }

    void OptionSetting(int maxValue)
    {
//        yAxis.setSpaceTop(10f);
//        yAxis.setAxisMinimum(0);
//        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        yAxis.setDrawGridLines(true);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1f);
//
        //yAxis.setAxisMaximum(maxValue);
    }

    void CustomChart()
    {
        yAxis = chart.getAxisLeft();
        yAxis.setTypeface(tfLight);
        chart.getAxisRight().setEnabled(false);
        yAxis.enableGridDashedLine(10f, 10f, 0f);

        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);

        if (subQuestion.getType() == SubQuestion.QuestionType.Option)
            OptionSetting(MyValueFormatter.QuestionIdToMaxValue.get(subQuestion.getQuestionId()));
        else
            InputSetting();

        MyValueFormatter myValueFormatter = new MyValueFormatter(subQuestion.getQuestionId());
        yAxis.setValueFormatter(myValueFormatter);

    }

    void GetData(Intent intent)
    {
        subQuestion = (SubQuestion) intent.getSerializableExtra("question");
        valueList = intent.getIntegerArrayListExtra("datas");
    }

    void TestData()
    {
        subQuestion = new SubQuestion(2, "sss", SubQuestion.QuestionType.Option);
        valueList.add(42);
        valueList.add(23);
        valueList.add(34);
        valueList.add(33);
        valueList.add(12);
        valueList.add(3);
        valueList.add(5);
        valueList.add(11);
        valueList.add(50);
    }

    void SetData(int range)
    {
        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 0; i < range && i <valueList.size(); i++) {
            float val = valueList.get(i);
            values1.add(new Entry(i, val));
        }

        // create a dataset and give it a type
        ScatterDataSet set1 = new ScatterDataSet(values1, "Data Set");
        set1.setScatterShape(ScatterChart.ScatterShape.SQUARE);
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);

        set1.setScatterShapeSize(8f);

        ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        ScatterData data = new ScatterData(dataSets);
        data.setValueTypeface(tfLight);

        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));

        SetData(seekBarX.getProgress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scatter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
//            case R.id.viewGithub: {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/ScatterChartActivity.java"));
//                startActivity(i);
//                break;
//            }
            case R.id.actionToggleValues: {
                List<IScatterDataSet> sets = chart.getData()
                        .getDataSets();

                for (IScatterDataSet iSet : sets) {

                    ScatterDataSet set = (ScatterDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
//            case R.id.actionToggleHighlight: {
//                if(chart.getData() != null) {
//                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
//                    chart.invalidate();
//                }
//                break;
//            }
            case R.id.actionTogglePinch: {
                if (chart.isPinchZoomEnabled())
                    chart.setPinchZoom(false);
                else
                    chart.setPinchZoom(true);

                chart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
                chart.notifyDataSetChanged();
                break;
            }
            case R.id.animateX: {
                chart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                chart.animateY(3000);
                break;
            }
            case R.id.animateXY: {

                chart.animateXY(3000, 3000);
                break;
            }
//            case R.id.actionSave: {
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGallery();
//                } else {
//                    requestStoragePermission(chart);
//                }
//                break;
//            }
        }
        return true;
    }

    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "ScatterChartActivity");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
