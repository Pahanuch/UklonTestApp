package com.example.paultikhonov.uklontestapp.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.paultikhonov.uklontestapp.AppDatabase;
import com.example.paultikhonov.uklontestapp.R;
import com.example.paultikhonov.uklontestapp.adapter.AddressesAdapter;
import com.example.paultikhonov.uklontestapp.adapter.viewholder.AddressesViewHolder;
import com.example.paultikhonov.uklontestapp.model.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressesListActivity extends AppCompatActivity implements AddressesViewHolder.OnAddressClickListener {

    private static final int MAX_LIST_SIZE = 10;

    private RecyclerView mRecyclerView;

    private AddressesAdapter mAddressesAdapter = new AddressesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_addresses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAddressesAdapter.setOnAddressClickListener(this, this);
        mRecyclerView.setAdapter(mAddressesAdapter);

        int addressesListSize = AppDatabase.getAppDatabase(this).addressDao().getAll().size();
        if (addressesListSize > MAX_LIST_SIZE) {
            Address address = AppDatabase.getAppDatabase(this).addressDao().getAll().get(0);
            AppDatabase.getAppDatabase(this).addressDao().delete(address);
        }

        List<Address> addressesItemList = new ArrayList<>();
        addressesItemList.addAll(AppDatabase.getAppDatabase(this).addressDao().getAll());
        mAddressesAdapter.setAddressesList(addressesItemList);

    }

    @Override
    public void onAddressClick(Address address) {
        Intent intent = new Intent();
        intent.putExtra(MapsActivity.ADDRESSES_LIST_REQUEST_VALUE, address.getAddressName());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
