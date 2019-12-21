package com.example.kursovaya_grapher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GrapherActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grapher);
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        Bundle bundle = this.getIntent().getExtras();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        ArrayList<LineGraphSeries<DataPoint>> seriesList = new ArrayList<>();
        ArrayList<ArrayList<DataPoint>> pointsList = new ArrayList<>();
        String[] Functions = bundle.getStringArray("functions");

        int counter = 0;
        try {
            for (int i = 0; i < Functions.length; i++) {
                counter = i;
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                ArrayList<DataPoint> pointsForSeries = new ArrayList<DataPoint>();
                for (double x = -8; x <= 8; x += 0.0001) {
                    Expression e = new ExpressionBuilder(Functions[i])
                            .variables("x")
                            .build()
                            .setVariable("x", x);
                    double y = e.evaluate();
                    DataPoint dataPoint = new DataPoint(Math.round(x * 1000.0) / 1000.0, Math.round(y * 1000.0) / 1000.0);
                    pointsForSeries.add(dataPoint);
                    series.appendData(dataPoint, true, 1000000);
                    series.setColor(colors[i]);
                    series.setTitle("y=" + Functions[i]);
                }
                seriesList.add(i, series);
                pointsList.add(i, pointsForSeries);
            }
        } catch (IllegalArgumentException er1) {
            Toast.makeText(getApplicationContext(), "Невозможно распознать функцию y=" + Functions[counter] + "! Возврат...",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        boolean p_search = false;
            PointsGraphSeries<DataPoint> seriesX = new PointsGraphSeries<>();
            if (pointsList.size() != 1) {

                ArrayList<DataPoint> non_sortedPoints = new ArrayList<>();
                seriesX.setShape(PointsGraphSeries.Shape.POINT);
                seriesX.setTitle("Т.перес.");
                seriesX.setColor(Color.BLACK);
                seriesX.setSize(15);
                seriesX.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(getApplicationContext(), "Координаты точки пересечения \n[x/y]: " + dataPoint, Toast.LENGTH_SHORT).show();
                    }
                });
                for (int i = 0; i < pointsList.size() - 1; i++) {
                    for (int j = i + 1; j < pointsList.size(); j++) {
                        for (int i1 = 0; i1 < pointsList.get(i).size(); i1++) {
                            if (pointsList.get(i).get(i1).getX() == pointsList.get(j).get(i1).getX() &&
                                    pointsList.get(i).get(i1).getY() == pointsList.get(j).get(i1).getY()) {
                                double x_point = pointsList.get(i).get(i1).getX();
                                double y_point = pointsList.get(i).get(i1).getY();
                                non_sortedPoints.add(new DataPoint(x_point, y_point));
                                p_search = true;

                            }
                        }
                    }
                }
                Collections.sort(non_sortedPoints, new Comparator<DataPoint>() {
                    @Override
                    public int compare(DataPoint o1, DataPoint o2) {
                        return Double.compare(o1.getX(), o2.getX());
                    }
                });
                for (int i = 0; i < non_sortedPoints.size(); i++) {
                    seriesX.appendData(non_sortedPoints.get(i), true, 1000);
                }
            }
            if(p_search){
                graph.addSeries(seriesX);
            }
        for (int i = 0; i < seriesList.size(); i++)
            graph.addSeries(seriesList.get(i));
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setTextSize(40);
        graph.getLegendRenderer().setBackgroundColor(Color.WHITE);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(-5);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-3);
        graph.getViewport().setMaxY(3);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
    }
}



