package com.example.paultikhonov.uklontestapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.paultikhonov.uklontestapp.R;
import com.example.paultikhonov.uklontestapp.adapter.viewholder.AddressesViewHolder;
import com.example.paultikhonov.uklontestapp.model.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PaulTikhonov on 13.01.2018.
 */

public class AddressesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    List<Address> mAddressList  = new ArrayList<>();

    @Nullable
    private AddressesViewHolder.OnAddressClickListener mOnAddressClickListener;

    public void setOnAddressClickListener(Context context, @Nullable AddressesViewHolder.OnAddressClickListener onAddressClickListener) {
        mContext = context;
        mOnAddressClickListener = onAddressClickListener;
    }

    public void setAddressesList(@NonNull List<Address> addressesList) {
        mAddressList.clear();
        mAddressList.addAll(addressesList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AddressesViewHolder(inflater.inflate(R.layout.li_home_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ((AddressesViewHolder) holder).bindView(mAddressList.get(position), mOnAddressClickListener);
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }
}
