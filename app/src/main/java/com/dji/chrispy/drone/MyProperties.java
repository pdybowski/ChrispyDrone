package com.dji.chrispy.drone;
//package com.ramps;

public class MyProperties {
    private static MyProperties mInstance= null;
    public float altitudePoint1;
    public float altitudePoint2;

    protected MyProperties(){}

    public static synchronized MyProperties getInstance() {
        if(null == mInstance){
            mInstance = new MyProperties();
        }
        return mInstance;
    }
}

