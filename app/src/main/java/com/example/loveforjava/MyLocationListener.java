package com.example.loveforjava;

import android.location.Location;
import android.location.LocationListener;

/**
 * This class gets the current location of the user
 */
public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

}

