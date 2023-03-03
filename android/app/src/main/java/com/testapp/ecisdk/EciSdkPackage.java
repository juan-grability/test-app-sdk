package com.testapp.ecisdk;

import com.testapp.MainApplication;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EciSdkPackage implements ReactPackage {
    private MainApplication mApplication;
    private EciSdkModule eciSdkModule;

    public EciSdkPackage(MainApplication application) {
        this.mApplication = application;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(eciSdkModule = new EciSdkModule(reactContext, mApplication));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
    public EciSdkModule getEciSdkModule(){
        return eciSdkModule;
    }

}