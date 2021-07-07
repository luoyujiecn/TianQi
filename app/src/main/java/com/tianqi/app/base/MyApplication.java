package com.tianqi.app.base;

import android.app.Application;


import java.io.IOException;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

public class MyApplication extends Application {
    public static boolean ENCRITY = false;
    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        init();
    }
    
    public void init(){
        initAutoSize();
    }
    private void initAutoSize() {
        AutoSizeConfig.getInstance()
                .setBaseOnWidth(true)
                .getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
    }


    public static MyApplication getInstance() {
        return myApplication;
    }
}
