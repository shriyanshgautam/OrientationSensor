package com.shriyansh.orientationsensor;

/**
 * Created by shriyansh on 19/5/15.
 */
public class Item {
    public static final int MAX_COORDINATES=100;
    Boundary boundary;
    Range itemRange;

    public Item(Boundary boundary){

       boundary=new Boundary(boundary);
       itemRange=new Range();
       this.boundary=boundary;
       this.itemRange=boundary.getRange();
    }

    public Range getItemRange(){
        return this.itemRange;
    }

    public void setBoundary(Boundary boundary){

         this.boundary=boundary;
         this.itemRange=boundary.getRange();
    }

    public Boundary getBoundary() {
        return this.boundary;
    }

    public boolean matchRange(Range userRange){
        boolean result=false;
        if(userRange.minX<=itemRange.minX && userRange.maxX>=itemRange.maxX){
            if(userRange.minY<=itemRange.minY && userRange.maxY>=itemRange.maxY){
                result=true;
            }
        }
        return result;
    }

    public boolean matchRegion(float angle,int userX,int userY){

        float slope=(float)Math.tan(angle);
        int i;
        boolean matches=false;
        for (i=0;i<6;i++){
            double error=boundary.point[i].x-slope*(float)(boundary.point[i].y-userX)+userY;
            if(Math.abs(error)<2.0f){
                //matches
                matches=true;
                break;
            }

        }
        return matches;
    }

    public boolean match(float angle,int userX,int userY){
        Range userRange=new Range(-MAX_COORDINATES,MAX_COORDINATES,-MAX_COORDINATES,MAX_COORDINATES);
        if(angle>0 && angle<90){
            //quadrant I
            userRange=new Range(userX,MAX_COORDINATES,userY,MAX_COORDINATES);
        }else if(angle>90 && angle<180){
            //quadrant II
            userRange=new Range(userX,MAX_COORDINATES,-MAX_COORDINATES,userY);
        }else if(angle>180 && angle<270){
            //quadrant III
            userRange=new Range(-MAX_COORDINATES,userX,-MAX_COORDINATES,userY);
        }else if(angle>270 && angle<360){
            //quadrant IV
            userRange=new Range(-MAX_COORDINATES,userX,userY,MAX_COORDINATES);
        }

        if(matchRange(userRange)){
            if(matchRegion(angle,userX,userY)){
                return true;
            }else{
                return false;
            }

        }else{
            return false;
        }
    }

}
