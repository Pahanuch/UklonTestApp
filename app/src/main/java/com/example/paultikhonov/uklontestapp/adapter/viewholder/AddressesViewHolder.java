package com.example.paultikhonov.uklontestapp.adapter.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.paultikhonov.uklontestapp.R;
import com.example.paultikhonov.uklontestapp.model.Address;

/**
 * Created by PaulTikhonov on 13.01.2018.
 */

public class AddressesViewHolder extends RecyclerView.ViewHolder{

    TextView tvText;

    public AddressesViewHolder(View itemView) {
        super(itemView);
    }

    public void bindView(final Address addressesItem, @Nullable final OnAddressClickListener onPostClickListener) {

        tvText = (TextView) itemView.findViewById(R.id.text);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostClickListener != null) {
                    onPostClickListener.onAddressClick(addressesItem);
                }
            }
        });

        tvText.setText(addressesItem.getAddressName());
    }

    public interface OnAddressClickListener{
        void onAddressClick(Address homeItem);
    }

}
