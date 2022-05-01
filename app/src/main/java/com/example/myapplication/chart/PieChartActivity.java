
package com.example.myapplication.chart;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.myapplication.R;
import com.example.myapplication.bean.SubQuestion;
import com.example.myapplication.chartsbase.DemoBase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartActivity extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private PieChart chart;
    private SeekBar seekBarX;
    private TextView tvX;

    SubQuestion subQuestion;
    HashMap<Integer, Integer> hashMap = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_piechart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("饼状图数据");

        GetData(getIntent());
        InitChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void GetData(Intent intent)
    {
        List<Integer> datas = intent.getIntegerArrayListExtra("datas");
        subQuestion = (SubQuestion) intent.getSerializableExtra("question");

        for (int data: datas) {
            if (hashMap.containsKey(data))
            {
                int oldTimes = hashMap.get(data);
                hashMap.replace(data, oldTimes+1);
            }
            else
            {
                hashMap.put(data, 1);
            }
        }
    }

    void InitChart()
    {
        tvX = findViewById(R.id.tvXMax);

        seekBarX = findViewById(R.id.seekBar1);

        seekBarX.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextTypeface(tfLight);
        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        seekBarX.setMax(hashMap.size());
        seekBarX.setProgress(1);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.GRAY);
        chart.setEntryLabelTypeface(tfLight);
        chart.setEntryLabelTextSize(12f);
    }

    private void setData(int count) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        int index = 0;
        if (subQuestion.getType() == SubQuestion.QuestionType.Input)
        {
            for (Map.Entry<Integer, Integer> data:hashMap.entrySet()) {
                //piechart show the ratio of different data. xValue should be the times of data appear
                if (index++ >= count)
                    break;
                int times = data.getValue();
                int value = data.getKey();
                String dec = String.valueOf(value);
                dec = "\"" + dec + "\"";
                entries.add(new PieEntry(times, dec, getResources().getDrawable(R.drawable.star)));
            }
        }
        else
        {
            MyValueFormatter valueFormatter = new MyValueFormatter(subQuestion.getQuestionId());
            for (Map.Entry<Integer, Integer> data:hashMap.entrySet()) {
                if (index++ >= count)
                    break;
                int times = data.getValue();
                int value = data.getKey();
                String dec = valueFormatter.GetPieChartDec(value);
                entries.add(new PieEntry(times, dec, getResources().getDrawable(R.drawable.star)));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);
        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
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
//                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/PieChartActivity.java"));
//                startActivity(i);
//                break;
//            }
            case R.id.actionToggleValues: {
                for (IDataSet<?> set : chart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                chart.invalidate();
                break;
            }
//            case R.id.actionToggleIcons: {
//                for (IDataSet<?> set : chart.getData().getDataSets())
//                    set.setDrawIcons(!set.isDrawIconsEnabled());
//
//                chart.invalidate();
//                break;
//            }
            case R.id.actionToggleHole: {
                if (chart.isDrawHoleEnabled())
                    chart.setDrawHoleEnabled(false);
                else
                    chart.setDrawHoleEnabled(true);
                chart.invalidate();
                break;
            }
//            case R.id.actionToggleMinAngles: {
//                if (chart.getMinAngleForSlices() == 0f)
//                    chart.setMinAngleForSlices(36f);
//                else
//                    chart.setMinAngleForSlices(0f);
//                chart.notifyDataSetChanged();
//                chart.invalidate();
//                break;
//            }
            case R.id.actionToggleCurvedSlices: {
                boolean toSet = !chart.isDrawRoundedSlicesEnabled() || !chart.isDrawHoleEnabled();
                chart.setDrawRoundedSlices(toSet);
                if (toSet && !chart.isDrawHoleEnabled()) {
                    chart.setDrawHoleEnabled(true);
                }
                if (toSet && chart.isDrawSlicesUnderHoleEnabled()) {
                    chart.setDrawSlicesUnderHole(false);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionDrawCenter: {
                if (chart.isDrawCenterTextEnabled())
                    chart.setDrawCenterText(false);
                else
                    chart.setDrawCenterText(true);
                chart.invalidate();
                break;
            }
            case R.id.actionToggleXValues: {

                chart.setDrawEntryLabels(!chart.isDrawEntryLabelsEnabled());
                chart.invalidate();
                break;
            }
//            case R.id.actionTogglePercent:
//                chart.setUsePercentValues(!chart.isUsePercentValuesEnabled());
//                chart.invalidate();
//                break;
            case R.id.animateX: {
                chart.animateX(1400);
                break;
            }
            case R.id.animateY: {
                chart.animateY(1400);
                break;
            }
            case R.id.animateXY: {
                chart.animateXY(1400, 1400);
                break;
            }
            case R.id.actionToggleSpin: {
                chart.spin(1000, chart.getRotationAngle(), chart.getRotationAngle() + 360, Easing.EaseInOutCubic);
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));

        setData(seekBarX.getProgress());
    }

    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "PieChartActivity");
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString(subQuestion.getDescription());
        s.setSpan(new RelativeSizeSpan(1.2f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
        //s.setSpan(new RelativeSizeSpan(.8f), 0, s.length(), 0);
        //s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
