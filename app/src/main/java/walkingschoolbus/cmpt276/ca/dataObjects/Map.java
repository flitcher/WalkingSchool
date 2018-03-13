package walkingschoolbus.cmpt276.ca.dataObjects;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import walkingschoolbus.cmpt276.ca.appUI.MapsActivity;

/**
 * Created by seungdobaek on 2018-03-12.
 */

public class Map {
    private boolean LocationPermission;

    private static Map instance;
    private Map(){};
    public static Map getInstance(){
        if (instance == null){
            instance = new Map();
            instance.setLocationPermission(false);
        }
        return instance;
    }

    public boolean isLocationPermission() {
        return LocationPermission;
    }

    public void setLocationPermission(boolean locationPermission) {
        LocationPermission = locationPermission;
    }
}
