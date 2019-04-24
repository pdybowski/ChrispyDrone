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
    private Button domeRoofBtn, externRoofBtn, openRoofBtn, coneRoofBtn, sphereRoofBtn;

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
    private TextView cross1;
    private TextView cross2;

    private int countInstructionText;       //number to iterate through the instruction arrays
    private TextView measInstruction;       //instruction

    private int overlaycolorID;
    private TextView resultOfMeausurementTextView;

                                /*FOR ROOF*/
    private Location droneLocationH0 = new Location("Start");
    private Location droneLocationH1 = new Location("Mid");
    private Location droneLocationOverRoof = new Location("END");

    private ArrayList<Integer> overlaycolors = new ArrayList();

    // Arrays with the instructions
    private String[] roofInstructions = {
            "Go to the bottom of a roof and press MEAS (cover the bottom with a line)",
            "Go to the top of the roof and press MEAS (cover the top with a line)",
            "Go over the roof to have the surface of the tank inside circle. Press MEAS if ready",
            "Press RESULT to show measurement result"};
    private String[] wallInstructions = {
            "Go to the top of a wall to measure and press MEAS (cover the top with a line)",
            "Go over the roof to have its surface inside circle. Press MEAS if ready",
            "Press RESULT to show measurement result"};

    private boolean domeTankRoofFlag, roofFlag, sphereRoofFlag;

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
        domeRoofBtn = findViewById(R.id.domeRoof_button);
        externRoofBtn = findViewById(R.id.externalRoof_button);
        openRoofBtn = findViewById(R.id.openRoof_button);
        coneRoofBtn = findViewById(R.id.coneRoof_button);
        sphereRoofBtn = findViewById(R.id.sphereRoof_button);

        typeOfMeasureBtn = findViewById(R.id.type_measure_button);
        meas_option = findViewById(R.id.meas_option);
        roofBtn = findViewById(R.id.roof_button);
        wallBtn = findViewById(R.id.wall_button);

        measureBtn = findViewById(R.id.measure_button);

        pointerCircle = findViewById(R.id.pointer1);
        pointerSquere = findViewById(R.id.pointer2);
        linearPointer = findViewById(R.id.linearPointer);
        resultOfMeausurementTextView = findViewById(R.id.resultOfMeausurementTextView);

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

        coneRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("CONE");
                typeOfMeasureBtn.setText("TYPE");
                measInstruction.setText("Press TYPE to choose if a ROOF or a WALL");
                domeTankRoofFlag = true;
                sphereRoofFlag = false;
            }
        });

        openRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("OPEN");
                typeOfMeasureBtn.setText("TYPE");
                measInstruction.setText("Press TYPE to choose if a ROOF or a WALL");
                domeTankRoofFlag = false;
                sphereRoofFlag = false;
            }
        });

        domeRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("DOME");
                typeOfMeasureBtn.setText("TYPE");
                measInstruction.setText("Press TYPE to choose if a ROOF or a WALL");
                domeTankRoofFlag = true;
                sphereRoofFlag = false;
            }
        });

        externRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.INVISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                menuBtn.setText("EXTERN");
                typeOfMeasureBtn.setText("TYPE");
                measInstruction.setText("Press TYPE to choose if a ROOF or a WALL");
                domeTankRoofFlag = false;
                sphereRoofFlag = false;
            }
        });

        sphereRoofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMeasureBtn.setEnabled(false);
                measureBtn.setEnabled(true);
                tank_option.setVisibility(View.INVISIBLE);
                linearPointer.setVisibility(View.VISIBLE);
                pointerCircle.setVisibility(View.INVISIBLE);
                color_option.setVisibility(View.VISIBLE);
                menuBtn.setText("SPHERE");
                typeOfMeasureBtn.setText("TYPE");
                measInstruction.setText("Go to the bottom of a sphere tank. Press MEAS if ready");
                sphereRoofFlag = true;
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
                if(domeTankRoofFlag){
                    linearPointer.setVisibility(View.VISIBLE);
                    measInstruction.setText(roofInstructions[countInstructionText]);    //go to the bottom of a roof
                }
                else{
                    linearPointer.setVisibility(View.VISIBLE);
                    measInstruction.setText(roofInstructions[countInstructionText+2]);     //go to the top of a wall
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
                    sphereRoofFlag = false;
                    return;
                }
                /** ROOF */
                if(roofFlag == true) {
                    /** ROOF FOR DOME | CONE */
                    if(domeTankRoofFlag == true && sphereRoofFlag == false){
                        countInstructionText++;
                        if (countInstructionText == 4) {
                            resultOfMeausurementTextView.setText("Roof height: " + String.valueOf(GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())) + "\n" +
                                    "Roof surface: " + String.valueOf(RoofTankAlgorythm(GetRadiusForCircle(droneLocationH0.getAltitude(), droneLocationOverRoof.getAltitude()), droneLocationH0.getAltitude(), droneLocationH1.getAltitude())));
                            measInstruction.setText("Press NEW to start new measurement");
                            measureBtn.setText("NEW");
                            return;
                        }
                        measInstruction.setText(roofInstructions[countInstructionText]);
                        if (countInstructionText == 0) {   //HEIGHT AT THE BEGNNING OF A ROOF
                            Localization(droneLocationH0);
                        }else if (countInstructionText == 1){   //HEIGHT AT THE TOP OF A ROOF
                                Localization(droneLocationH1);
                        }else if (countInstructionText == 2){   //HEIGHT IVER THE ROOF
                            linearPointer.setVisibility(View.INVISIBLE);
                            pointerCircle.setVisibility(View.VISIBLE);
                            Localization(droneLocationOverRoof);
                        }else if (countInstructionText == 3){
                            measureBtn.setText("RESULT");
                            pointerCircle.setVisibility(View.INVISIBLE);
                            color_option.setVisibility(View.INVISIBLE);
                            return;
                        }
                    }
                    /** ROOF - TANKS WITH NO CONVEX ROOF: EXTERNAL | OPEN_TOP */
                    else if(domeTankRoofFlag == false){
                        if (countInstructionText == 0){
                            pointerCircle.setVisibility(View.VISIBLE);
                            linearPointer.setVisibility(View.INVISIBLE);
                            Localization(droneLocationH0);  //height of a wall
                            measInstruction.setText(roofInstructions[2]);   //go over the roof
                            countInstructionText++;
                        } else if (countInstructionText == 1) {
                            pointerCircle.setVisibility(View.INVISIBLE);
                            color_option.setVisibility(View.INVISIBLE);
                            Localization(droneLocationOverRoof);    //height over the roof
                            measureBtn.setText("RESULT");
                            measInstruction.setText(roofInstructions[3]);   //press result
                            countInstructionText++;
                        } else if (countInstructionText == 2) {  //if we finished all the measurements
                            resultOfMeausurementTextView.setText("Roof height: " + String.valueOf(GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())) + "\n" +
                                    "Roof surface: " + String.valueOf(RoofTankAlgorythm(GetRadiusForCircle(droneLocationH0.getAltitude(), droneLocationOverRoof.getAltitude()), 0, 0)));
                            measInstruction.setText("Press NEW to start new measurement");
                            measureBtn.setText("NEW");
                        }
                    }
                }
                /** WALL for DOME | CONE | EXTERNAL | OPEN_TOP | INTERNAL */
                else if(roofFlag == false && sphereRoofFlag == false){
                    countInstructionText++;
                    if(countInstructionText == 1){
                        Localization(droneLocationH1);  //top of a wall
                        linearPointer.setVisibility(View.INVISIBLE);    //linear layout
                        pointerCircle.setVisibility(View.VISIBLE);      //circle layout
                    }else if(countInstructionText == 2){  //if we finished all the measurements and we want result
                        Localization(droneLocationOverRoof);    //height over tank
                        pointerCircle.setVisibility(View.INVISIBLE);      //circle layout
                        measureBtn.setText("RESULT");
                    }else if(countInstructionText == 3){ //if we click RESULT
                        resultOfMeausurementTextView.setText("Wall height: " + String.valueOf(droneLocationH1.getAltitude()) + "\n" +
                                "Wall surface: " + String.valueOf(SurfaceWallTank(GetRadiusForCircle(droneLocationH1.getAltitude(), droneLocationOverRoof.getAltitude()), droneLocationH1.getAltitude())));
                        measInstruction.setText("Press NEW to start new measurement");
                        measureBtn.setText("NEW");
                        return;
                    }
                    measInstruction.setText(wallInstructions[countInstructionText]);
                }
                /** FOR THE SPHERE */
                else if(sphereRoofFlag){
                    countInstructionText++;
                    if(countInstructionText == 1){
                        Localization(droneLocationH0);  //height on the bottom of a sphere
                        measInstruction.setText("Go to measure the top of the the sphere. Press MEAS if ready.");
                    }else if(countInstructionText == 2){
                        Localization(droneLocationH1);  //height on the top of a sphere
                        linearPointer.setVisibility(View.INVISIBLE);    //linear layout
                        color_option.setVisibility(View.INVISIBLE);
                        measInstruction.setText("Press RESULT to see results");
                        measureBtn.setText("RESULT");
                    }else if(countInstructionText == 3){
                        resultOfMeausurementTextView.setText("Sphere height: " + String.valueOf(droneLocationH1.getAltitude()) + "\n" +
                                "Sphere surface: " + String.valueOf(SurfaceSphereTank(GetHeightDifference(droneLocationH0.getAltitude(), droneLocationH1.getAltitude())/2)));
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
                colorBtn.setTextColor((Integer) overlaycolors.get(overlaycolorID));
                overlaycolorID+=1;
                overlaycolorID%=10;
                colorBtn.setBackgroundColor((Integer) overlaycolors.get(overlaycolorID));
            }
        });
        parentView = (ViewGroup) findViewById(R.id.root_view);
    }

    /** ROOF for DOME | CONE | EXTERNAL | OPEN_TOP | */
    private double RoofTankAlgorythm(double r, double height1, double height2){
        double epsilon, roofHeight, P;  //values to calculate roof surface
        if(domeTankRoofFlag){       //DOME/CONE/ROOF_FLOATING_TANK
            roofHeight = height2 - height1;
            epsilon = Math.sqrt(1-Math.pow(roofHeight,2)/Math.pow(r, 4));
            P = (2*Math.PI*Math.pow(r,2)+Math.PI*Math.pow(roofHeight,2)/epsilon*Math.log((1+epsilon)/(1-epsilon)))/2;
        }else{   //EXTERNAL_FLOATING/OPEN_TOP_TANK
            P = r*r;
        }
        return P;
    }

    /** height to odleglosc miedzy wysokoscia dachu a polozeniem drona w ktorym zawiera caly dach */
    /** radius to promien dachu */

    /** metoda do liczenia powierzchni sciany tankow */
    private double SurfaceWallTank(double radius, double height){
        double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        return 2*Math.PI*radius*distanceFromRoof;
    }

    /** metoda do liczenia powierzhcni dachu stozka */
    private double SurfaceRoofTank(double radius, double height){
        double distanceFromRoof=GetHeightDifference(height, droneLocationH1.getAltitude());
        return Math.PI*radius*Math.sqrt(Math.pow(radius,2) + Math.pow(distanceFromRoof,2));
    }

    /** sphere tank */
    private double SurfaceSphereTank(double radius){
        return 4*Math.PI*Math.pow(radius,2);
    }

    /** promien kola*/
    double GetRadiusForCircle(double heightTopRoof, double heightOverRoof){
        double distanceFromRoof=GetHeightDifference(heightTopRoof, heightOverRoof);
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

    /*private double GetDistance2D(Location loc1, Location loc2) {
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
    }*/

    // zamiana stopni na radiany
    /*private double  deg2rad(double deg) {
        return deg * (Math.PI/180);
    }*/

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
