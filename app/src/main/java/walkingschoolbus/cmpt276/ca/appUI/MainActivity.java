package walkingschoolbus.cmpt276.ca.appUI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Map;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.fragment.MainActivity_group_fragment;
import walkingschoolbus.cmpt276.ca.fragment.MainActivity_map_fragment;
import walkingschoolbus.cmpt276.ca.fragment.MainActivity_profile_fragment;
import walkingschoolbus.cmpt276.ca.fragment.SectionsPageAdapter;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_INFO;


public class MainActivity extends AppCompatActivity {

    public static final String USER_ID = "id";
    private static String TAG = "MainActivity";
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private User userManager = User.getInstance();


    private User user;
    private ApiInterface proxy;
    Token token;
    Map map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.MainActivity_toolbar);
        setSupportActionBar(toolbar);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.MainActivity_container);
        setUpViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.MainActivity_tabs);
        tabLayout.setupWithViewPager(viewPager);
        ServerManager.connectToServerWithToken(MainActivity.this);
        getUser();
        getLocationPermission();
    }

    private void setUpViewPager(ViewPager viewPager){
         sectionsPageAdapter.addFragment(new MainActivity_profile_fragment(), "PROFILE");
         sectionsPageAdapter.addFragment(new MainActivity_group_fragment(), "GROUP");
         sectionsPageAdapter.addFragment(new MainActivity_map_fragment(), "MAP");
         viewPager.setAdapter(sectionsPageAdapter);
        refreshSharedPreferences();

        setBtn();
    }

    private void getUser(){
        String email = userManager.getEmail();
        String pass = userManager.getPassword();

        Log.i(TAG, ""+email);
        Log.i(TAG, ""+pass);
    }

    private void response(User returnedUser) {
        String email = returnedUser.getEmail();
        Long id = returnedUser.getId();
        String name = returnedUser.getName();
        String password = returnedUser.getPassword();
        Log.d("app", "email = " + email);
        List<User> MonitoredByUsers = returnedUser.getMonitoredByUsers();
        List<User> MonitorsUsers = returnedUser.getMonitorsUsers();
        List<WalkingGroups> memberOfGroups = returnedUser.getMemberOfGroups();
        List<WalkingGroups> leadsGroups = returnedUser.getLeadsGroups();
        String Href = returnedUser.getHref();
        user.setEmail(email);
        user.setId(id);
        user.setName(name);

        //i don't getUserByEmail returns you a password field
        //check the return Json in the docs
        user.setPassword(password);
        user.setMonitorsUsers(MonitorsUsers);
        user.setMonitoredByUsers(MonitoredByUsers);
        user.setMemberOfGroups(memberOfGroups);
        user.setLeadsGroups(leadsGroups);
        user.setHref(Href);

    private void refreshSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID, String.valueOf(userManager.getId()));
        editor.apply();
    }

    private void getLocationPermission() {
        map = map.getInstance();
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setLocationPermission(true);
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
                }
        }
    private void setBtn(){
        Button parentListBtn = (Button)findViewById(R.id.parentList);
        parentListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ParentActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button childListBtn = (Button)findViewById(R.id.Childlist);
        childListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChildActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button mapBtn = (Button) findViewById(R.id.MainActivity_mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MapsActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button logoutBtn = (Button) findViewById(R.id.MainActivity_logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                ServerManager.setDoLogin(false);
                Intent intent = LoginActivity.makeIntent(MainActivity.this);
                startActivity(intent);

                finish();
            }
        });


    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}

