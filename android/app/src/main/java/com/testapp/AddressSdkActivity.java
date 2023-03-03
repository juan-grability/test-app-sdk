package com.testapp;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;

import com.elcorteingles.ecisdk.profile.layout.address.addaddress.EciAddNewAddressFragment;
import com.elcorteingles.ecisdk.profile.models.domain.AddressResponse;
import com.elcorteingles.ecisdk.profile.tools.Tools;
import com.testapp.GECISDK.PromiseEnum;
import com.testapp.GECISDK.address.Address;

import java.io.Serializable;

public class AddressSdkActivity extends AppCompatActivity implements Serializable {
    private Button onBackButton;
    private FragmentTransaction fragmentTransaction;
    private PromiseEnum refreshAddresEnumVal = PromiseEnum.resolve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdkprueba);
        init();
        checkExtras(getIntent().getExtras());
    }

    private void init() {
        setView();
        setListeners();
        getSupportFragmentManage();
    }

    private void setView() {
        onBackButton = findViewById(R.id.onback);
    }

    private void setListeners() {
        onBackButton.setOnClickListener(v -> {
            refreshAddresEnumVal = PromiseEnum.reject;
            onBackPressed();
        });
    }

    private void getSupportFragmentManage() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
    }

    private void checkExtras(Bundle bundle) {
        AddressResponse addressResponse = (AddressResponse) bundle.get(Address.ADDRESS_ID_KEY);
        Boolean isFirst = (Boolean) bundle.get(Tools.IS_FIRST_ADDRESS_KEY);
        
        if (addressResponse != null) {
            validateAddresResponse(bundle);
        } else {
            showAddNewAddressFragmentWithArguments(isFirst, false);
        }
    }

    private void validateAddresResponse(Bundle bundle) {
        try {
            AddressResponse addressResponse = (AddressResponse) bundle.get(Address.ADDRESS_ID_KEY);
            Boolean changeNeeded = (Boolean) bundle.get(Tools.IS_CHANGED_NEEDED);

            showAddNewAddressFragmentWithArguments(addressResponse, changeNeeded);
        } catch (ClassCastException e) {
            refreshAddresEnumVal = PromiseEnum.reject;
            finish();
        }
    }

    private void showAddNewAddressFragmentWithArguments(Object argument, Boolean changeNeeded) {
        EciAddNewAddressFragment eciAddNewAddressFragment = new EciAddNewAddressFragment();
        if (argument instanceof Boolean) {
            eciAddNewAddressFragment = EciAddNewAddressFragment.newInstance((Boolean) argument);
        } else if (argument instanceof AddressResponse) {
            eciAddNewAddressFragment = EciAddNewAddressFragment.newInstance((AddressResponse) argument, changeNeeded);
        }
        fragmentTransaction.replace(R.id.fragmenprueba, eciAddNewAddressFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            refreshAddresEnumVal = PromiseEnum.reject;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executeRefreshAddress(refreshAddresEnumVal);
    }

    private void executeRefreshAddress(PromiseEnum promiseEnum) {
        ((MainApplication) getApplication()).getEciSdkPackage().getEciSdkModule().refreshAddreses(promiseEnum);
    }
}
