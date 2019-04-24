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
    private Button sphereRoofBtn;
    private Button floatRoofBtn;

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
    private LinearLayout linearPointer;
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
    private String[] wallInstructions = {
            "Go to the top of a wall to measure and press MEAS (cover the top with a line)",
            "Go over the roof to have its surface inside circle. Press MEAS if ready",
            "Press RESULT to show measurement result"};
    //flags for roof
    //roundTankRoofFlag is a flag for 1st type of a roof with a ROOF, if its FALSE it means that tank has NO-ROOF
    //roofFlag is to inform program if we press ROOF type to measue
    //when roofFlag takes false value it means that we chose to measure WALL
    private boolean sphereTankRoofFlag, roofFlag;

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
        sphereRoofBtn = findViewById(R.id.sphereRoof_button);
        floatRoofBtn = findViewById(R.id.floatRoof_button);
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
        linearPointer = findViewById(R.id.linearPointer);
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

        sphereRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("Sphere");
                typeOfMeasureBtn.setText("TYPE");
                measInstruction.setText("Press TYPE to choose if ROOF or WALL");
                sphereTankRoofFlag = true;
            }
        });

        floatRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("Float");
                typeOfMeasureBtn.setText("TYPE");
                measInstruction.setText("Press TYPE to choose if ROOF or WALL");
                sphereTankRoofFlag = false;
            }
        });

        typeOfMeasureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meas_option.getVisibility() == meas_option.INVISIBLE)
                    meas_option.setVisibility(meas_option.VISIBLE);
                else
                    meas_option.setVisibility(meas_option.INVISIBLE);
            }
        });

        roofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(false);
                measureBtn.setEnabled(true);
                meas_option.setVisibility(View.INVISIBLE);
                typeOfMeasureBtn.setText("Roof");
                color_option.setVisibility(View.VISIBLE);
                roofFlag = true;
                if(sphereTankRoofFlag){
                    linearPointer.setVisibility(View.VISIBLE);
                    measInstruction.setText(roofInstructions[countInstructionText]);
                }
                else{
                    pointerCircle.setVisibility(View.VISIBLE);
                    measInstruction.setText(roofInstructions[countInstructionText+2]);
                }

            }
        });

        wallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(false);
                measureBtn.setEnabled(true);
                meas_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.VISIBLE);
                typeOfMeasureBtn.setText("Wall");
                measInstruction.setText(wallInstructions[countInstructionText]);
                roofFlag = false;
            }
        });

        measureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBtn.setEnabled(false);      //if we did first measurement we couldnt change TANK
                typeOfMeasureBtn.setEnabled(false);     //if we did first measurement we couldnt change ROOF to WALL or WALL to ROOF
                if(measureBtn.getText() == "NEW"){      //restart to beginning config
                    countInstructionText = 0;
                    measInstruction.setText("Press TANK button to choose a tank");
                    menuBtn.setText("TANK");
                    typeOfMeasureBtn.setText("TYPE");
                    measureBtn.setText("MEAS");
                    resultOfMeausurementTextView.setText("");
                    menuBtn.setEnabled(true);
                    measureBtn.setEnabled(false);
                    return;
                }
                if(roofFlag == true) {       //if we have ROOF type
                    // IF TANK WITH ROOF
                    if(sphereTankRoofFlag == true){
                        countInstructionText++;
                        if (countInstructionText == 4) {    //if we press RESULT then we get result of our measurement
                            //if we chose tank1, algorythm for tank1 roof surface
                            resultOfMeausurementTextView.setText("Roof height: " + String.valueOf(GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())) + "\n" +
                                    "Roof surface: " + String.valueOf(SurfaceRoofAlgorythm(5, 3, 7)));
                            measInstruction.setText("Press NEW to start new measurement");
                            measureBtn.setText("NEW");
                            return;
                        }
                        measInstruction.setText(roofInstructions[countInstructionText]);
                        if (countInstructionText == 0) {   //1st height
                            Localization(droneLocationH0);
                        }else if (countInstructionText == 1){   //2nd height
                            Localization(droneLocationH1);
                        }else if (countInstructionText == 2){   //height over the roof
                            linearPointer.setVisibility(View.INVISIBLE);    //linear layout
                            pointerCircle.setVisibility(View.VISIBLE);      //circle layout
                            Localization(droneLocationOverRoof);
                        }else if (countInstructionText == 3){
                            measureBtn.setText("RESULT");
                            pointerCircle.setVisibility(View.INVISIBLE);
                            color_option.setVisibility(View.INVISIBLE);
                            return;
                        }
                    }else if(sphereTankRoofFlag == false){// FLAOT TANK WITH NO ROOF
                        if (countInstructionText == 0){   //height over the roof
                            pointerCircle.setVisibility(View.INVISIBLE);
                            color_option.setVisibility(View.INVISIBLE);
                            measInstruction.setText(roofInstructions[3]);
                            Localization(droneLocationOverRoof);
                            measureBtn.setText("RESULT");
                            countInstructionText++;
                        } else if (countInstructionText == 1) {  //if we finished all the measurements
                            resultOfMeausurementTextView.setText("Roof height: " + String.valueOf(GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())) + "\n" +
                                    "Roof surface: " + String.valueOf(SurfaceRoofAlgorythm(5, 0, 0)));
                            measInstruction.setText("Press NEW to start new measurement");
                            measureBtn.setText("NEW");
                            countInstructionText++;
                        }
                    }
                }else{
                    //for WALL
                    if(countInstructionText == wallInstructions.length-1){  //if we finished all the measurements and we want result
                        measureBtn.setText("RESULT");
                    }
                    measInstruction.setText(wallInstructions[countInstructionText]);
                    countInstructionText++;
                    if(countInstructionText == 0) {   //height
                        Localization(droneLocationH0);
                        pointerCircle.setVisibility(View.INVISIBLE);
                    }else if (countInstructionText == 1) {  //if we finished all the measurements
                        resultOfMeausurementTextView.setText("Roof height: " + String.valueOf(GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())) + "\n" +
                                "Roof surface: " + String.valueOf(SurfaceRoofAlgorythm(5, 1, 1)));
                        measInstruction.setText("Press NEW to start new measurement");
                        measureBtn.setText("NEW");
                    }
                }
            }
        });

        // CLICK TO CHANGE LAYOUT COLOR
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable bckcolor1 = (GradientDrawable)pointerCircle.getBackground();
                GradientDrawable bckcolor2 = (GradientDrawable)pointerSquere.getBackground();
                GradientDrawable bckcolor3 = (GradientDrawable)linearPointer.getBackground();
                bckcolor1.setStroke(10,(Integer) overlaycolors.get(overlaycolorID));
                bckcolor2.setStroke(10,(Integer) overlaycolors.get(overlaycolorID));
                bckcolor3.setStroke(10,(Integer) overlaycolors.get(overlaycolorID));
                cross1.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                cross2.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                //linearPointer.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                /*square_option.setVisibility(square_option.INVISIBLE);
                circle_option.setVisibility(square_option.INVISIBLE);
                color_option.setVisibility(color_option.INVISIBLE);*/

                colorBtn.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                overlaycolorID+=1;
                overlaycolorID%=10;
                colorBtn.setBackgroundColor((Integer) overlaycolors.get(overlaycolorID));
            }
        });

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

        parentView = (ViewGroup) findViewById(R.id.root_view);
    }
 /** powierzchnia dachu dome */
    private double SurfaceRoofAlgorythm(double r, double height1, double height2){
        double epsilon, roofHeight, P;  //values to calculate roof surface
        if(sphereTankRoofFlag){       //if roof has ROOF
            roofHeight = height2 - height1;
            epsilon = Math.sqrt(1-Math.pow(roofHeight,2)/Math.pow(r, 4));
            P = (2*Math.PI*Math.pow(r,2)+Math.PI*Math.pow(roofHeight,2)/epsilon*Math.log((1+epsilon)/(1-epsilon)))/2;
        }else{   //if roof has NO-roof
            P = r*r;
        }
        return P;
    }

    /** height to odleglosc miedzy wysokoscia dachu a polozeniem drona w ktorym zawiera caly dach */
    /** radius to promien dachu */

    /** metoda do liczenia powierzchni sciany tankow */
    private double SurfaceWallTank(double radius,double height){
        double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        return 2*Math.PI*radius*distanceFromRoof;
    }

    /** metoda do liczenia powierzhcni dachu stozka */
    private double SurfaceRoofTank(double radius,double height){
        double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        return Math.PI*radius*Math.sqrt(Math.pow(radius,2) + Math.pow(distanceFromRoof,2));
    }

   //sphere tank
    private double SurfaceSphereTank(double radius){
        return 4*Math.PI*Math.pow(radius,2);
    }

    /** promien kola*/
    double GetWidthForCircle(double height){
        double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        double radius;
        radius=((0.8*distanceFromRoof)/1.9)/2;
        return radius;
    }
    /** width w przypadku kwadratu jest jego bokiem */
    private double GetWidthForSquere(double height){
        double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        double width;
        width=((0.8*distanceFromRoof)/1.9)/2;
        return width;
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

    /*private class ResizeAnimation extends Animation {
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
    }*/
}