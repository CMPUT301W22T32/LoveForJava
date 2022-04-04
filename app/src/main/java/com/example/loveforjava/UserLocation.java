package com.example.loveforjava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class UserLocation {
    Context cxt;

    public UserLocation(Context context){
        cxt = context;
    }

    @SuppressLint("MissingPermission")
    public Location getLocation(){
        LocationManager locationManager = (LocationManager) cxt.getSystemService(Context.LOCATION_SERVICE);
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
            Log.i("MAP", location.getLongitude()+", "+ location.getLatitude());
            return location;
        }
        return location;
    }

    private void locPermission() {
        if (ActivityCompat.checkSelfPermission(cxt,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(cxt, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
