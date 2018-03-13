package walkingschoolbus.cmpt276.ca.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import walkingschoolbus.cmpt276.ca.appUI.MapsActivity;
import walkingschoolbus.cmpt276.ca.dataObjects.Map;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * Created by seungdobaek on 2018-03-12.
 */

public class MainActivity_map_fragment extends Fragment {

    private MapView mapView;
    private GoogleMap mGoogleMap;
    private Map map;
    private String TAG = "MapFrag";
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient FLPC;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainactivity_map_fragment, container, false);

        map = map.getInstance();
        mapView = view.findViewById(R.id.MapFrag_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((GoogleMap googleMap) -> {
            mGoogleMap = googleMap;
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);

            MapsInitializer.initialize(getActivity());

            getCurrentDeviceLocation();

        });


        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void getCurrentDeviceLocation() {
        FLPC = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if (map.isLocationPermission()){
                Task location = FLPC.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM));
                            }
                        } else{
                            Toast.makeText(getContext(), "unable to get device location",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.e(TAG, "Security Excpetion(device location): " + e.getMessage());
        }
    }
}
