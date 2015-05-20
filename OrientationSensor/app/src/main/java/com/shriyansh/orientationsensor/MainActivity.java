package com.shriyansh.orientationsensor;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TriggerEventListener mTriggerEventListener;
    TextView tv,tvcurrentRegion;
    Button reset,setCoordinates;
    EditText etAbcissa,etOrdinate;
    int initx=0;
    int inity=0;
    int initz=0;
    int flag=0;

    int userx=0;
    int usery=0;
    int currenRegion=0;

    boolean sector[]=new boolean[12];
    Item item[];

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 12;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private ViewPager mPagerLeft;
    private ViewPager mPagerRight;


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private PagerAdapter mPagerAdapterLeft;
    private PagerAdapter mPagerAdapterRight;
    Bundle thisbund;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=(TextView)findViewById(R.id.tv);
        tvcurrentRegion=(TextView)findViewById(R.id.curr_region);
        reset=(Button)findViewById(R.id.reset);
        setCoordinates=(Button)findViewById(R.id.set_coordinates);
        etAbcissa=(EditText)findViewById(R.id.abcissa);
        etOrdinate=(EditText)findViewById(R.id.ordinate);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerLeft=(ViewPager)findViewById(R.id.pagerLeft);
        mPagerRight=(ViewPager)findViewById(R.id.pagerRight);

        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerLeft.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerRight.setPageTransformer(true, new ZoomOutPageTransformer());

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPagerAdapterLeft = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPagerLeft.setAdapter(mPagerAdapterLeft);
        mPagerAdapterRight = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPagerRight.setAdapter(mPagerAdapterRight);


        initializeItems();

        for(boolean sec:sector){
            sec=false;
        }

        sector[0]=true;


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
            }
        });

        setCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    userx=Integer.parseInt(etAbcissa.getText().toString());
                    usery=Integer.parseInt(etOrdinate.getText().toString());

                }catch (Exception e){
                    userx=0;
                    usery=0;
                }

            }
        });




    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float lux = event.values[0];
        // Do something with this sensor value.
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.
        if(flag==0){
            //for the first time
            initx=-(int)event.values[0];
            inity=-(int)event.values[1];
            initz=-(int)event.values[2];

            flag=1;
        }



        final float alpha = 0.8f;


        int x=(int)event.values[0];
        int y=(int)event.values[1];
        int z=(int)event.values[2];

        tv.setText("X : "+x+" \nY : "+y+" \nZ : "+z);


         if(y<-40){
             analyseOrientation(x);
         }

    }

    public void regionChanged(int angle){
        for (int i=0;i<8;i++){
            if (item[i].match(angle,userx,usery)){
                toast("Item "+i+" visible");
            }
        }

    }

    public void initializeItems(){
        item=new Item[8];

        Point point[]=new Point[6];
        point[0]=new Point(4,9);
        point[1]=new Point(5,9);
        point[2]=new Point(6,9);
        point[3]=new Point(4,11);
        point[4]=new Point(5,11);
        point[5]=new Point(6,11);

        item[0]=new Item(new Boundary(point));

        point[0]=new Point(-4,9);
        point[1]=new Point(-5,9);
        point[2]=new Point(-6,9);
        point[3]=new Point(-4,11);
        point[4]=new Point(-5,11);
        point[5]=new Point(-6,11);

        item[1]=new Item(new Boundary(point));

        point[0]=new Point(-4,-9);
        point[1]=new Point(-5,-9);
        point[2]=new Point(-6,-9);
        point[3]=new Point(-4,-11);
        point[4]=new Point(-5,-11);
        point[5]=new Point(-6,-11);

        item[2]=new Item(new Boundary(point));


        point[0]=new Point(4,-9);
        point[1]=new Point(5,-9);
        point[2]=new Point(6,-9);
        point[3]=new Point(4,-11);
        point[4]=new Point(5,-11);
        point[5]=new Point(6,-11);

        item[3]=new Item(new Boundary(point));

        point[0]=new Point(9,4);
        point[1]=new Point(9,5);
        point[2]=new Point(9,6);
        point[3]=new Point(11,4);
        point[4]=new Point(11,5);
        point[5]=new Point(11,6);

        item[4]=new Item(new Boundary(point));


        point[0]=new Point(9,-4);
        point[1]=new Point(9,-5);
        point[2]=new Point(9,-6);
        point[3]=new Point(11,-4);
        point[4]=new Point(11,-5);
        point[5]=new Point(11,-6);

        item[5]=new Item(new Boundary(point));

        point[0]=new Point(-9,4);
        point[1]=new Point(-9,5);
        point[2]=new Point(-9,6);
        point[3]=new Point(-11,4);
        point[4]=new Point(-11,5);
        point[5]=new Point(-11,6);

        item[6]=new Item(new Boundary(point));

        point[0]=new Point(-9,-4);
        point[1]=new Point(-9,-5);
        point[2]=new Point(-9,-6);
        point[3]=new Point(-11,-4);
        point[4]=new Point(-11,-5);
        point[5]=new Point(-11,-6);

        item[7]=new Item(new Boundary(point));



    }




    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ScreenSlidePageFragment fragment=new ScreenSlidePageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public class ScreenSlidePageFragment extends Fragment {




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_retail, container, false);



            int position =getArguments().getInt("position",0);
            ImageView imageView=(ImageView)rootView.findViewById(R.id.retailImage);
            switch(position){
                case 0:imageView.setImageResource(R.drawable.retail_screen);
                    break;
                case 1:imageView.setImageResource(R.drawable.compare);
                    break;
                case 2:imageView.setImageResource(R.drawable.popup);
                    break;
                case 3:imageView.setImageResource(R.drawable.event_recommendation);
                    break;
                case 4:imageView.setImageResource(R.drawable.search_history);
                    break;
                case 5:imageView.setImageResource(R.drawable.retail_screen);
                    break;
                case 6:imageView.setImageResource(R.drawable.compare);
                    break;
                case 7:imageView.setImageResource(R.drawable.popup);
                    break;
                case 8:imageView.setImageResource(R.drawable.event_recommendation);
                    break;
                case 9:imageView.setImageResource(R.drawable.search_history);
                    break;
                case 10:imageView.setImageResource(R.drawable.retail_screen);
                    break;
                case 11:imageView.setImageResource(R.drawable.compare);
                    break;


            }



            return rootView;
        }
    }

    public void analyseOrientation(int x){
        if(x>0 && x<30){
            //reg I
            currenRegion=0;
        }else if(x>30 && x<60){
            //reg I
            currenRegion=1;
        }else if(x>60 && x<90){
            //reg I
            currenRegion=2;
        }else if(x>90 && x<120){
            //reg I
            currenRegion=3;
        }else if(x>120 && x<150){
            //reg I
            currenRegion=4;
        }else if(x>150 && x<180){
            //reg I
            currenRegion=5;
        }else if(x>180 && x<210){
            //reg I
            currenRegion=6;
        }else if(x>210 && x<240){
            //reg I
            currenRegion=7;
        }else if(x>240 && x<270){
            //reg I
            currenRegion=8;
        }else if(x>270 && x<300){
            //reg I
            currenRegion=9;
        }else if(x>300 && x<330){
            //reg I
            currenRegion=10;
        }else if(x>330 && x<360){
            //reg I
            currenRegion=11;
        }

        // sector[currenRegion]=true;

        for (int i=0;i<12;i++){
            if(sector[i]==true){
                if(i==currenRegion){
                    //the region is same
                }else{
                    //region is not he same
                    //update the prevision region
                    sector[i]=false;
                    sector[currenRegion]=true;
                    regionChanged(x);
                    mPager.setCurrentItem(currenRegion,true);
                    if(currenRegion!=0)
                    mPagerLeft.setCurrentItem(currenRegion-1,true);
                    if (currenRegion!=11)
                    mPagerRight.setCurrentItem(currenRegion+1,true);


                    tvcurrentRegion.setText(currenRegion+1+"");
                    //toast("Changed " + (i + 1) + " to " + (currenRegion + 1));
                    continue;
                }

            }
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    double sign (Point p1, Point p2, Point p3)
    {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    boolean pointInTriangle (Point pt, Point v1, Point v2, Point v3)
    {
        boolean b1, b2, b3;

        b1 = sign(pt, v1, v2) < 0.0f;
        b2 = sign(pt, v2, v3) < 0.0f;
        b3 = sign(pt, v3, v1) < 0.0f;

        return ((b1 == b2) && (b2 == b3));
    }


}
