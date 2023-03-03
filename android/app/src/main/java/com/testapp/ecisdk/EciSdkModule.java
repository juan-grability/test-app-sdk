package com.testapp.ecisdk;


import com.testapp.GECISDK.GECISDK;
import com.testapp.GECISDK.PromiseEnum;
import com.testapp.GECISDK.AlertFactory;
import com.testapp.MainActivity;
import com.testapp.MainApplication;
import com.testapp.R;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class EciSdkModule extends ReactContextBaseJavaModule {
    private static ReactContext mReactContext;
    private MainApplication mApplication;
    private Promise promise;
    private static final String ERROR = "error";

    public EciSdkModule(ReactApplicationContext reactContext, MainApplication application) {
        super(reactContext);
        mReactContext = reactContext;
        mApplication = application;

        GECISDK.initSdk(mReactContext, mApplication);
    }

    @Override
    public String getName() {
        return "EciSdk";
    }

    @ReactMethod
    public void startLoginView(String target, final Promise promise) {
        GECISDK.getAccess().startLoginAccess(target, promise);
    }

    @ReactMethod
    public void startLogoutFlow(final Promise promise) {
        GECISDK.getAccess().logout(promise);
    }

    @ReactMethod
    public void getAccessToken(final Promise promise) {
        GECISDK.getAccess().getAccess(promise);
    }

    @ReactMethod
    public void getAddresses(final Promise promise) {
        GECISDK.getAddress().getAddresses(promise);
    }

    @ReactMethod
    public void createAddress(String Address, Boolean changedNeeded, Boolean isFirst, final Promise promise) {
        this.promise = promise;
        GECISDK.getAddress().createOrUpdateAddress(MainActivity.getmActivity(), Address, changedNeeded, isFirst, promise);
    }

    @ReactMethod
    public void deleteAddress(String addressId, Promise promise) {
        GECISDK.getAddress().deleteAddress(addressId, promise);
    }

    @ReactMethod
    public void startProfileFlow(final Promise promise) {
        GECISDK.getProfile().startMyProfileSdk(MainActivity.getmActivity());
    }

    @ReactMethod
    public void getProfile(Promise promise) {
        GECISDK.getProfile().getProfile(promise);
    }

    public void refreshAddreses(PromiseEnum promiseEnum) {
        if (PromiseEnum.resolve.equals(promiseEnum)) {
            AlertFactory.showToast(mReactContext, getmReactContext().getString(R.string.message_toast));
            promise.resolve("");
        } else {
            promise.reject(ERROR, ERROR);
        }
    }

    public static ReactContext getmReactContext() {
        return mReactContext;
    }
}
