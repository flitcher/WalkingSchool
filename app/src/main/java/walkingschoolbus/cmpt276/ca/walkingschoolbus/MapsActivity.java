package walkingschoolbus.cmpt276.ca.walkingschoolbus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.security.KeyChain;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "MapsActivity";
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap mMap;
    private boolean myLocationPermission = false;
    private FusedLocationProviderClient FLPC;
    private EditText searchAdressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getLocationPermission();
        searchAdressText = (EditText) findViewById(R.id.MapsActivity_searchText);
        initSearch();
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

        // Add a marker in Sydney and move the camera
        if (myLocationPermission) {

            getCurrentDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
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
    }

    private void searchAddress(){
        String address = searchAdressText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> listAddress = new ArrayList<>();
        try{
            listAddress = geocoder.getFromLocationName(address, 1);
        } catch(IOException e){
            Log.e(TAG, "searchAddress: IOExcetpoin: "+e.getMessage());
        }
        if (listAddress.size() > 0){
            Log.d (TAG, "found Location: " + listAddress.get(0).toString());
        }
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                myLocationPermission = true;
                initMap();
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
        myLocationPermission = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    Log.i (TAG, "entered for result if");
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            myLocationPermission = false;
                            return;
                        }
                    }
                    myLocationPermission = true;
                    initMap();
                }
        }
    }

    private void getCurrentDeviceLocation() {
        FLPC = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        try {
            if (myLocationPermission){
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
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MapsActivity.class);
        return intent;
    }
}
