package com.shriyansh.orientationsensor;

import java.text.DecimalFormat;

/**
 * Created by shriyansh on 19/5/15.
 */
public class Point {
    public double x;
    public double y;

    public Point(double x,double y){
        this.x=x;
        this.y=y;
    }

    public Point(Point point){
        this.x=point.x;
        this.y=point.y;
    }

    public void changeXY(double x,double y){
        this.x=x;
        this.y=y;
    }

    @Override
    public String toString() {
        DecimalFormat form = new DecimalFormat("#0.0");
        return "Point [x=" + form.format(x) + ", y=" + form.format(y) + "]";
    }

}
