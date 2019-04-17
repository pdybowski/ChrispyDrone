package com.dji.chrispy.drone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dji.mapkit.core.maps.DJIMap;
import com.dji.mapkit.core.models.DJILatLng;

import java.util.ArrayList;

import dji.common.flightcontroller.LocationCoordinate3D;
import dji.keysdk.CameraKey;
import dji.keysdk.KeyManager;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.ux.widget.FPVOverlayWidget;
import dji.ux.widget.FPVWidget;
import dji.ux.widget.MapWidget;
import dji.ux.widget.controls.CameraControlsWidget;

/** Activity that shows all the UI elements together */
public class CompleteWidgetActivity extends Activity {

    /*private MapWidget mapWidget;*/
    private ViewGroup parentView;
    private FPVWidget fpvWidget;
    private FPVWidget secondaryFPVWidget;
    private FrameLayout secondaryVideoView;
    private LocationCoordinate3D altitude;

    private boolean isMapMini = true;
    private boolean measure = false;

    private Button menuBtn;
    private Button circleBtn;
    private Button squereBtn;
    private Button typeOfMeasureBtn;
    private Button altitudeBtn;
    private Button widthBtn;
    private Button distanceBtn;
    private Button measureBtn;
    private Button colorBtn;
    private LinearLayout options;
    private LinearLayout options2;
    private LinearLayout options3;
    private LinearLayout pointerCircle;
    private LinearLayout pointerSquere;
    private TextView cross1;
    private TextView cross2;

    private int height;
    private int width;
    private int margin;
    private int deviceWidth;
    private int deviceHeight;
    private int overlaycolorID;
    private float dAltitudePoint;
    private TextView resultOfMeausurementTextView;
    private String typeOfMeasure;

    private Location dronelocationStart = new Location("Start");
    private Location dronelocationEnd = new Location("Now");

    private ArrayList<Integer> overlaycolors = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_widgets);

        height = DensityUtil.dip2px(this, 100);
        width = DensityUtil.dip2px(this, 150);
        margin = DensityUtil.dip2px(this, 12);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

        /*mapWidget = (MapWidget) findViewById(R.id.map_widget);
        mapWidget.initAMap(new MapWidget.OnMapReadyListener() {
            @Override
            public void onMapReady(@NonNull DJIMap map) {
                map.setOnMapClickListener(new DJIMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(DJILatLng latLng) {
                        onViewClick(mapWidget);
                    }
                });
            }
        });
        mapWidget.onCreate(savedInstanceState);*/
                            /*ADDED*/
        menuBtn = findViewById(R.id.menu_button);
        circleBtn = findViewById(R.id.circle_button);
        squereBtn = findViewById(R.id.squere_button);
        options = findViewById(R.id.options);
        options2 = findViewById(R.id.options2);
        typeOfMeasureBtn = findViewById(R.id.type_measure_button);
        pointerCircle = findViewById(R.id.pointer1);
        pointerSquere = findViewById(R.id.pointer2);
        altitudeBtn = findViewById(R.id.altitude_button);
        resultOfMeausurementTextView = findViewById(R.id.resultOfMeausurementTextView);
        widthBtn = findViewById(R.id.width_button);
        distanceBtn = findViewById(R.id.distance_button);
        measureBtn = findViewById(R.id.measure_button);

        //<!-- Added by Daniel -->
        overlaycolorID=1;
        options3 =findViewById(R.id.options3);
        colorBtn=findViewById(R.id.color_button);
        cross1=findViewById(R.id.cross1);
        cross2=findViewById(R.id.cross2);
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_white));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_black));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_red));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_green));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_blue));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_white_half_transparent));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_black_half_transparent));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_red_half_transparent));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_green_half_transparent));
        overlaycolors.add((Integer)getResources().getColor(R.color.overlay_blue_half_transparent));
        //<!-- Added by Daniel -->



        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.getVisibility() == options.INVISIBLE && options2.getVisibility() == options2.INVISIBLE) {
                    options.setVisibility(options.VISIBLE);
                    options2.setVisibility(options2.VISIBLE);
                    options3.setVisibility(options3.VISIBLE);   //<!-- Added by Daniel -->
                }
                else {
                    options.setVisibility(options.INVISIBLE);
                    options2.setVisibility(options2.INVISIBLE);
                    options3.setVisibility(options3.INVISIBLE); //<!-- Added by Daniel -->
                }
            }
        });
        circleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pointerCircle.getVisibility() == pointerCircle.INVISIBLE) {
                    pointerCircle.setVisibility(pointerCircle.VISIBLE);
                    pointerSquere.setVisibility(pointerSquere.INVISIBLE);
                }
                else {
                    pointerCircle.setVisibility(pointerCircle.INVISIBLE);
                }
                options2.setVisibility(options2.INVISIBLE);
                options.setVisibility(options.INVISIBLE);
                options3.setVisibility(options3.INVISIBLE); //<!-- Added by Daniel -->
            }
        });
        squereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pointerSquere.getVisibility() == pointerSquere.INVISIBLE) {
                    pointerSquere.setVisibility(pointerSquere.VISIBLE);
                    pointerCircle.setVisibility(pointerCircle.INVISIBLE);
                }
                else {
                    pointerSquere.setVisibility(pointerSquere.INVISIBLE);
                }
                options.setVisibility(options.INVISIBLE);
                options2.setVisibility(options.INVISIBLE);
                options3.setVisibility(options3.INVISIBLE); //<!-- Added by Daniel -->
            }
        });


        //<!-- Added by Daniel -->
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GradientDrawable bckcolor1 = (GradientDrawable)pointerCircle.getBackground();
                GradientDrawable bckcolor2 = (GradientDrawable)pointerSquere.getBackground();
                bckcolor1.setStroke(10,(Integer) overlaycolors.get(overlaycolorID));
                bckcolor2.setStroke(10,(Integer) overlaycolors.get(overlaycolorID));
                cross1.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                cross2.setTextColor((Integer) overlaycolors.get(overlaycolorID));

                options.setVisibility(options.INVISIBLE);
                options2.setVisibility(options.INVISIBLE);
                options3.setVisibility(options3.INVISIBLE);

                colorBtn.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                overlaycolorID+=1;
                overlaycolorID%=10;
                colorBtn.setBackgroundColor((Integer) overlaycolors.get(overlaycolorID));
            }
        });
        //<!-- Added by Daniel -->


        /* Added by Krystian */
        typeOfMeasureBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if( altitudeBtn.getVisibility() == altitudeBtn.INVISIBLE && widthBtn.getVisibility() == widthBtn.INVISIBLE && distanceBtn.getVisibility() == distanceBtn.INVISIBLE)
                {
                    altitudeBtn.setVisibility(View.VISIBLE);
                    widthBtn.setVisibility(View.VISIBLE);
                    distanceBtn.setVisibility(View.VISIBLE);
                }
                else {
                    altitudeBtn.setVisibility(View.INVISIBLE);
                    widthBtn.setVisibility(View.INVISIBLE);
                    distanceBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        /* Added by Krystian */
        altitudeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (measure == false)
                {
                    if (typeOfMeasure == "atlitude") {
                        typeOfMeasure = "";
                        altitudeBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                    } else {
                        typeOfMeasure = "atlitude";
                        altitudeBtn.setBackgroundColor(Color.RED);
                        widthBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                        distanceBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                    }
                }
            }
        });

        /* Added by Krystian */
        widthBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (measure == false)
                {
                    if (typeOfMeasure == "width") {
                        typeOfMeasure = "";
                        widthBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                    } else {
                        typeOfMeasure = "width";
                        altitudeBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                        widthBtn.setBackgroundColor(Color.RED);
                        distanceBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                    }
            }
        }
        });

        /* Added by Krystian */
        distanceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (measure == false) {
                    if (typeOfMeasure == "distance") {
                        typeOfMeasure = "";
                        distanceBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                    } else {
                        typeOfMeasure = "distance";
                        widthBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                        altitudeBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_gray, null));
                        distanceBtn.setBackgroundColor(Color.RED);
                    }
                }
            }
        });

        /* Added by Krystian */
        measureBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // start measurement
                if (measure == false)
                {
                    measure = true;
                    measureBtn.setText("Stop measure");
                    if (typeOfMeasure == "atlitude")
                    {
                        Localization(dronelocationStart);
                    }
                    else if (typeOfMeasure == "width")
                    {
                        Localization(dronelocationStart);
                    }
                    else if (typeOfMeasure == "distance")
                    {
                        Localization(dronelocationStart);
                    }
                }
                // end measurement
                else if (measure == true)
                {
                    measure = false;
                    measureBtn.setText("Start measure");
                    if (typeOfMeasure == "atlitude")
                    {
                        Localization(dronelocationEnd);
                        resultOfMeausurementTextView.setText("Height: " + String.valueOf( GetHeightDifference(dronelocationStart.getAltitude(), dronelocationEnd.getAltitude())));
                    }
                    else if (typeOfMeasure == "width")
                    {
                        Localization(dronelocationEnd);
                        resultOfMeausurementTextView.setText("Width: " + String.valueOf(GetDistance2D(dronelocationStart,dronelocationEnd)));
                    }
                    else if (typeOfMeasure == "distance")
                    {
                        Localization(dronelocationEnd);
                        resultOfMeausurementTextView.setText("Distance: " + String.valueOf(
                                GetTotalDistance3D(GetHeightDifference(dronelocationStart.getAltitude(),
                                dronelocationEnd.getAltitude()),GetDistance2D(dronelocationStart,dronelocationEnd))));

                    }
                }
            }

        });

        /*ADDED BY K*/
/*        altitudeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isMeasHeight) {
                    isMeasHeight = false;
                    altitudeBtn.setText("WORK");
                    MyProperties.getInstance().altitudePoint1 = 20;     //= altitude.getAltitude();
                }
                else{
                    isMeasHeight = true;
                    altitudeBtn.setText("MEAS");
                    MyProperties.getInstance().altitudePoint2 = 46;     //= altitude.getAltitude();
                    dAltitudePoint = (MyProperties.getInstance().altitudePoint2 - MyProperties.getInstance().altitudePoint1);
                    altitudeValue.setText("Hdistance: " + dAltitudePoint);
                }
            }
        });
*/
        parentView = (ViewGroup) findViewById(R.id.root_view);

        /*fpvWidget = findViewById(R.id.fpv_widget);
        fpvWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClick(fpvWidget);
            }
        });*/
   /*     secondaryVideoView = (FrameLayout) findViewById(R.id.secondary_video_view);
        secondaryFPVWidget = findViewById(R.id.secondary_fpv_widget);
       secondaryFPVWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapVideoSource();
            }
        });
        updateSecondaryVideoVisibility();*/
    }

    /*private void onViewClick(View view) {
        if (view == fpvWidget && !isMapMini) {
            resizeFPVWidget(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 0, 0);
            reorderCameraCapturePanel();
            ResizeAnimation mapViewAnimation = new ResizeAnimation(mapWidget, deviceWidth, deviceHeight, width, height, margin);
            mapWidget.startAnimation(mapViewAnimation);
            isMapMini = true;
        } else if (view == mapWidget && isMapMini) {
            hidePanels();
            resizeFPVWidget(width, height, margin, 12);
            reorderCameraCapturePanel();
            ResizeAnimation mapViewAnimation = new ResizeAnimation(mapWidget, width, height, deviceWidth, deviceHeight, 0);
            mapWidget.startAnimation(mapViewAnimation);
            isMapMini = false;
        }
    }*/

    /* Added by Krystian */
    private void Localization(Location location)
    {
        location.setLatitude(DataOsdGetPushCommon.getInstance().getLatitude());
        location.setLongitude(DataOsdGetPushCommon.getInstance().getLongitude());
        location.setAltitude(DataOsdGetPushCommon.getInstance().getHeight()/10.0);
    }

    /* Added by Krystian */
    private double GetHeightDifference(double height1, double height2)
    {
        return Math.abs(height1 - height2);
    }

    /* Added by Krystian */
    private double GetDistance2D(Location loc1, Location loc2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(loc2.getLatitude()-loc1.getLatitude());  // deg2rad below
        double dLon = deg2rad(loc2.getLongitude()-loc1.getLongitude());
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(loc1.getLatitude())) * Math.cos(deg2rad(loc2.getLatitude())) *
                                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c * 1000; // Distance in m
        return d;
    }

    /* Added by Krystian */
    // zamiana stopni na radiany
    private double  deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    /* Added by Krystian */
    private double GetTotalDistance3D(double height, double length)
    {
        return Math.sqrt(Math.pow(height, 2)+Math.pow(length, 2));
    }

    private void resizeFPVWidget(int width, int height, int margin, int fpvInsertPosition) {
        RelativeLayout.LayoutParams fpvParams = (RelativeLayout.LayoutParams) fpvWidget.getLayoutParams();
        fpvParams.height = height;
        fpvParams.width = width;
        fpvParams.rightMargin = margin;
        fpvParams.bottomMargin = margin;
        if (isMapMini) {
            fpvParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
            fpvParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            fpvParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        } else {
            fpvParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            fpvParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            fpvParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        }
        fpvWidget.setLayoutParams(fpvParams);

        parentView.removeView(fpvWidget);
        parentView.addView(fpvWidget, fpvInsertPosition);
    }

    private void reorderCameraCapturePanel() {
        View cameraCapturePanel = findViewById(R.id.CameraCapturePanel);
        parentView.removeView(cameraCapturePanel);
        parentView.addView(cameraCapturePanel, isMapMini ? 9 : 13);
    }

    private void swapVideoSource() {
        if (secondaryFPVWidget.getVideoSource() == FPVWidget.VideoSource.SECONDARY) {
            fpvWidget.setVideoSource(FPVWidget.VideoSource.SECONDARY);
            secondaryFPVWidget.setVideoSource(FPVWidget.VideoSource.PRIMARY);
        } else {
            fpvWidget.setVideoSource(FPVWidget.VideoSource.PRIMARY);
            secondaryFPVWidget.setVideoSource(FPVWidget.VideoSource.SECONDARY);
        }
    }

    private void updateSecondaryVideoVisibility() {
        if (secondaryFPVWidget.getVideoSource() == null) {
            secondaryVideoView.setVisibility(View.GONE);
        } else {
            secondaryVideoView.setVisibility(View.VISIBLE);
        }
    }

    private void hidePanels() {
        KeyManager.getInstance().setValue(CameraKey.create(CameraKey.HISTOGRAM_ENABLED), false, null);
        KeyManager.getInstance().setValue(CameraKey.create(CameraKey.COLOR_WAVEFORM_ENABLED), false, null);

        findViewById(R.id.pre_flight_check_list).setVisibility(View.GONE);
        findViewById(R.id.rtk_panel).setVisibility(View.GONE);
        findViewById(R.id.spotlight_panel).setVisibility(View.GONE);
        findViewById(R.id.speaker_panel).setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Hide both the navigation bar and the status bar.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //mapWidget.onResume();
    }

    /*@Override
    protected void onPause() {
        mapWidget.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapWidget.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapWidget.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapWidget.onLowMemory();
    }*/

    private class ResizeAnimation extends Animation {

        private View mView;
        private int mToHeight;
        private int mFromHeight;

        private int mToWidth;
        private int mFromWidth;
        private int mMargin;

        private ResizeAnimation(View v, int fromWidth, int fromHeight, int toWidth, int toHeight, int margin) {
            mToHeight = toHeight;
            mToWidth = toWidth;
            mFromHeight = fromHeight;
            mFromWidth = fromWidth;
            mView = v;
            mMargin = margin;
            setDuration(300);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float height = (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
            float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) mView.getLayoutParams();
            p.height = (int) height;
            p.width = (int) width;
            p.rightMargin = mMargin;
            p.bottomMargin = mMargin;
            mView.requestLayout();
        }
    }
}
