package com.example.paultikhonov.uklontestapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paultikhonov.uklontestapp.AppDatabase;
import com.example.paultikhonov.uklontestapp.routers.MainRouter;
import com.example.paultikhonov.uklontestapp.utils.PermissionUtils;
import com.example.paultikhonov.uklontestapp.R;
import com.example.paultikhonov.uklontestapp.modules.DirectionFinder;
import com.example.paultikhonov.uklontestapp.view.DirectionFinderListener;
import com.example.paultikhonov.uklontestapp.model.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int ADDRESSES_LIST_ORIGIN_REQUEST_CODE = 2;
    private static final int ADDRESSES_LIST_DESTINATION_REQUEST_CODE = 3;

    public static final String ADDRESSES_LIST_REQUEST_VALUE = "address";

    @NonNull
    private MainRouter mRouter;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng mLatLng;
    private Marker mMarker;
    private Geocoder mGeocoder;

    private Button mBtnFindPath;
    private Button mBtnOrigin;
    private Button mBtnDestination;
    private EditText mEtOrigin;
    private EditText mEtDestination;

    private List<Marker> mOriginMarkers = new ArrayList<>();
    private List<Marker> mDestinationMarkers = new ArrayList<>();
    private List<Polyline> mPolylinePaths = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mRouter = new MainRouter(this);

        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(this);

        mGeocoder = new Geocoder(this, Locale.getDefault());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mBtnFindPath = (Button) findViewById(R.id.btnFindPath);
        mBtnOrigin = (Button) findViewById(R.id.btnOrigin);
        mBtnDestination = (Button) findViewById(R.id.btnDestination);
        mEtOrigin = (EditText) findViewById(R.id.etOrigin);
        mEtDestination = (EditText) findViewById(R.id.etDestination);

        mBtnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        mBtnOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRouter.showAddresses(ADDRESSES_LIST_ORIGIN_REQUEST_CODE);
            }
        });

        mBtnDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRouter.showAddresses(ADDRESSES_LIST_DESTINATION_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private void sendRequest() {
        String origin = mEtOrigin.getText().toString();
        String destination = mEtDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, getString(R.string.origin_message), Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, getString(R.string.destination_message), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        getDeviceLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub

                //save current location
                mLatLng = point;

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = mGeocoder.getFromLocation(point.latitude, point.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);

                if (address != null) {
                    if (address.getAddressLine(0) != null) {
                        if (mEtOrigin.isFocused()) {
                            mEtOrigin.setText(address.getAddressLine(0));
                            mEtOrigin.setSelection(mEtOrigin.getText().length());
                            com.example.paultikhonov.uklontestapp.model.Address address1 = new com.example.paultikhonov.uklontestapp.model.Address();
                            address1.setAddressName(address.getAddressLine(0));
                            AppDatabase.getAppDatabase(MapsActivity.this).addressDao().insertAll(address1);
                        } else if (mEtDestination.isFocused()) {
                            mEtDestination.setText(address.getAddressLine(0));
                            mEtDestination.setSelection(mEtDestination.getText().length());
                            com.example.paultikhonov.uklontestapp.model.Address address1 = new com.example.paultikhonov.uklontestapp.model.Address();
                            address1.setAddressName(address.getAddressLine(0));
                            AppDatabase.getAppDatabase(MapsActivity.this).addressDao().insertAll(address1);
                        }
                    }
                }

                //remove previously placed Marker
                if (mMarker != null) {
                    mMarker.remove();
                }

                //place marker where user just clicked
                mMarker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            }
        });
    }

    private void getDeviceLocation() {

    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */

        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location location = (Location) task.getResult();
                            LatLng currentLatLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                    14.0f);
                            mMap.moveCamera(update);
                        }
                    }
                });
            } else {
                PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION, false);
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, results,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            getDeviceLocation();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        mProgressDialog = ProgressDialog.show(this, getString(R.string.progress_dialog_title),
                getString(R.string.progress_dialog_message), true);

        if (mOriginMarkers != null) {
            for (Marker marker : mOriginMarkers) {
                marker.remove();
            }
        }

        if (mDestinationMarkers != null) {
            for (Marker marker : mDestinationMarkers) {
                marker.remove();
            }
        }

        if (mPolylinePaths != null) {
            for (Polyline polyline: mPolylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        mProgressDialog.dismiss();
        mPolylinePaths = new ArrayList<>();
        mOriginMarkers = new ArrayList<>();
        mDestinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            mOriginMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));

            mDestinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            mPolylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }

        if (requestCode == ADDRESSES_LIST_ORIGIN_REQUEST_CODE) {
            String address = data.getStringExtra(ADDRESSES_LIST_REQUEST_VALUE);
            mEtOrigin.setText(address);
            mEtOrigin.setSelection(address.length());
        }

        if (requestCode == ADDRESSES_LIST_DESTINATION_REQUEST_CODE) {
            String address = data.getStringExtra(ADDRESSES_LIST_REQUEST_VALUE);
            mEtDestination.setText(address);
            mEtDestination.setSelection(address.length());
        }

    }
}