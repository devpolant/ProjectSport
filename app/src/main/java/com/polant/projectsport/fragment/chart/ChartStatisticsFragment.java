package com.polant.projectsport.fragment.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.polant.projectsport.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Random;

/**
 * Created by Антон on 25.10.2015.
 */
public class ChartStatisticsFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_statistics_chart;

    //Константа, которая определяет интервал статистики.
    public static final String STATISTICS_INTERVAL = "STATISTICS_INTERVAL";
    public static final int STATISTICS_WEEK = 7;
    public static final int STATISTICS_MONTH = 30;

    private View view;
    private int interval;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }

    public static ChartStatisticsFragment getInstance(int intervalValue){
        Bundle args = new Bundle();
        args.putInt(STATISTICS_INTERVAL, intervalValue);

        ChartStatisticsFragment fragment = new ChartStatisticsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null){
            interval = getArguments().getInt(STATISTICS_INTERVAL);
        }
        initChart();
    }

    //Инициализирую график.
    private void initChart() {
        XYSeries series = new XYSeries(getString(R.string.text_your_statistics));
        Random r = new Random();
        for (int i = 0; i < 30; i++) {
            series.add(i, i + r.nextInt(10));
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        //Визуализация.
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        if (interval == STATISTICS_WEEK) {
            renderer.setColor(Color.RED);
        }
        else{
            renderer.setColor(Color.BLUE);
        }
        //Include low and max value.
        renderer.setDisplayBoundingPoints(true);

        //Add point markers.
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        //We want to avoid black border.
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins.
        //Disable Pan on two axis.
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);
        //We show the grid.
        mRenderer.setShowGrid(true);

        //Получаю ссылку на контейнер, который содержит график.
        LinearLayout chartLayout = (LinearLayout) view.findViewById(R.id.chart);

        //Получаю само Представление графика.
        GraphicalView chartView;
        if (interval == STATISTICS_WEEK) {
            //Линейный график
            chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);
        }
        else{
            //Диаграмма.
            chartView = ChartFactory.getBarChartView(getActivity(), dataset, mRenderer, BarChart.Type.DEFAULT);
        }
        //Добавляю график в лайаут.
        chartLayout.addView(chartView, 0);
    }
}
