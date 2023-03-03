package com.testapp.GECISDK.Profile;

import android.app.Activity;
import android.util.Log;

import com.elcorteingles.ecisdk.ECISDK;
import com.elcorteingles.ecisdk.access.callbacks.IGetAccessTokenCallback;
import com.elcorteingles.ecisdk.access.errors.RefreshErrors;
import com.elcorteingles.ecisdk.profile.callbacks.IGetCustomerCallback;
import com.elcorteingles.ecisdk.profile.errors.GetCustomerErrors;
import com.elcorteingles.ecisdk.profile.models.CustomerProfile;
import com.testapp.GECISDK.Constan;
import com.testapp.MainActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile {

    private static final String PROFILE = "profile";
    private static final String ERRORS = "error";
    private static final String ERROR_PROFILE_FAIL = "profile request fail";
    private static final String ADDRESS = "addresses";
    private static final String CELL_PHONE = "cellPhone";
    private static final String LANDLINE_PHONE = "landlinePhone";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String SECOND_LAST_NAME = "secondLastName";

    public void getProfile(Promise promise) {

        ECISDK.profile.getCustomer(new IGetCustomerCallback() {
            @Override
            public void onSuccess(CustomerProfile customerProfile) {
                promise.resolve(modelToProfileJson(customerProfile));
            }

            @Override
            public void onFailure(GetCustomerErrors getCustomerErrors) {
                WritableMap errorMap = Arguments.createMap();
                errorMap.putString(ERRORS, getCustomerErrors.toString());
                promise.reject(ERROR_PROFILE_FAIL, errorMap);
            }
        });
    }

    protected WritableMap modelToProfileJson(CustomerProfile customerProfile) {
        WritableMap tokenMap = Arguments.createMap();
        try {
            tokenMap.putString(FIRST_NAME, customerProfile.getGivenName());
            tokenMap.putString(CELL_PHONE, customerProfile.getCellPhone());
            tokenMap.putString(LANDLINE_PHONE, customerProfile.getLandLinePhone());
            tokenMap.putString(LAST_NAME, customerProfile.getSurName1());
            tokenMap.putString(SECOND_LAST_NAME, customerProfile.getSurName2());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenMap;
    }

    protected String modelToJsonString(CustomerProfile customerProfile) {
        JSONObject profileJson = null;
        try {
            profileJson = new JSONObject(new Gson().toJson(customerProfile));
            profileJson.remove(ADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileJson.toString();
    }

    public void startMyProfileSdk(Activity activity) {
        ECISDK.profile.startMyProfileActivity(activity, 0);
    }
}
