package com.example.paultikhonov.uklontestapp.routers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.example.paultikhonov.uklontestapp.activity.AddressesListActivity;

/**
 * Created by PaulTikhonov on 16.01.2018.
 */

public class MainRouter {

    @NonNull
    private final FragmentActivity mActivity;

    public MainRouter(@NonNull FragmentActivity activity) {
        mActivity = activity;
        setup();
    }

    private void setup() {

    }

    public void showAddresses(int requestCode) {
        mActivity.startActivityForResult(new Intent(mActivity, AddressesListActivity.class), requestCode);
    }

}
