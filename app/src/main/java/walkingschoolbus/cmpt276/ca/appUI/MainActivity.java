package walkingschoolbus.cmpt276.ca.appUI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import walkingschoolbus.cmpt276.ca.dataObjects.Map;
import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.fragment.MainActivity_group_fragment;
import walkingschoolbus.cmpt276.ca.fragment.MainActivity_map_fragment;
import walkingschoolbus.cmpt276.ca.fragment.MainActivity_profile_fragment;
import walkingschoolbus.cmpt276.ca.fragment.SectionsPageAdapter;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_INFO;

/**
 * Dashboard for the user
 */
public class MainActivity extends AppCompatActivity {

    public static final String USER_ID = "id";
    private static String TAG = "MainActivity";
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private User myUser = User.getInstance();
    private Map map;
    private Timer timer = new Timer();
    Location lastLocation;
    Location newLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServerManager.connectToServerWithToken(MainActivity.this);
        getUser();

        Toolbar toolbar = findViewById(R.id.MainActivity_toolbar);
        setSupportActionBar(toolbar);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.MainActivity_container);
        setUpViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.MainActivity_tabs);
        tabLayout.setupWithViewPager(viewPager);
        getLocationPermission();
    }

    private void trackDistance() {
        lastLocation = getCurrentLocation();
        if (lastLocation == null) {
            Log.i(TAG, "lastLocation is null");
        }
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                newLocation = getCurrentLocation();
                if (newLocation != null){
                    float distance = lastLocation.distanceTo(newLocation);
                    Log.i(TAG, "distance traveled: " + distance);
                    lastLocation = newLocation;
                    newLocation = null;
                } else{
                    Log.i(TAG, "newLocation is null");
                }
                handler.postDelayed(this, 10000);
            }
        });
    }

    private Location getCurrentLocation() {
        if (map.isLocationPermission()){
            Log.d(TAG, "get current location");
            Criteria criteria = new Criteria();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "No permission to access GPS", Toast.LENGTH_SHORT).show();
                return null;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                return location;
            } else{
                Toast.makeText(MainActivity.this, "Your Location is not valid", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        Toast.makeText(MainActivity.this, "No permission to access GPS", Toast.LENGTH_LONG).show();
        return null;
    }

    private void setUpViewPager(ViewPager viewPager){
         sectionsPageAdapter.addFragment(new MainActivity_profile_fragment(), "PROFILE");
         sectionsPageAdapter.addFragment(new MainActivity_group_fragment(), "GROUP");
         sectionsPageAdapter.addFragment(new MainActivity_map_fragment(), "MAP");
         viewPager.setAdapter(sectionsPageAdapter);
         refreshSharedPreferences();
    }

    private void getUser(){
        String email = myUser.getEmail();
        String pass = myUser.getPassword();

        Log.i(TAG, ""+email);
        Log.i(TAG, ""+pass);
    }

    private void refreshSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID, String.valueOf(myUser.getId()));
        editor.commit();
    }

    private void getLocationPermission() {
        map = map.getInstance();
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setLocationPermission(true);
                trackDistance();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i (TAG, "entered for result");
        map.setLocationPermission(false);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    Log.i (TAG, "entered for result if");
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            map.setLocationPermission(false);
                            return;
                        }
                    }
                    map.setLocationPermission(true);
                    trackDistance();
                }
        }
    }
    public void logOut(){
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        ServerManager.setDoLogin(false);
        myUser.removeAllUserInfo();
        Intent intent = LoginActivity.makeIntent(MainActivity.this);
        startActivity(intent);

        timer.cancel();
        timer.purge();
        finish();

    }




    private void refreshMessageList(){
        ProxyBuilder.SimpleCallback<List<Message>> callback = returnedMessageList->responseUnreadMessage(returnedMessageList);
        ServerManager.refreshUnreadMessage(myUser.getId(),callback);
    }
    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    //response
    private void responseUnreadMessage(List<Message> messageList){
        myUser.setUnreadMessages(messageList);
        ProxyBuilder.SimpleCallback<List<Message>> callback = returnedMessageList->responeReadMessage(returnedMessageList);
        ServerManager.refreshReadMessage(myUser.getId(),callback);
    }
    private void responeReadMessage(List<Message> messageList){
        myUser.setReadMessages(messageList);
    }





    @Override
    protected void onResume() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshMessageList();
            }
        }, 0, 60000);
        super.onResume();
    }
}

