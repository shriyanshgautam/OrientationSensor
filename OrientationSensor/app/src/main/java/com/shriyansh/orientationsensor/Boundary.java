package com.shriyansh.orientationsensor;

import java.util.Arrays;

/**
 * Created by shriyansh on 19/5/15.
 */
public class Boundary {

    public static final double RadiusMusium=100;

    Point point[];

    public Boundary(Point point[]){
        this.point=new Point[6];

        for (int i=0;i<6;i++){
           this.point[i]=new Point(point[i]);
        }
    }

    public Boundary(Boundary boundary){
        this.point=new Point[6];
        for (int i=0;i<6;i++) {
            this.point[i] = boundary.point[i];
        }
    }

    public Range getRange(){

        double x[]=new double[6];
        double y[]=new double[6];
        for(int i=0;i<6;i++){
            x[i]=this.point[i].x;
            y[i]=this.point[i].y;
        }
        Arrays.sort(x);
        Arrays.sort(y);
        Range range=new Range(x[0],x[5],y[0],y[5]);
        return range;
    }
}
