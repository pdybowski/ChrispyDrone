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
    private Button distanceBtn, domeRoofBtn, externRoofBtn, openRoofBtn, coneRoofBtn, sphereRoofBtn, groundBtn, pointBtn;

    private Button typeOfMeasureBtn;
    private Button roofBtn;
    private Button wallBtn;

    private Button measureBtn;

    private Button colorBtn;
    private LinearLayout linearPointer;
    private LinearLayout tank_option;
    private LinearLayout meas_option;
    private LinearLayout color_option;
    private LinearLayout pointerCircle;
    private LinearLayout pointerSquere;
    private LinearLayout measPoint_option;
    private LinearLayout distancePointer;
    private TextView cross1;
    private TextView cross2;
    private TextView cross3;
    private LinearLayout distanceCross;

    private int countInstructionText;       //number to iterate through the instruction arrays
    private TextView measInstruction;       //instruction

    private int overlaycolorID;
    private TextView resultOfMeausurementTextView;

    private Location droneLocationH0 = new Location("Start");
    private Location droneLocationH1 = new Location("Mid");
    private Location droneLocationOverRoof = new Location("END");

    private ArrayList<Integer> overlaycolors = new ArrayList();

    private boolean distanceFlag, domeTankRoofFlag, roofFlag, sphereRoofFlag, groundFlag, pointFlag, openTankRoofFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_widgets);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        // INSTRUCTION
        measInstruction = findViewById(R.id.measInstruction);

        // WIDGETS
        menuBtn = findViewById(R.id.menu_button);
        tank_option = findViewById(R.id.tank_option);
        measPoint_option = findViewById(R.id.measPoint_option);
        distanceBtn = findViewById(R.id.distance_button);
        domeRoofBtn = findViewById(R.id.domeRoof_button);
        externRoofBtn = findViewById(R.id.externalRoof_button);
        openRoofBtn = findViewById(R.id.openRoof_button);
        coneRoofBtn = findViewById(R.id.coneRoof_button);
        sphereRoofBtn = findViewById(R.id.sphereRoof_button);

        typeOfMeasureBtn = findViewById(R.id.type_measure_button);
        meas_option = findViewById(R.id.meas_option);
        groundBtn = findViewById(R.id.ground_button);
        pointBtn = findViewById(R.id.point_button);
        roofBtn = findViewById(R.id.roof_button);
        wallBtn = findViewById(R.id.wall_button);

        measureBtn = findViewById(R.id.measure_button);

        pointerCircle = findViewById(R.id.pointer1);
        pointerSquere = findViewById(R.id.pointer2);
        linearPointer = findViewById(R.id.linearPointer);
        distanceCross = findViewById(R.id.cross_for_distance);
        resultOfMeausurementTextView = findViewById(R.id.resultOfMeausurementTextView);

        // COLORS
        overlaycolorID=1;
        color_option =findViewById(R.id.color_option);
        colorBtn=findViewById(R.id.color_button);
        cross1=findViewById(R.id.cross1);
        cross2=findViewById(R.id.cross2);
        cross3=findViewById(R.id.cross3);

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


        /** TANK TYPE BUTTON */
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

        distanceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                RestartMeasurement();
                tank_option.setVisibility(View.INVISIBLE);
                typeOfMeasureBtn.setVisibility(View.INVISIBLE);
                distanceCross.setVisibility((View.VISIBLE));
                measureBtn.setVisibility(View.VISIBLE);
                distanceCross.setVisibility(View.VISIBLE);
                color_option.setVisibility(View.VISIBLE);
                measureBtn.setEnabled(true);
                menuBtn.setText("Distance");
                measInstruction.setText("Go to the 1st point to measure. Press \"Get Point\" if ready.");
                domeTankRoofFlag = false;
                sphereRoofFlag = false;
                distanceFlag = true;
                openTankRoofFlag = false;
            }
        });

        domeRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestartMeasurement();
                typeOfMeasureBtn.setVisibility(View.VISIBLE);
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("Dome");
                measInstruction.setText("Press \"Type\" to choose \"Roof\" or \"Wall\".");
                domeTankRoofFlag = true;
                sphereRoofFlag = false;
                distanceFlag = false;
                openTankRoofFlag = false;
            }
        });

        openRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestartMeasurement();
                typeOfMeasureBtn.setVisibility(View.VISIBLE);
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("Open");
                measInstruction.setText("Press \"Type\" to choose \"Roof\" or \"Wall\".");
                domeTankRoofFlag = false;
                sphereRoofFlag = false;
                distanceFlag = false;
                openTankRoofFlag = true;
            }
        });

        coneRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestartMeasurement();
                typeOfMeasureBtn.setVisibility(View.VISIBLE);
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("Cone");
                measInstruction.setText("Press \"Type\" to choose \"Roof\" or a \"Wall\".");
                domeTankRoofFlag = true;
                sphereRoofFlag = false;
                distanceFlag = false;
                openTankRoofFlag = false;
            }
        });

        externRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestartMeasurement();
                typeOfMeasureBtn.setVisibility(View.VISIBLE);
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("Extern");
                measInstruction.setText("Press \"Type\" to choose \"Roof\" or \"Wall\".");
                openTankRoofFlag = true;
                domeTankRoofFlag = false;
                sphereRoofFlag = false;
                distanceFlag = false;
            }
        });

        sphereRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestartMeasurement();
                typeOfMeasureBtn.setVisibility(View.INVISIBLE);
                measureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.VISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                color_option.setVisibility(View.VISIBLE);
                measureBtn.setVisibility(View.VISIBLE);
                meas_option.setVisibility(View.INVISIBLE);
                menuBtn.setText("Sphere");
                measInstruction.setText("Go to the bottom of a sphere tank. Press \"Get Point\" if ready.");
                domeTankRoofFlag = false;
                sphereRoofFlag = true;
                distanceFlag = false;
                openTankRoofFlag = false;
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
                linearPointer.setVisibility(View.VISIBLE);
                typeOfMeasureBtn.setVisibility(View.INVISIBLE);
                measureBtn.setVisibility(View.VISIBLE);
                measureBtn.setEnabled(true);
                meas_option.setVisibility(View.INVISIBLE);
                typeOfMeasureBtn.setText("Roof");
                color_option.setVisibility(View.VISIBLE);
                roofFlag = true;
                if(domeTankRoofFlag){
                    //linearPointer.setVisibility(View.VISIBLE);
                    measInstruction.setText("Go to the bottom of a roof Press \"Get Point\" if ready.");    //go to the bottom of a roof
                }
                else{
                    //linearPointer.setVisibility(View.VISIBLE);
                    measInstruction.setText("Go to the top of a wall Press \"Get Point\" if ready.");     //go to the top of a wall
                }
            }
        });

        wallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setVisibility(View.INVISIBLE);
                measPoint_option.setVisibility(View.VISIBLE);
                meas_option.setVisibility(View.INVISIBLE);
                typeOfMeasureBtn.setText("Wall");
                measInstruction.setText("Chose if you want to measure from the ground or from the point.");
                roofFlag = false;
            }
        });

        groundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measPoint_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.VISIBLE);
                color_option.setVisibility(View.VISIBLE);
                measInstruction.setText("Go to the top of a wall to measure and press \"Get Point\" (cover the top with a line).");
                groundFlag = true;
                measureBtn.setVisibility(View.VISIBLE);
                measureBtn.setEnabled(true);
            }
        });

        pointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measPoint_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.VISIBLE);
                color_option.setVisibility(View.VISIBLE);
                measureBtn.setVisibility(View.VISIBLE);
                measInstruction.setText("Go to the 1st point where you want to start measure. Press \"Get Point\" if ready.");
                pointFlag = true;
                measureBtn.setEnabled(true);
            }
        });

        measureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBtn.setEnabled(false);      //if we did first measurement we couldnt change TANK
                typeOfMeasureBtn.setVisibility(View.INVISIBLE);
                //typeOfMeasureBtn.setEnabled(false);     //if we did first measurement we couldnt change ROOF to WALL or WALL to ROOF
                countInstructionText++;
                if(measureBtn.getText() == "New"){      //restart to beginning config
                    RestartMeasurement();
                }
                /** ROOF */
                else if(roofFlag && !distanceFlag) {
                    MeasureRoofTank();
                }
                /** WALL for DOME | CONE | EXTERNAL | OPEN_TOP | INTERNAL */
                else if(!roofFlag && !sphereRoofFlag && !distanceFlag){
                    if (groundFlag){                            // FROM GROUND
                        MeasureWallFromGround();
                    }else if(pointFlag){                                     // FROM POINT
                        MeasureWallFromPoint();
                    }
                }
                /** SPHERE */
                else if(sphereRoofFlag && !distanceFlag){
                    MeasureSphere();
                }
                /** DISTANCE */
                else if(distanceFlag){
                    DistanceBetweenTwoPoints();
                }
            }
        });

        /** CLICK TO CHANGE LAYOUT COLOR */
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
                cross3.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                colorBtn.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                overlaycolorID+=1;
                overlaycolorID%=10;
                colorBtn.setBackgroundColor((Integer) overlaycolors.get(overlaycolorID));
            }
        });
        parentView = (ViewGroup) findViewById(R.id.root_view);
    }

    /** DISTANCE */
    private void DistanceBetweenTwoPoints(){
        if(countInstructionText == 1){
            Localization(droneLocationH0);
            measInstruction.setText("Go to the 2nd point to measure. Press \"Get Point\" if ready.");
        }else if(countInstructionText == 2){
            Localization(droneLocationH1);
            distanceCross.setVisibility(View.INVISIBLE);
            color_option.setVisibility(View.INVISIBLE);
            measInstruction.setText("Press \"Result\" to show measurement result.");
            measureBtn.setText("Result");
        }else if(countInstructionText == 3){ //if we click RESULT
            resultOfMeausurementTextView.setText("Distance: " + String.format("0.2f", GetDistance2D(droneLocationH0, droneLocationH1)));
            measInstruction.setText("Press \"New\" to start new measurement.");
            measureBtn.setText("New");
        }
    }

    /** WALL from ground */
    private void MeasureWallFromGround(){
        if(countInstructionText == 1){
            Localization(droneLocationH0);  //top of a wall
            linearPointer.setVisibility(View.INVISIBLE);    //linear layout
            pointerCircle.setVisibility(View.VISIBLE);      //circle layout
            measInstruction.setText("Go over the roof to have the surface of the tank inside circle. Press \"Get Point\" if ready.");
        }else if(countInstructionText == 2){  //if we finished all the measurements and we want result
            Localization(droneLocationOverRoof);    //height over tank
            pointerCircle.setVisibility(View.INVISIBLE);      //circle layout
            color_option.setVisibility(View.INVISIBLE);
            measureBtn.setVisibility(View.VISIBLE);
            measInstruction.setText("Press \"Result\" to show measurement result.");
            measureBtn.setText("Result");
        }else if(countInstructionText == 3){ //if we click RESULT
            resultOfMeausurementTextView.setText("Wall height: " + String.format("%.3f", droneLocationH0.getAltitude()) + "\n" +
                    "Wall surface: " + String.format("%.3f", SurfaceWallTank(GetRadiusForCircle(droneLocationH0.getAltitude(), droneLocationOverRoof.getAltitude()), droneLocationH0.getAltitude())));
            measInstruction.setText("Press \"New\" to start new measurement.");
            measureBtn.setText("New");
        }
    }

    /** WALL from point */
    private void MeasureWallFromPoint(){
        if(countInstructionText == 1){
            Localization(droneLocationH0);  //bottom point
            measInstruction.setText("Go to the 2nd point where you want to measure. Press \"Get Point\" if ready.");
        }else if(countInstructionText == 2){
            Localization(droneLocationH1);    //top point
            linearPointer.setVisibility(View.INVISIBLE);
            pointerCircle.setVisibility(View.VISIBLE);      //circle layout
            measInstruction.setText("Go over the roof to have the surface of the tank inside circle. Press \"Get Point\" if ready.");
        }else if(countInstructionText == 3){  //if we finished all the measurements and we want result
            Localization(droneLocationOverRoof);    //height over tank
            color_option.setVisibility(View.INVISIBLE);
            pointerCircle.setVisibility(View.INVISIBLE);
            measInstruction.setText("Press \"Result\" to show measurement result.");
            measureBtn.setText("Result");
        }else if(countInstructionText == 4) {
            resultOfMeausurementTextView.setText("Wall height: " + String.format("%.3f", GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude()) + "\n" +
                    "Wall surface: " + String.format("%.3f", SurfaceWallTank(GetRadiusForCircle(droneLocationH0.getAltitude(), droneLocationOverRoof.getAltitude()), droneLocationH0.getAltitude()))));
            measInstruction.setText("Press \"New\" to start new measurement.");
            measureBtn.setText("New");
        }
    }

    /** ROOF for DOME | CONE | EXTERNAL | OPEN_TOP */
    private void MeasureRoofTank(){
        /** ROOF FOR DOME | CONE */
        if(domeTankRoofFlag == true && sphereRoofFlag == false){
            if (countInstructionText == 1) {   //HEIGHT AT THE BEGNNING OF A ROOF
                Localization(droneLocationH0);
                measInstruction.setText("Go to the top of a roof. Press \"Get Point\" if ready.");
            }else if (countInstructionText == 2){   //HEIGHT AT THE TOP OF A ROOF
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.VISIBLE);
                Localization(droneLocationH1);
                measInstruction.setText("Go over the roof to have the surface of the tank inside circle. Press \"Get Point\" if ready.");   //go over the roof
            }else if (countInstructionText == 3){   //HEIGHT IVER THE ROOF
                pointerCircle.setVisibility(View.INVISIBLE);
                color_option.setVisibility(View.INVISIBLE);
                Localization(droneLocationOverRoof);
                measInstruction.setText("Press \"Result\" to show measurement result.");   //press result
                measureBtn.setText("Result");
            }else if (countInstructionText == 4) {
                resultOfMeausurementTextView.setText("Roof height: " + String.format("%0.3f", GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())) + "\n" +
                        "Roof radius: " + GetRadiusForCircle(droneLocationH0.getAltitude(), droneLocationOverRoof.getAltitude()) + "\n" +
                        "Roof surface: " + String.format("%.3f", RoofTankAlgorithm(GetRadiusForCircle(droneLocationH0.getAltitude(), droneLocationOverRoof.getAltitude()), GetHeightDifference(droneLocationH0.getAltitude(),droneLocationH1.getAltitude()))));
                measInstruction.setText("Press \"New\" to start new measurement.");
                measureBtn.setText("New");
            }
        }
        /** ROOF - TANKS WITH NO CONVEX ROOF: EXTERNAL | OPEN_TOP */
        else if(!domeTankRoofFlag && openTankRoofFlag){
            if (countInstructionText == 1){
                pointerCircle.setVisibility(View.VISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                Localization(droneLocationH0);  //height of a wall
                measInstruction.setText("Go over the roof to have the surface of the tank inside circle. Press \"Get Point\" if ready.");   //go over the roof
            } else if (countInstructionText == 2) {
                pointerCircle.setVisibility(View.INVISIBLE);
                color_option.setVisibility(View.INVISIBLE);
                Localization(droneLocationOverRoof);    //height over the roof
                measInstruction.setText("Press \"Result\" to show measurement result.");   //press result
                measureBtn.setText("Result");
            } else if (countInstructionText == 3) {  //if we finished all the measurements
                resultOfMeausurementTextView.setText("Roof height: " + String.format("%.3f", GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())) + "\n" +
                        "Roof surface: " + String.format("%.3f", RoofTankAlgorithm(GetRadiusForCircle(droneLocationH0.getAltitude(), droneLocationOverRoof.getAltitude()), 0)));
                measInstruction.setText("Press \"New\" to start new measurement.");
                measureBtn.setText("New");
            }
        }
    }

    /** SPHERE */
    private void MeasureSphere(){
        if(countInstructionText == 1){
            pointerCircle.setVisibility(View.INVISIBLE);
            linearPointer.setVisibility(View.VISIBLE);    //linear layout
            Localization(droneLocationH0);  //height on the bottom of a sphere
            measInstruction.setText("Go to measure the top of the the sphere. Press \"Get point\" if ready.");
        }else if(countInstructionText == 2){
            Localization(droneLocationH1);  //height on the top of a sphere
            linearPointer.setVisibility(View.INVISIBLE);    //linear layout
            color_option.setVisibility(View.INVISIBLE);
            measInstruction.setText("Press \"Result\" to show measurement result");
            measureBtn.setText("Result");
        }else if(countInstructionText == 3){
            resultOfMeausurementTextView.setText("Sphere height: " + String.format("%.3f", GetHeightDifference(droneLocationH0.getAltitude(),droneLocationH1.getAltitude())) + "\n" +
                    "Sphere surface: " + String.format("%.3f", SurfaceSphereTank(GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())/2)));
            measInstruction.setText("Press \"New\" to start new measurement");
            measureBtn.setText("New");
        }
    }

    /** Restart measurement */
    private void RestartMeasurement(){
        measureBtn.setVisibility(View.INVISIBLE);
        meas_option.setVisibility(View.INVISIBLE);
        measPoint_option.setVisibility(View.INVISIBLE);
        color_option.setVisibility((View.INVISIBLE));
        typeOfMeasureBtn.setVisibility(View.INVISIBLE);
        distanceCross.setVisibility((View.INVISIBLE));
        linearPointer.setVisibility(View.INVISIBLE);
        countInstructionText = 0;
        measInstruction.setText("Press \"Tank\" button to choose a tank");
        menuBtn.setText("Tank");
        typeOfMeasureBtn.setText("Type");
        measureBtn.setText("Get point");
        resultOfMeausurementTextView.setText("");
        menuBtn.setEnabled(true);
        measureBtn.setEnabled(false);
        sphereRoofFlag = false;
        pointFlag = false;
        groundFlag = false;
        distanceFlag = false;
        openTankRoofFlag = false;
        roofFlag = false;
    }

    /** Algorithm to measure surface of a roof*/
    private double RoofTankAlgorithm(double r, double roofHeight){
        double epsilon, P;
        /** DOME/CONE/ROOF_FLOATING_TANK */
        if(domeTankRoofFlag){
            double factor = 1.6075;
            P = 4*Math.PI*Math.pow(((Math.pow(r,2*factor))+2*Math.pow(r,factor)*Math.pow(roofHeight,factor))/3,1/factor);
            //epsilon = Math.sqrt(1-(Math.pow(roofHeight,2)/Math.pow(r,2)));
            //P = (2*Math.PI*Math.pow(r,2)+Math.PI*Math.pow(roofHeight,2)/epsilon*Math.log((1+epsilon)/(1-epsilon)))/2;
        }
        /** EXTERNAL_FLOATING/OPEN_TOP_TANK */
        else{
            P = r*r;
        }
        return P;
    }

    /** metoda do liczenia powierzchni sciany tankow */
    private double SurfaceWallTank(double radius, double height){
        //double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        return 2*Math.PI*radius*height;
    }

    /** metoda do liczenia powierzhcni dachu stozka */
    private double SurfaceRoofTank(double radius, double height){
        double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        return Math.PI*radius*Math.sqrt(Math.pow(radius,2) + Math.pow(distanceFromRoof,2));
    }

    /** sphere tank - promien to odgleglosc miedzy wys pocz. i kon. */
    private double SurfaceSphereTank(double radius){
        return 4*Math.PI*Math.pow(radius,2);
    }

    /** Circle radius */
    double GetRadiusForCircle(double heightBottomRoof, double heightOverRoof){
        double distanceFromRoof=GetHeightDifference(heightBottomRoof, heightOverRoof);
        double radius;
        radius=((0.8*distanceFromRoof)/1.9)/2;
        return radius;
    }

    /** width for square */
    private double GetWidthForSquere(double height){
        double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        double width;
        width=((0.8*distanceFromRoof)/1.9)/2;
        return width;
    }

    private void Localization(Location location)
    {
        location.setLatitude(DataOsdGetPushCommon.getInstance().getLatitude());
        location.setLongitude(DataOsdGetPushCommon.getInstance().getLongitude());
        location.setAltitude(DataOsdGetPushCommon.getInstance().getHeight()/10.0);
    }

    private double GetHeightDifference(double height1, double height2)
    {
        return Math.abs(height1 - height2);
    }

    /** degrees to radians */
    private double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    /** distance between two points */
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

}
