package com.testapp.EventEmitter;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.Arguments;

public class RNEventEmitter extends ReactContextBaseJavaModule {
  public RNEventEmitter(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "RNEventEmitter";
  }

  private void sendEvent(
    ReactContext reactContext,
    String eventName,
    WritableMap params
  ) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }

  @ReactMethod
  public void onInvalidToken() {
    String EVENT_KEY = "onInvalidToken";
    WritableMap eventMap = Arguments.createMap();
    eventMap.putString("type", EVENT_KEY);
    sendEvent(getReactApplicationContext(), EVENT_KEY, eventMap);
  }
}