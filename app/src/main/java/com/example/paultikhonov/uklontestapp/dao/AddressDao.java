package com.example.paultikhonov.uklontestapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.paultikhonov.uklontestapp.model.Address;

import java.util.List;

/**
 * Created by PaulTikhonov on 17.01.2018.
 */

@Dao
public interface AddressDao {

    @Query("SELECT * FROM address")
    List<Address> getAll();

    @Query("SELECT * FROM address where address_name LIKE  :addressName")
    Address findByAddressName(String addressName);

    @Query("SELECT COUNT(*) from address")
    int countAddresses();

    @Insert
    void insertAll(Address... addresses);

    @Delete
    void delete(Address address);
}
