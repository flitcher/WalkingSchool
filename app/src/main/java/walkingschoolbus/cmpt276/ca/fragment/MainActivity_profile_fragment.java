package walkingschoolbus.cmpt276.ca.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.appUI.ChildMessage;
import walkingschoolbus.cmpt276.ca.appUI.MainActivity;
import walkingschoolbus.cmpt276.ca.appUI.ProfileActivity;
import walkingschoolbus.cmpt276.ca.appUI.UnreadMessageActivity;
import walkingschoolbus.cmpt276.ca.appUI.readMessageActivity;
import walkingschoolbus.cmpt276.ca.dataObjects.Map;
import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;
import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_INFO;

/**
 * Created by seungdobaek on 2018-03-12.
 */

public class MainActivity_profile_fragment extends Fragment {
    View view;
    User myUser;
    private static final String TAG = "Profile";
    Context context;
    Animation showLayout;
    Animation hideLayout;
    Map map;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiInterface proxy;
    Token token;
    private static final String SWITCHINFO = "walkingschoolbus.cmpt276.ca.fragment-ProfileFrag-switchInfo";


    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mainactivity_profile_fragment, container, false);

        myUser = myUser.getInstance();
        map = map.getInstance();
        ServerManager.connectToServerWithToken(getContext());
        context = getContext();
        setupBtn();
        setupSwitch();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        return view;
    }

    private void setupSwitch() {
        sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Switch walkingWithGroupSwitch = (Switch) view.findViewById(R.id.ProfileFrag_walkingWithGroupSwitch);
        if(sharedPreferences.getBoolean(SWITCHINFO, false)){
            walkingWithGroupSwitch.setChecked(true);
            handler.post(updateLastGPSCoordinate);
        }
        walkingWithGroupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putBoolean(SWITCHINFO, true);
                    handler.post(updateLastGPSCoordinate);
                } else {
                    editor.putBoolean(SWITCHINFO, false);
                    handler.removeCallbacks(updateLastGPSCoordinate);
                }
                editor.commit();
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


    private void getDeviceLocation() {
        if (map.isLocationPermission()) {
            Log.d(TAG, "location being updated?");
            Criteria criteria = new Criteria();
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "No permission to access GPS", Toast.LENGTH_SHORT).show();
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date curr = new Date();
                String timestamp = sdf.format(curr);
                myUser.getLastGpsLocation().setTimestamp(timestamp);
                myUser.getLastGpsLocation().setLat(latitude);
                myUser.getLastGpsLocation().setLng(longitude);
            } else{
                Toast.makeText(context, "Your Location is not valid", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void editUser(){
        //udpate GPS coordiante
        //edit user by setting its gps coordinate
        //send it to the server
        getDeviceLocation();
        Call<walkingschoolbus.cmpt276.ca.dataObjects.Location> caller = proxy.setUserLocation(myUser.getId(), myUser.getLastGpsLocation());
        ProxyBuilder.callProxy(context, caller, returnedLocation -> responseEdit(returnedLocation));
    }

    private void responseEdit(walkingschoolbus.cmpt276.ca.dataObjects.Location location){
        Log.d(TAG, "gps location updated");
        Log.d(TAG, "Lat, Long = " + location.getLat() +
                location.getLng()+" Time: "+ location.getTimestamp());
    }

    private void setupBtn() {
        Button userProfileBtn = (Button) view.findViewById(R.id.ProfileFrag_profile);
        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ProfileActivity.makeIntent(getContext(), myUser.getId());
                startActivity(intent);
            }
        });
        FloatingActionButton logoutBtn = (FloatingActionButton) view.findViewById(R.id.ProfileFrag_logOut);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).logOut();
            }
        });


        showLayout = AnimationUtils.loadAnimation(context, R.anim.show_layout_message);
        hideLayout = AnimationUtils.loadAnimation(context, R.anim.hide_layout_message);

        FloatingActionButton messageInfoBtn = (FloatingActionButton) view.findViewById(R.id.ProfileFrag_MessageInfo);
        LinearLayout readMessageLayout = (LinearLayout) view.findViewById(R.id.ProfileFrag_readMessageLayout);
        LinearLayout unreadMessageLayout = (LinearLayout) view.findViewById(R.id.ProfileFrag_unreadMessageLayout);
        messageInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readMessageLayout.getVisibility() == View.VISIBLE &&
                        unreadMessageLayout.getVisibility() == View.VISIBLE){
                    readMessageLayout.setVisibility(View.GONE);
                    readMessageLayout.startAnimation(hideLayout);
                    unreadMessageLayout.setVisibility(View.GONE);
                    unreadMessageLayout.startAnimation(hideLayout);
                } else{
                    readMessageLayout.setVisibility(View.VISIBLE);
                    readMessageLayout.startAnimation(showLayout);
                    unreadMessageLayout.setVisibility(View.VISIBLE);
                    unreadMessageLayout.startAnimation(showLayout);
                }
            }
        });

        FloatingActionButton readMessageBtn = (FloatingActionButton) view.findViewById(R.id.ProfileFrag_readMessage);
        readMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = readMessageActivity.makeIntent(context);
                startActivity(intent);
            }
        });

        FloatingActionButton unreadMessageBtn = (FloatingActionButton) view.findViewById(R.id.ProfileFrag_unreadMessage);
        unreadMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UnreadMessageActivity.makeIntent(context);
                startActivity(intent);
            }
        });

        Button panicBtn = (Button) view.findViewById(R.id.ProfileFrag_Panic);
        panicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickEmergencyMessage();
            }
        });
        panicBtn.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Intent intent = ChildMessage.makeIntent(getContext());
                startActivity(intent);
                return true;
            }
        });
    }

    private void quickEmergencyMessage(){
        String text = "Warning!!! "+ myUser.getName()+ " need help!! "+"ID: "+myUser.getId()+" Lat: "+ myUser.getLastGpsLocation().getLat()+" Lng: "+ myUser.getLastGpsLocation().getLng();
        for(int i = 0; i < myUser.getMemberOfGroups().size(); i++) {
            ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseCallGroup(returnedMessage);
            ServerManager.callToGroup(myUser.getMemberOfGroups().get(i).getId(),text,callback);
        }
        ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseCallGroup(returnedMessage);
        ServerManager.callToParent(myUser.getId(),text,callback);
    }

    private void responseCallGroup(Message message){
        Log.i("User","send successful!");
    }
}
