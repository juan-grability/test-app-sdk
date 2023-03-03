package com.testapp.GECISDK;

import android.widget.Toast;

import com.elcorteingles.ecisdk.ECISDK;
import com.elcorteingles.ecisdk.access.EciAccessParams;
import com.elcorteingles.ecisdk.access.managers.ILogoutCallback;
import com.elcorteingles.ecisdk.access.presenter.EciAccess;
import com.elcorteingles.ecisdk.core.models.LocaleECI;
import com.elcorteingles.ecisdk.orders.interactor.EciOrdersInteractor;
import com.elcorteingles.ecisdk.orders.presenter.EciOrders;
import com.elcorteingles.ecisdk.profile.interactor.EciProfileInteractor;
import com.elcorteingles.ecisdk.profile.presenter.EciProfile;
import com.testapp.BuildConfig;
import com.testapp.GECISDK.Access.Access;
import com.testapp.GECISDK.Profile.Profile;
import com.testapp.GECISDK.address.Address;
import com.testapp.MainApplication;
import com.elcorteingles.ecisdk.core.models.SourceECI;
import com.testapp.R;
import com.facebook.react.bridge.ReactContext;

public class GECISDK {

    private static Address address;
    private static Access access;
    private static Profile profile;
    private static final String DEBUG_BUILD_TYPE = "debug";
    private static final String NFT_BUILD_TYPE = "nft";
    private static final String UAT_BUILD_TYPE = "uat";


    public static void initSdk(ReactContext mReactContext, MainApplication mApplication) {
        String packageName = mApplication.getPackageName();

        if(packageName.contains("portugal")){
            ECISDK.locale = LocaleECI.pt;
        }else{
            ECISDK.locale = LocaleECI.es;
        }

        if(packageName.contains("hipercor")){
            ECISDK.setSourceValue(SourceECI.APP_HIPERCOR);
        }else{
            ECISDK.setSourceValue(SourceECI.APP_SUPERMERCADO);
        }

        EciAccess eciAccess = new EciAccess(mApplication,
                new EciAccessParams("com.testapp.app.dev",
                        getEciSdkClientId(mReactContext),
                        getEciSdkClientSecret(mReactContext),
                        null,
                        null,
                        new ILogoutCallback() {
                            @Override
                            public void onLogout() {
                                Toast.makeText(mReactContext.getBaseContext(), "Logout complete!", Toast.LENGTH_SHORT).show();
                            }
                        }));

        ECISDK.setAccess(eciAccess);
        ECISDK.setProfile(new EciProfile(new EciProfileInteractor(eciAccess, mReactContext.getApplicationContext())));
        ECISDK.setOrders(new EciOrders(new EciOrdersInteractor(eciAccess, mReactContext.getApplicationContext())));
    }
    private static String getEciSdkClientId(ReactContext mReactContext) {
        switch (BuildConfig.BUILD_TYPE) {
            case NFT_BUILD_TYPE:
                return  mReactContext.getString(R.string.sdk_client_id_nft);
            case UAT_BUILD_TYPE:
                return  mReactContext.getString(R.string.sdk_client_id_uat);
            default:
                return  mReactContext.getString(R.string.sdk_client_id_pro);
        }
    }

    private static String getEciSdkClientSecret(ReactContext mReactContext) {
        switch (BuildConfig.BUILD_TYPE) {
            case NFT_BUILD_TYPE:
                return mReactContext.getString(R.string.sdk_client_secret_nft);
            case UAT_BUILD_TYPE:
                return  mReactContext.getString(R.string.sdk_client_secret_uat);
            default:
                return  mReactContext.getString(R.string.sdk_client_secret_pro);
        }
    }

    public static Address getAddress() {return (address == null)? new Address() : address;}

    public static Access getAccess() {return (access == null)? new Access() : access;}

    public static Profile getProfile(){return (profile == null)? new Profile():profile;}
}
