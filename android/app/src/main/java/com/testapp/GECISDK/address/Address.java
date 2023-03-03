package com.testapp.GECISDK.address;

import android.app.Activity;
import android.content.Intent;

import com.elcorteingles.ecisdk.ECISDK;
import com.elcorteingles.ecisdk.profile.callbacks.IDeleteCustomerAddressResponseCallback;
import com.elcorteingles.ecisdk.profile.callbacks.IGetCustomerAddressesCallback;
import com.elcorteingles.ecisdk.profile.errors.DeleteCustomerAddressErrors;
import com.elcorteingles.ecisdk.profile.errors.GetCustomerAddressesErrors;
import com.elcorteingles.ecisdk.profile.models.domain.AddressResponse;
import com.elcorteingles.ecisdk.profile.requests.DeleteAddressRequest;
import com.elcorteingles.ecisdk.profile.tools.Tools;
import com.testapp.AddressSdkActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Address {
    public static final String ADDRESS_ID_KEY = "addressId";
    private static final String ERROR_ADDRESS_PROMISA = "address request fail";
    private static final String ERRORS = "error";
    private static final String IDENTIFIER = "identifier";
    private static final String ID_REMOVE = "id";
    private static final String ADDRESSES = "addresses";

    public void getAddresses(Promise promise) {
        ECISDK.profile.getAddresses(new IGetCustomerAddressesCallback() {
            @Override
            public void onSuccess(ArrayList<AddressResponse> arrayList) {
                WritableMap tokenMap = Arguments.createMap();
                tokenMap.putString(ADDRESSES, Arrays.toString(getAddressList(arrayList)));
                promise.resolve(tokenMap);
            }

            @Override
            public void onFailure(GetCustomerAddressesErrors getCustomerAddressesErrors) {
                WritableMap errorMap = Arguments.createMap();
                errorMap.putString(ERRORS, getCustomerAddressesErrors.toString());
                promise.reject(ERROR_ADDRESS_PROMISA, errorMap);
            }
        });
    }

    private String[] getAddressList(ArrayList<AddressResponse> arrayList) {
        String adresses[] = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            adresses[i] = getAddressJsonAsString(arrayList.get(i));
        }
        return adresses;
    }

    protected String getAddressJsonAsString(AddressResponse addressModel) {
        JSONObject addressJson = null;
        try {
            addressJson = new JSONObject(new Gson().toJson(addressModel));
            addressJson.remove(ID_REMOVE);
            addressJson.put(IDENTIFIER, addressModel.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addressJson.toString();
    }

    public void createOrUpdateAddress(Activity activity, String address, Boolean changeNeeded, Boolean isFirst, Promise promise) {
        if (address != null) {
            getAddress(activity, address, changeNeeded, promise);
        } else {
            showAddressActivity(activity, null, false, isFirst);
        }
    }

    private void showAddressActivity(Activity activity, AddressResponse addressResponse, Boolean changeNeeded, Boolean isFirst) {
        Intent intent = new Intent(activity, AddressSdkActivity.class);
        if (addressResponse != null){
            intent.putExtra(ADDRESS_ID_KEY, addressResponse);
            intent.putExtra(Tools.IS_CHANGED_NEEDED, changeNeeded);
        }else{
            intent.putExtra(Tools.IS_FIRST_ADDRESS_KEY, isFirst);
        }
        activity.startActivity(intent);
    }

    public void getAddress(Activity activity, String addresId, Boolean changeNeeded, Promise promise) {
        ECISDK.profile.getAddresses(new IGetCustomerAddressesCallback() {
            @Override
            public void onSuccess(ArrayList<AddressResponse> arrayList) {
                AddressResponse addressUpdate = getAddressResponseById(arrayList, addresId);
                if (addressUpdate != null) {
                    showAddressActivity(activity, addressUpdate, changeNeeded, false);
                } else {
                    promise.reject(ERROR_ADDRESS_PROMISA, Arguments.createMap());
                }
            }

            @Override
            public void onFailure(GetCustomerAddressesErrors getCustomerAddressesErrors) {
                WritableMap errorMap = Arguments.createMap();
                errorMap.putString(ERRORS, getCustomerAddressesErrors.toString());
                promise.reject(ERROR_ADDRESS_PROMISA, errorMap);
            }
        });
    }

    private AddressResponse getAddressResponseById(ArrayList<AddressResponse> arrayList, String addresId) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getId().equals(addresId)) {
                return arrayList.get(i);
            }
        }
        return null;
    }

    public void deleteAddress(String addressId, Promise promise) {
        DeleteAddressRequest deleteAddressRequest = new DeleteAddressRequest(addressId);
        ECISDK.profile.deleteAddress(deleteAddressRequest, new IDeleteCustomerAddressResponseCallback() {
            @Override
            public void onSuccess() {
                promise.resolve("");
            }

            @Override
            public void onFailure(DeleteCustomerAddressErrors deleteCustomerAddressErrors) {
                WritableMap errorMap = Arguments.createMap();
                errorMap.putString(ERRORS, deleteCustomerAddressErrors.toString());
                promise.reject(ERROR_ADDRESS_PROMISA, errorMap);
            }
        });
    }
}
