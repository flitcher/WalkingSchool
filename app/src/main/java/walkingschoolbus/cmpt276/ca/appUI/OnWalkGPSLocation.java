package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import walkingschoolbus.cmpt276.ca.dataObjects.Map;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class OnWalkGPSLocation extends AppCompatActivity {

    private String activityState = "STOPPED";
    private static final String TAG = "onwalk";

    private FusedLocationProviderClient FLPC;
    Map map;

    private User userManager = User.getInstance();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_walk_gpslocation);
        map = map.getInstance();
        startWalkingBtn();

    }

    private void startWalkingBtn() {
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.OnWalk_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activityState.equals("RUNNING")) {
                    handler.removeCallbacks(updateLastGPSCoordinate);
                    setActivityState("STOPPED");
                    finish();
                } else {
                    handler.post(updateLastGPSCoordinate);
                    setActivityState("RUNNING");
                }
            }
        });
    }

    private Runnable updateLastGPSCoordinate = new Runnable() {
        @Override
        public void run() {

            editUser();
            Log.d(TAG, "location updating");

            //run every 30s
            handler.postDelayed(updateLastGPSCoordinate, 30000);
        }
    };

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, OnWalkGPSLocation.class);
        return intent;
    }

    public String getActivityState() {
        return activityState;
    }

    public void setActivityState(String activityState) {
        this.activityState = activityState;
    }

    private void getDeviceLocation() {
        FLPC = LocationServices.getFusedLocationProviderClient(OnWalkGPSLocation.this);
        try {
            if(map.isLocationPermission()) {
                Task location = FLPC.getLastLocation();
                Log.d(TAG, "location being updated?");
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if(currentLocation != null) {
                                userManager.getLastGpsLocation().setLat(currentLocation.getLatitude());
                                userManager.getLastGpsLocation().setLng(currentLocation.getLongitude());
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Security Excpetion(device location): " + e.getMessage());
        }
    }
    private void editUser(){
        //udpate GPS coordiante
        //edit user by setting its gps coordinate
        //send it to the server
        getDeviceLocation();
        ProxyBuilder.SimpleCallback<User> callback = returnedUser-> responseEdit(returnedUser);
        ServerManager.editUserProfile(userManager,callback);
    }

    private void responseEdit(User user){
        Log.d(TAG, "gps location updated");
        Log.d(TAG, "Long, Lat = " + userManager.getLastGpsLocation().getLat() +
                userManager.getLastGpsLocation().getLng());
    }
}
