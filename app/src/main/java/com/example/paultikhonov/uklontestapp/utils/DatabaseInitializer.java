package com.example.paultikhonov.uklontestapp.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.paultikhonov.uklontestapp.AppDatabase;
import com.example.paultikhonov.uklontestapp.model.Address;

import java.util.List;

/**
 * Created by PaulTikhonov on 17.01.2018.
 */


public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static Address addAddress(final AppDatabase db, Address address) {
        db.addressDao().insertAll(address);
        return address;
    }

    private static void deleteAddress(final AppDatabase db, Address address){
        db.addressDao().delete(address);
    }

    private static void populateWithTestData(AppDatabase db) {
        /*Address address = new Address();
        address.setAddressName("Odessa main street");
        addAddress(db, address);*/

        List<Address> userList = db.addressDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + userList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}