package com.testapp.GECISDK;

import android.content.Context;
import android.widget.Toast;

public class AlertFactory {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
