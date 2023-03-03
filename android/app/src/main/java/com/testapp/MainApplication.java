package com.testapp;

// import com.ibits.react_native_in_app_review.AppReviewPackage;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;
// import com.lugg.ReactNativeConfig.ReactNativeConfigPackage;
// import com.microsoft.codepush.react.CodePush;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDex;
import com.testapp.detectharware.*;
import com.testapp.ecisdk.*;
import com.testapp.EventEmitter.*;

public class MainApplication extends Application implements ReactApplication {

    private static MainApplication mApp;
    private EciSdkPackage eciSdkPackage;
    private RNEventEmitterPackage eventEmitterPackage;

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          packages.add(new DetectHardwarePackage());
          packages.add(eciSdkPackage = new EciSdkPackage(MainApplication.this));
          packages.add(eventEmitterPackage = new RNEventEmitterPackage());
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();

        mApp = this;

    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
    initializeBroadCastReceiver(this, eventEmitterPackage);
  }

      @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public EciSdkPackage getEciSdkPackage() {
        return eciSdkPackage;
    }

    public static MainApplication getInstance() {
        return mApp;
    }

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.testapp.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }

    /**
   * Initializes invalid token broadcast receiver. This information is then sent to RN.
   * initializeBroadCastReceiver(eventEmitterPackage);
   *
   * @param context
   * @param eventEmitterPackage
   */
  private static void initializeBroadCastReceiver(
          Context context, RNEventEmitterPackage eventEmitterPackage
  ) {
    String NO_TOKEN_INTENT_ACTION = "no token action";

    BroadcastReceiver receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NO_TOKEN_INTENT_ACTION)) {
          eventEmitterPackage.onInvalidToken();
        }
      }
    };

    IntentFilter noTokenIntentFilter = new IntentFilter(NO_TOKEN_INTENT_ACTION);
    LocalBroadcastManager.getInstance(context).registerReceiver(receiver, noTokenIntentFilter);
  }
}
