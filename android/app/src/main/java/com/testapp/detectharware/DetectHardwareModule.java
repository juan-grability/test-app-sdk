
package com.testapp.detectharware;

import java.lang.Math;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.provider.Settings;
import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Field;

public class DetectHardwareModule extends ReactContextBaseJavaModule {

    private ReactContext mReactContext;

    public DetectHardwareModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "DetectHardware";
    }

    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();

        final Context ctx = getReactApplicationContext();
        final DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();

        if (Build.VERSION.SDK_INT >= 17) {
            Display display = ((WindowManager) mReactContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            try {
                Display.class.getMethod("getRealMetrics", DisplayMetrics.class).invoke(display, metrics);
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        constants.put("REAL_WINDOW_WIDTH", getRealWidth(metrics));
        constants.put("REAL_WINDOW_HEIGHT", getRealHeight(metrics));
        constants.put("SOFT_MENU_BAR_HEIGHT", getSoftMenuBarHeight(metrics));
        constants.put("HAS_HARDWARE_MENU_BAR", getHardwareMenuBar(metrics));

        return constants;
    }

    private float getRealHeight(DisplayMetrics metrics) {
        return metrics.heightPixels / metrics.density;
    }

    private float getRealWidth(DisplayMetrics metrics) {
        return metrics.widthPixels / metrics.density;
    }

    private float getSoftMenuBarHeight(DisplayMetrics metrics) {
        final float realHeight = getRealHeight(metrics);
        final Context ctx = getReactApplicationContext();
        final DisplayMetrics usableMetrics = ctx.getResources().getDisplayMetrics();

        ((WindowManager) mReactContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(metrics);
        final int usableHeight = usableMetrics.heightPixels;

        return Math.max(0, realHeight - usableHeight / metrics.density);
    }

    private boolean getHardwareMenuBar(DisplayMetrics metrics) {
        boolean hasHardwareKeys = false;
        final Context ctx = getReactApplicationContext();
        boolean hasMenuKey = ViewConfiguration.get(ctx).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        hasHardwareKeys = hasMenuKey && hasBackKey;
        return hasHardwareKeys;
    }
}