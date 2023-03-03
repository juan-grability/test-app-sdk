package com.testapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import com.testapp.ecisdk.LoginSuccessInterface;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView;

public class MainActivity extends ReactActivity {

  private static MainActivity mActivity;
  private static LoginSuccessInterface callback;

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "testApp";
  }

@Override
  protected ReactActivityDelegate createReactActivityDelegate() {
    return new ReactActivityDelegate(this, getMainComponentName()) {
      @Override
      protected ReactRootView createRootView() {
        return new RNGestureHandlerEnabledRootView(MainActivity.this);
      }
    };
  }

  public void setLoginSuccessCallback(LoginSuccessInterface loginSuccessInterface) {
    callback = loginSuccessInterface;
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.v("TESTS", "Dimension: " + getResources().getDimension(R.dimen.info_margin_top));
    mActivity = this;

    boolean isLarge = getResources().getBoolean(R.bool.isTablet);

    int currentOrientation = this.getRequestedOrientation();
    if (!isLarge) {
      this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    } else if (currentOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
      this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    // ATTENTION: This was auto-generated to handle app links.
    Intent appLinkIntent = getIntent();
    String appLinkAction = appLinkIntent.getAction();
    Uri appLinkData = appLinkIntent.getData();
  }

  public static MainActivity getmActivity() {
    return mActivity;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data != null && callback != null) {
      callback.onLoginResponse(requestCode, data);
    }
  }


  @Override
  protected void attachBaseContext(final Context baseContext) {
    Context newContext = baseContext;

    // Screen zoom is supported from API 24+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
      Resources resources = baseContext.getResources();
      DisplayMetrics displayMetrics = resources.getDisplayMetrics();
      Configuration configuration = resources.getConfiguration();

      if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
        configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
        newContext = baseContext.createConfigurationContext(configuration);
      }
    }
    super.attachBaseContext(newContext);
  }
}
