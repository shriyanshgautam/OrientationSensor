package com.shriyansh.orientationsensor;

/**
 * Created by shriyansh on 19/5/15.
 */
public class Range {
    double minX;
    double minY;
    double maxX;
    double maxY;




    public Range() {
            minX=0;
            minY=0;
            maxX=0;;
            maxY=0;
    }

    public Range(double minX,double maxX,double minY,double maxY){
        this.minX=minX;
        this.maxX=maxX;
        this.minY=minY;
        this.maxY=maxY;

    }

    public void setRange(double minX,double maxX,double minY,double maxY){
        this.minX=minX;
        this.maxX=maxX;
        this.minY=minY;
        this.maxY=maxY;

    }

    public Range getRange(){
        return this;
    }

}
