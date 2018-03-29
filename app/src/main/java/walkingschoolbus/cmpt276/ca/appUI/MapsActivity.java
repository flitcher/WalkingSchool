package walkingschoolbus.cmpt276.ca.appUI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Map;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap mMap;
    private FusedLocationProviderClient FLPC;
    private EditText searchAdressText;
    Map map;
    ApiInterface proxy;
    Token token;
    User myUser;
    List<User> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initialize();
    }

    private void initialize() {
        myUser = myUser.getInstance();
        childList = myUser.getMonitorsUsers();
        map = map.getInstance();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        if (map.isLocationPermission()){
            initMap();
        }
        searchAdressText = (EditText) findViewById(R.id.MapsActivity_searchText);
        initSearch();
        showChildLocation();
    }

    private void showChildLocation() {
        if (childList != null) {
            for (User child : childList) {
                Call<User> caller = proxy.getUserById(child.getId());
                ProxyBuilder.callProxy(MapsActivity.this, caller, returndChild -> responseChild(returndChild));
            }
        }
    }

    private void responseChild(User returndChild) {
        walkingschoolbus.cmpt276.ca.dataObjects.Location childLocation = returndChild.getLastGpsLocation();
        if (childLocation != null){
            if (returndChild.getLastGpsLocation().getTimestamp() != null) {
                LatLng childLatLng = new LatLng(childLocation.getLat(), childLocation.getLng());
                mMap.addMarker(new MarkerOptions()
                        .title("Last Location of " + returndChild.getName() +
                                "/Time updated at: " + returndChild.getLastGpsLocation().getTimestamp())
                        .position(childLatLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (map.isLocationPermission()) {

            getCurrentDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            Call<List<WalkingGroups>> caller = proxy.getGroups();
            ProxyBuilder.callProxy(MapsActivity.this, caller, returnedGroups->response(returnedGroups));
        }
    }

    private void response(List<WalkingGroups> returnedGroups) {
       if (returnedGroups != null) {
           for (WalkingGroups groups : returnedGroups) {
               if (groups.getRouteLatArray() == null || groups.getRouteLngArray() == null
                       || groups.getRouteLngArray().length < 2 || groups.getRouteLatArray().length <2){
               }
               else{
                   LatLng startLatLng = new LatLng(groups.getRouteLatArray()[0], groups.getRouteLngArray()[0]);
                   LatLng destLatLng = new LatLng(groups.getRouteLatArray()[1], groups.getRouteLngArray()[1]);
                   mMap.addMarker(new MarkerOptions()
                           .position(startLatLng)
                           .draggable(true)
                           .title(groups.getGroupDescription()+": start position"));
                   mMap.addMarker(new MarkerOptions().position(destLatLng).title(groups.getGroupDescription()+": destination"));
                   mMap.getUiSettings().setZoomControlsEnabled(true);
                   mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                       @Override
                       public void onMarkerDragStart(Marker marker) {
                           Intent intent = GroupActivity.makeIntent(MapsActivity.this, groups.getId(), 5000);
                           startActivity(intent);
                       }

                       @Override
                       public void onMarkerDrag(Marker marker) {

                       }

                       @Override
                       public void onMarkerDragEnd(Marker marker) {

                       }
                   });
               }
           }
       }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.MapsActivity_map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void initSearch(){
        searchAdressText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getAction() == keyEvent.ACTION_DOWN || keyEvent.getAction() == keyEvent.KEYCODE_ENTER){
                    searchAddress();
                }
                return false;
            }
        });
        ImageView gpsImage = (ImageView) findViewById(R.id.MapsActivity_gpsIcon);
        gpsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }

    private void searchAddress(){
        String addressString = searchAdressText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> listAddress = new ArrayList<>();
        try{
            listAddress = geocoder.getFromLocationName(addressString, 1);
        } catch(IOException e){
            Log.e(TAG, "searchAddress: IOExcetpoin: "+e.getMessage());
        }
        if (listAddress.size() > 0){
            Address addressSearched = listAddress.get(0);
            Log.d (TAG, "found Location: " + addressSearched.toString());
            LatLng latLng = new LatLng(addressSearched.getLatitude(),addressSearched.getLongitude());
            moveCamera(latLng, DEFAULT_ZOOM);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(addressSearched.getAddressLine(0));
            mMap.addMarker(markerOptions);
        }
    }

    private void getCurrentDeviceLocation() {
        FLPC = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        try {
            if (map.isLocationPermission()){
                Task location = FLPC.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);
                            }
                        } else{
                            Toast.makeText(MapsActivity.this, "unable to get device location",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.e(TAG, "Security Excpetion(device location): " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MapsActivity.class);
        return intent;
    }
}
