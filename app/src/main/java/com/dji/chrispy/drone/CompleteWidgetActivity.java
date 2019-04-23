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
import com.mapbox.mapboxsdk.style.layers.Property;

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
    private Button tank1Btn;
    private Button tank2Btn;

    private Button typeOfMeasureBtn;
    private Button roofBtn;
    private Button wallBtn;

    private Button measureBtn;

    private Button circleBtn;
    private Button squereBtn;
    private Button altitudeBtn;
    private Button widthBtn;
    private Button distanceBtn;


    private Button colorBtn;
    private LinearLayout square_option;
    private LinearLayout circle_option;
    private LinearLayout tank_option;
    private LinearLayout meas_option;
    private LinearLayout color_option;
    private LinearLayout pointerCircle;
    private LinearLayout pointerSquere;
    private TextView cross1;
    private TextView cross2;

    private int countInstructionText;       //number to iterate through the instruction arrays
    private TextView measInstruction;       //instruction

    private int height;
    private int width;
    private int margin;
    private int deviceWidth;
    private int deviceHeight;
    private int overlaycolorID;
    private float dAltitudePoint;
    private TextView resultOfMeausurementTextView;
    private String typeOfMeasure;

                                /*FOR ROOF*/
    private Location droneLocationH0 = new Location("Start");
    private Location droneLocationH1 = new Location("Mid");
    private Location droneLocationOverRoof = new Location("END");

    private ArrayList<Integer> overlaycolors = new ArrayList();

    // Arrays with the instructions
    private String[] roofInstructions = {
            "Go to the bottom of a roof and press MEAS (cover the bottom with a line)",
            "Go to the top of a roof and press MEAS (cover the top with a line)",
            "Go over the roof to have its surface inside circle. Press MEAS if ready",
            "Press RESULT to show measurement result"};
    private String[] wallFromGroundInstuctions = {
            "Go to the top of a surface to measure and press MEAS (cover the top with a line)",
            "Go to the left side of a surface and press MEAS",
            "Go to the right side of a surface and press MEAS",
            "Press RESULT to show measurement result"};
    private String[] wallNotFromGroundInstructions = {
            "Go to the bottom of a surface to measure and press MEAS (cover the bottom with a line)",
            "Go to the top of a surface to measure and press MEAS (cover the top with a line)",
            "Go to the left side of a surface and press MEAS",
            "Go to the right side of a surface and press MEAS",
            "Press RESULT to show measurement result"};
    private String[] finishInstructions = {
            "Press NEW to start new measurement"};
    private boolean tank1Flag, tank2Flag, roofFlag;

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

        // INSTRUCTION
        measInstruction = findViewById(R.id.measInstruction);

        // WIDGETS
        menuBtn = findViewById(R.id.menu_button);
        tank_option = findViewById(R.id.tank_option);
        tank1Btn = findViewById(R.id.tank1_button);
        tank2Btn = findViewById(R.id.tank2_button);
        /*circleBtn = findViewById(R.id.circle_button);
        squereBtn = findViewById(R.id.squere_button);
        square_option = findViewById(R.id.square_option);
        circle_option = findViewById(R.id.circle_option);*/

        typeOfMeasureBtn = findViewById(R.id.type_measure_button);
        meas_option = findViewById(R.id.meas_option);
        roofBtn = findViewById(R.id.roof_button);
        wallBtn = findViewById(R.id.wall_button);

        measureBtn = findViewById(R.id.measure_button);

        pointerCircle = findViewById(R.id.pointer1);
        pointerSquere = findViewById(R.id.pointer2);
        //altitudeBtn = findViewById(R.id.altitude_button);
        resultOfMeausurementTextView = findViewById(R.id.resultOfMeausurementTextView);
        //widthBtn = findViewById(R.id.width_button);
        //distanceBtn = findViewById(R.id.distance_button);

        // COLORS
        overlaycolorID=1;
        color_option =findViewById(R.id.color_option);
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

        // TANK TYPE BUTTON
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tank_option.getVisibility() == tank_option.INVISIBLE) {
                    tank_option.setVisibility(tank_option.VISIBLE);
                }
                else {
                    tank_option.setVisibility(tank_option.INVISIBLE);
                }
            }
        });

        tank1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menuBtn.setEnabled(false);
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                menuBtn.setText("Tank1");
                measInstruction.setText("Press TYPE to choose if ROOF or WALL");
            }
        });

        tank2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menuBtn.setEnabled(false);
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                menuBtn.setText("Tank2");
                measInstruction.setText("Press TYPE to choose if ROOF or WALL");
            }
        });

        typeOfMeasureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meas_option.getVisibility() == meas_option.INVISIBLE) {
                    meas_option.setVisibility(meas_option.VISIBLE);
                }
                else {
                    meas_option.setVisibility(meas_option.INVISIBLE);
                }
            }
        });

        roofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measureBtn.setEnabled(true);
                meas_option.setVisibility(View.INVISIBLE);
                typeOfMeasureBtn.setText("Roof");
                measInstruction.setText(roofInstructions[countInstructionText]);
                roofFlag = true;
            }
        });

        wallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measureBtn.setEnabled(true);
                meas_option.setVisibility(View.INVISIBLE);
                typeOfMeasureBtn.setText("Wall");
                roofFlag = false;
            }
        });

        measureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBtn.setEnabled(false);      //if we did first measurement we couldnt change TANK
                typeOfMeasureBtn.setEnabled(false);     //if we did first measurement we couldnt change ROOF to WALL or WALL to ROOF
                countInstructionText++;
                if(measureBtn.getText() == "NEW"){      //measurement from the begining
                    countInstructionText = 0;
                    measInstruction.setText("Press TANK button to choose a tank");
                    menuBtn.setText("TANK");
                    typeOfMeasureBtn.setText("TYPE");
                    measureBtn.setText("MEAS");
                    resultOfMeausurementTextView.setText("");
                    menuBtn.setEnabled(true);
                    return;
                }
                if(countInstructionText == roofInstructions.length){    //if we press RESULT then we get result of our measurement
                    resultOfMeausurementTextView.setText("Roof height: " + String.valueOf( GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())) + "\n" +
                            "Roof surface: " + "POWIERZCHNIA");
                    measInstruction.setText("Press NEW to start new measurement");
                    measureBtn.setText("NEW");
                    return;
                }
                if(roofFlag == true){       //if we have roof type
                    if(countInstructionText == roofInstructions.length-1){  //if we finished all the measurements
                        measureBtn.setText("RESULT");
                        return;
                    }
                    measInstruction.setText(roofInstructions[countInstructionText]);
                    if(countInstructionText == 0)   //1st height
                        Localization(droneLocationH0);
                    if(countInstructionText == 1)   //2nd height
                        Localization(droneLocationH1);
                    if(countInstructionText == 2)
                        Localization(droneLocationOverRoof);
                }
                else{
                    //measInstruction.setText(wallInstructions[countInstructionText]);
                }
            }
        });

        // CLICK TO SHOW CIRCLE LAYOUT
        /*circleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pointerCircle.getVisibility() == pointerCircle.INVISIBLE) {
                    pointerCircle.setVisibility(pointerCircle.VISIBLE);
                    pointerSquere.setVisibility(pointerSquere.INVISIBLE);
                }
                else {
                    pointerCircle.setVisibility(pointerCircle.INVISIBLE);
                }
                circle_option.setVisibility(circle_option.INVISIBLE);
                square_option.setVisibility(square_option.INVISIBLE);
                color_option.setVisibility(color_option.INVISIBLE);
            }
        });*/

        // CLICK TO SHOW SQUARE LAYOUT
        /*squereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pointerSquere.getVisibility() == pointerSquere.INVISIBLE) {
                    pointerSquere.setVisibility(pointerSquere.VISIBLE);
                    pointerCircle.setVisibility(pointerCircle.INVISIBLE);
                }
                else {
                    pointerSquere.setVisibility(pointerSquere.INVISIBLE);
                }
                square_option.setVisibility(square_option.INVISIBLE);
                circle_option.setVisibility(square_option.INVISIBLE);
                color_option.setVisibility(color_option.INVISIBLE);
            }
        });*/

        // CLICK TO CHANGE LAYOUT COLOR
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable bckcolor1 = (GradientDrawable)pointerCircle.getBackground();
                GradientDrawable bckcolor2 = (GradientDrawable)pointerSquere.getBackground();
                bckcolor1.setStroke(10,(Integer) overlaycolors.get(overlaycolorID));
                bckcolor2.setStroke(10,(Integer) overlaycolors.get(overlaycolorID));
                cross1.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                cross2.setTextColor((Integer) overlaycolors.get(overlaycolorID));

                square_option.setVisibility(square_option.INVISIBLE);
                circle_option.setVisibility(square_option.INVISIBLE);
                color_option.setVisibility(color_option.INVISIBLE);

                colorBtn.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                overlaycolorID+=1;
                overlaycolorID%=10;
                colorBtn.setBackgroundColor((Integer) overlaycolors.get(overlaycolorID));
            }
        });

        // ROOF OR WALL TYPE
        /*typeOfMeasureBtn.setOnClickListener(new View.OnClickListener(){
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
        });*/

        // BUTTON T TAKE HEIGHT
        /*altitudeBtn.setOnClickListener(new View.OnClickListener(){
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
        });*/

        // BUTTON TO TAKE WIDTH
        /*widthBtn.setOnClickListener(new View.OnClickListener() {
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
*/
        // BUTTON TO TAKE DISTANCE
        /*distanceBtn.setOnClickListener(new View.OnClickListener(){
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
        });*/

        // TAKE MEAS
        /*measureBtn.setOnClickListener(new View.OnClickListener(){
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

        });*/

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
    private double GetTotalDistance3D(double height, double length){
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
