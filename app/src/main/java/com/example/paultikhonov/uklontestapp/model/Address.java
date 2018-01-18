package com.example.paultikhonov.uklontestapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Paul on 28.12.2017.
 */

@Entity(tableName = "address")
public class Address {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "address_name")
    private String mAddressName;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAddressName() {
        return mAddressName;
    }

    public void setAddressName(String addressName) {
        this.mAddressName = addressName;
    }

}
