package com.testapp.GECISDK.Access;

import com.elcorteingles.ecisdk.ECISDK;
import com.elcorteingles.ecisdk.access.callbacks.IGetAccessTokenCallback;
import com.elcorteingles.ecisdk.access.errors.RefreshErrors;
import com.elcorteingles.ecisdk.access.responses.LoginResponse;
import com.testapp.GECISDK.Constan;
import com.testapp.MainActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;

import java.net.MalformedURLException;
import java.net.URL;

public class Access {

    public void getAccess(Promise promise){
        ECISDK.access.getAccessToken(new IGetAccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                WritableMap tokenMap = Arguments.createMap();
                tokenMap.putString("token", accessToken);
                promise.resolve(tokenMap);

            }

            @Override
            public void onError(RefreshErrors error) {
                WritableMap errorMap = Arguments.createMap();
                errorMap.putString("error", error.toString());
                promise.reject("token failure", errorMap);
            }
        });
    }

    public void startLoginAccess(String target, Promise promise){
        MainActivity.getmActivity().setLoginSuccessCallback((requestCode, data) -> {
            LoginResponse response = data.getParcelableExtra("Login_response");
            if (requestCode == Constan.REQUEST_CODE_TEN && response != null) {
                WritableMap params = Arguments.createMap();
                params.putString("token", response.getAccesToken());
                params.putString("name", response.getGivenName());
                params.putString("firstLastName", response.getFamilyName());
                params.putString("secondLastName", null);
                params.putString("userId", response.getSub());
                params.putString("email", response.getEmail());
                promise.resolve(params);
            }
        });
        ECISDK.access.startEciAccessActivityForResult(Constan.REQUEST_CODE_TEN, MainActivity.getmActivity(), returnBackURL(target));
    }

    public static URL returnBackURL(String target) {
        try {
            if (target.equals("hipercor")) {
                return new URL("https://www.hipercor.es/supermercado/");
            }
            if (target.equals("eci")) {
                return new URL("https://www.elcorteingles.es/supermercado/");
            }
            if (target.equals("portugal")) {
                return new URL("https://www.elcorteingles.pt/supermercado/");
            }
        }catch(MalformedURLException e){ }
        return null;
    }

    public void logout(Promise promise){
        ECISDK.access.logout();
        ECISDK.access.getAccessToken(new IGetAccessTokenCallback() {
            @Override
            public void onSuccess(String s) {
                promise.reject("error","token");
            }
            @Override
            public void onError(RefreshErrors refreshErrors) {
                promise.resolve("");
            }
        });
    }
}
