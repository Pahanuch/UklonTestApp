package com.example.paultikhonov.uklontestapp.view;

import com.example.paultikhonov.uklontestapp.model.Route;

import java.util.List;

/**
 * Created by PaulTikhonov on 13.01.2018.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
