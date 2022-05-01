
package com.example.myapplication.chart;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.SubQuestion;
import com.example.myapplication.chartsbase.DemoBase;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.List;

public class AnotherBarActivity extends DemoBase implements OnSeekBarChangeListener {

    private BarChart chart;
    private SeekBar seekBarX;
    private TextView tvX;
    XAxis xAxis;
    YAxis yAxis;

    SubQuestion subQuestion;
    ArrayList<Integer> valueList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetData(getIntent());
        //TestData();
        setTitle("柱状图数据");
        InitChart();
        CustomChart();
    }

    void GetData(Intent intent)
    {
        valueList = intent.getIntegerArrayListExtra("datas");
        subQuestion = (SubQuestion) intent.getSerializableExtra("question");
    }

    void InputSetting()
    {
        yAxis.setGranularityEnabled(false);
        yAxis.setDrawGridLines(false);
        yAxis.setSpaceTop(5f);

        for (IDataSet set : chart.getData().getDataSets())
            set.setDrawValues(true);
    }

    void OptionSetting(int maxValue)
    {
        yAxis.setSpaceTop(10f);
        yAxis.setAxisMinimum(0);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(true);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1f);

        yAxis.setAxisMaximum(maxValue);
    }

    void CustomChart()
    {
        yAxis = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);

        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);

        if (subQuestion.getType() == SubQuestion.QuestionType.Option)
            OptionSetting(MyValueFormatter.QuestionIdToMaxValue.get(subQuestion.getQuestionId()));
        else
            InputSetting();

        MyValueFormatter myValueFormatter = new MyValueFormatter(subQuestion.getQuestionId());
        yAxis.setValueFormatter(myValueFormatter);
    }

    void InitChart()
    {
        tvX = findViewById(R.id.tvXMax);

        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);

        chart.getDescription().setText(subQuestion.getDescription());

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        // setting data
        seekBarX.setMax(valueList.size());
        seekBarX.setProgress(valueList.size()/2);

        // add a nice and smooth animation
        chart.animateY(1500);

        chart.getLegend().setEnabled(false);
    }

    void SetData(int range)
    {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < range && i < valueList.size(); i++) {
            float val = valueList.get(i);
            values.add(new BarEntry(i, val));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);

            chart.setData(data);
            chart.setFitBars(true);
        }

        chart.invalidate();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));

        SetData(seekBarX.getProgress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        menu.removeItem(R.id.actionToggleIcons);
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
//                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/AnotherBarActivity.java"));
//                startActivity(i);
//                break;
//            }
            case R.id.actionToggleValues: {

                for (IDataSet set : chart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                chart.invalidate();
                break;
            }
            /*
            case R.id.actionToggleIcons: { break; }
             */
//            case R.id.actionToggleHighlight: {
//
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
            case R.id.actionToggleBarBorders: {
                for (IBarDataSet set : chart.getData().getDataSets())
                    ((BarDataSet)set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                chart.invalidate();
                break;
            }
            case R.id.animateX: {
                chart.animateX(2000);
                break;
            }
            case R.id.animateY: {
                chart.animateY(2000);
                break;
            }
            case R.id.animateXY: {

                chart.animateXY(2000, 2000);
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
        saveToGallery(chart, "AnotherBarActivity");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
