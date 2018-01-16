package com.example.paultikhonov.uklontestapp.model;

import com.example.paultikhonov.uklontestapp.model.Distance;
import com.example.paultikhonov.uklontestapp.model.Duration;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by PaulTikhonov on 13.01.2018.
 */

public class Route {

    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}