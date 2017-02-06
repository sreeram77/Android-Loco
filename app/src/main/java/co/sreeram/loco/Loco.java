package co.sreeram.loco;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class Loco extends Service
{
    private static final String TAG = "LOCOLOCO";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private String lat;
    private int x=0;
    private double la1,la2,lo1,lo2;


    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {

            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);


        }

        @Override
        public void onLocationChanged(Location location)
        {
            x++;
            if(x==1){
                la1=location.getLatitude();
                lo1=location.getLongitude();
            }
            la2=location.getLatitude();
            lo2=location.getLatitude();
            mLastLocation.set(location);
            Intent i = new Intent("LOCATION_UPDATED");
            lat=String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude());
            i.putExtra("BBBB",lat);
            sendBroadcast(i);
        }

        @Override
        public void onProviderDisabled(String provider){}

        @Override
        public void onProviderEnabled(String provider){}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "Failed ", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "GPS" + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Intent in = new Intent("LOCATION_UPDATED");
        in.putExtra("DIST",String.valueOf(getx()));
        sendBroadcast(in);
        x=0;
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "Failed", ex);
                }
            }
        }

    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public double getx()
    {
        double dist;
        if (x==1)
            dist=0;
        else {
            double earthRadius = 6371.0; // miles (or 6371.0 kilometers)
            double dLat = Math.toRadians(la2-la1);
            double dLng = Math.toRadians(lo2-lo1);
            double sindLat = Math.sin(dLat / 2);
            double sindLng = Math.sin(dLng / 2);
            double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                    * Math.cos(Math.toRadians(la1)) * Math.cos(Math.toRadians(la2));
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
             dist = earthRadius * c;

        }
        return (dist);

    }
    /*private double deg2rad(double deg) {
        return (deg * 3.14 / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / 3.14);
    }*/
}