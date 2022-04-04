package com.example.loveforjava;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Map_Activity extends AppCompatActivity {
    private MapView map = null;
    ArrayList<QRcode> qRcodes = new ArrayList<QRcode>();
    ArrayList<Marker> nearByQrCodes = new ArrayList<Marker>();
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18.0);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);


        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        //Location location = getLocation();
        UserLocation userLocation = new UserLocation(this);
        Location location = userLocation.getLocation();
        Log.i("MAP2", location.getLongitude()+", "+location.getLatitude());
        GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());

        Marker startMarker = new Marker(map);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);
        map.getController().setZoom(18.0);
        map.getController().setCenter(point);

        APIMain APIServer = new APIMain();
        APIServer.getAllCodes(new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                qRcodes = (ArrayList<QRcode>) response.get("data");

                for(int i=0; i < qRcodes.size(); i++) {
                    GeoPoint qrPoint;
                    Marker qrMarker;
                    try {
                        if (!qRcodes.get(i).getLoc().isEmpty()) {
                            Log.i("HERE", qRcodes.get(i).getLoc().get(0) + "," + qRcodes.get(i).getLoc().get(1));
                            qrPoint = new GeoPoint(Double.parseDouble(qRcodes.get(i).getLoc().get(1)),
                                    Double.parseDouble(qRcodes.get(i).getLoc().get(0)));
                            qrMarker = new Marker(map);
                            qrMarker.setPosition(qrPoint);
                            qrMarker.setTitle(qRcodes.get(i).getScore() + "");
                            qrMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                            nearByQrCodes.add(qrMarker);
                        }
                    }catch(Exception e){
                        System.out.println("Couldn't add nearby QR codes");
                    }
                }
                map.getOverlays().addAll(nearByQrCodes);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private Location getLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locPermission();
        Location location = null;
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        List<String> providers = locationManager.getProviders(true);
        Log.i("PROVIDERS", providers+"");
        for(String provider : providers){
            location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                continue;
            }
            Log.i("MAP", location.getLongitude()+", "+location.getLatitude());
            return location;
        }
        return location;
    }

    public void locPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }
}