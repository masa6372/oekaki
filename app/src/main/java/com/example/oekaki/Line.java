package com.example.oekaki;

import android.graphics.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Line {
    private int color;
    private ArrayList<Point> points;
    public Line(int color) {
        points = new ArrayList<Point>();
        this.color = color;
    }
    public int getColor(){
        return color;
    }
    public ArrayList<Point> getPoints() {
        return points;
    }
    public void addPoint(Point p) {
        points.add(p);
    }
}
