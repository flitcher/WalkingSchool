package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.lang.reflect.Proxy;
import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class GroupActivity extends AppCompatActivity {

    public static final String GROUPID = "walkingschoolbus.cmpt276.ca.appUI-groupID";
    private ApiInterface proxy;
    private static final String TAG = "GroupActivity";
    private Long groupID;
    private Token token;
    private WalkingGroups walkingGroups;
    List<User> memberList;
    ListView members;
    private static final float DEFAULT_ZOOM = 15f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        extract_intent();
        initialize();
    }

    private void initialize(){
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<WalkingGroups> caller = proxy.getOneGroup(groupID);
        ProxyBuilder.callProxy(GroupActivity.this, caller, returnedGroup->response(returnedGroup));
    }

    private void response(WalkingGroups returnedWalkingGroups) {
        Log.w(TAG, "server replied with walkingGroup: " + returnedWalkingGroups.toString());
        walkingGroups = returnedWalkingGroups;
        setText();
    }

    private void setText() {
        TextView groupDescriptionTxt = (TextView) findViewById(R.id.GroupActivity_groupDescription);
        TextView groupIDTxt = (TextView) findViewById(R.id.GroupActivity_groupID);
        TextView startingPositionTxt = (TextView) findViewById(R.id.GroupActivity_startingPosition);
        TextView destinationTxt =  (TextView) findViewById(R.id.GroupActivity_Destination);
        groupDescriptionTxt.setText(walkingGroups.getGroupDescription());
        groupIDTxt.setText(""+walkingGroups.getId());
        if (walkingGroups.getRouteLatArray() == null || walkingGroups.getRouteLngArray() == null
                || walkingGroups.getRouteLngArray().length < 2 || walkingGroups.getRouteLatArray().length <2){
            startingPositionTxt.setText("Initial point: "+"Not available");
            destinationTxt.setText("Destination: "+"Not available");
        }
        else{
            startingPositionTxt.setText("Initial point: "+ Double.toString(walkingGroups.getRouteLatArray()[0]) + ", " +
                    Double.toString(walkingGroups.getRouteLngArray()[0]));
            destinationTxt.setText("Destination: "+Double.toString(walkingGroups.getRouteLatArray()[1]) + ", " +
                    Double.toString(walkingGroups.getRouteLngArray()[1]));
        }
        setClick();
    }

    private void setClick() {
        memberList = walkingGroups.getMembersUsers();
        LinearLayout member = (LinearLayout) findViewById(R.id.GroupActivity_member);
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_groupmember, null);
                Long userID = walkingGroups.getLeader().getId();
                Call<User> caller = proxy.getUserById(userID);
                ProxyBuilder.callProxy(GroupActivity.this, caller, returnedUser->responseLeader(returnedUser, mView));
                ArrayAdapter<User> adapter = new MyListadapter();
                members = (ListView) mView.findViewById(R.id.MemberDialog_member);
                if (memberList != null) {
                    members.setAdapter(adapter);
                }
                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        LinearLayout map = (LinearLayout) findViewById(R.id.GroupActivity_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (walkingGroups.getRouteLatArray() == null || walkingGroups.getRouteLngArray() == null
                        || walkingGroups.getRouteLngArray().length < 2 || walkingGroups.getRouteLatArray().length <2){
                    Toast.makeText(GroupActivity.this, "GPS coordinate are not valid", Toast.LENGTH_LONG).show();
                }
                else{
                    Dialog dialog = new Dialog(GroupActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_groupgps);
                    dialog.show();
                    MapView mapView = (MapView) dialog.findViewById(R.id.GroupGPS_mapView);
                    MapsInitializer.initialize(GroupActivity.this);
                    mapView.onCreate(dialog.onSaveInstanceState());
                    mapView.onResume();
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng startLatLng = new LatLng(walkingGroups.getRouteLatArray()[0], walkingGroups.getRouteLngArray()[0]);
                            LatLng destLatLng = new LatLng(walkingGroups.getRouteLatArray()[1], walkingGroups.getRouteLngArray()[1]);
                            googleMap.addMarker(new MarkerOptions().position(startLatLng).title("start position"));
                            googleMap.addMarker(new MarkerOptions().position(destLatLng).title("destination"));
                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, DEFAULT_ZOOM));
                        }
                    });
                    dialog.show();
                }
            }
        });

        FloatingActionButton joinGroup = (FloatingActionButton) findViewById(R.id.GroupActivity_joinGroup);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void responseLeader(User returnedUser, View view){
        User user = returnedUser;
        TextView leader = (TextView) view.findViewById(R.id.MemberDialog_leader);
        leader.setText(user.getName());
    }

    private class MyListadapter extends ArrayAdapter<User> {
        public MyListadapter(){
            super(GroupActivity.this, R.layout.memberlistlayout, memberList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View groupView = convertView;
            if (groupView == null){
                groupView = getLayoutInflater().inflate(R.layout.memberlistlayout, parent, false);
            }
            final View mView = groupView;
            User curUser = memberList.get(position);
            if (curUser != null) {
                Call<User> caller = proxy.getUserById(curUser.getId());
                ProxyBuilder.callProxy(GroupActivity.this, caller, returnedUser->responseMember(returnedUser, mView));
            }
            return mView;
        }
    }
    private void responseMember(User returnedUser, View view){
        TextView name = (TextView) view.findViewById(R.id.MemberLayout_name);
        name.setText(returnedUser.getName());
    }

    private void extract_intent() {
        Intent intent = getIntent();
        groupID = intent.getLongExtra(GROUPID, -1);
    }

    public static Intent makeIntent(Context context, Long groupId){
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra(GROUPID, groupId);
        return intent;
    }
}
