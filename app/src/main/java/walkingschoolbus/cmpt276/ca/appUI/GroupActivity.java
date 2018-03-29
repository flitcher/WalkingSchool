package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * activities and features corresponding to walking group
 */
public class GroupActivity extends AppCompatActivity {

    private static final String GROUPID = "walkingschoolbus.cmpt276.ca.appUI-GroupActivity-groupID";
    private static final String REQUESTCODE = "walkingschoolbus.cmpt276.ca.appUI-GroupActivity-requestCode";
    private static final int REQUEST_CODE_GROUPLIST_REMOVE = 2000;
    private static final int REQUEST_CODE_GROUPLIST_ADD = 1000;
    private ApiInterface proxy;
    private static final String TAG = "GroupActivity";
    private Long groupID;
    private Long userID;
    private Token token;
    private WalkingGroups walkingGroups;
    private User myUser;
    FloatingActionButton joinMonitorGroup;
    FloatingActionButton joinMeGroup;
    FloatingActionButton removeMonitorGroup;
    FloatingActionButton removeMeGroup;
    FloatingActionButton startWalking;
    Animation hideButton;
    Animation showButton;
    Animation hideLayout;
    Animation showLayout;
    Animation showLayoutRemove;
    Animation hideLayoutRemove;
    Animation showLayoutMessage;
    Animation hideLayoutMessage;
    int requestCode;
    private Handler handler = new Handler();

    //in app message
    FloatingActionButton broadcast;
    FloatingActionButton groupMessage;
    FloatingActionButton memberReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        extract_intent();
        initialize();
        setStartWalkingBtn();
    }



    private void setStartWalkingBtn() {
        startWalking = (FloatingActionButton) findViewById(R.id.groupActivity_startBtn);
        startWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OnWalkGPSLocation.makeIntent(GroupActivity.this);
                startActivity(intent);
            }
        });
    }

    private void initialize(){
        joinMeGroup = (FloatingActionButton) findViewById(R.id.GroupActivity_joinMeGroup);
        joinMonitorGroup = (FloatingActionButton) findViewById(R.id.GroupActivity_joinMonitorGroup);
        removeMeGroup = (FloatingActionButton) findViewById(R.id.GroupActivity_removeMeGroup);
        removeMonitorGroup = (FloatingActionButton) findViewById(R.id.GroupActivity_removeMonitorGroup);
        showButton = AnimationUtils.loadAnimation(GroupActivity.this, R.anim.show_button);
        hideButton = AnimationUtils.loadAnimation(GroupActivity.this, R.anim.hide_button);
        showLayout = AnimationUtils.loadAnimation(GroupActivity.this, R.anim.show_layout);
        hideLayout = AnimationUtils.loadAnimation(GroupActivity.this, R.anim.hide_layout);
        showLayoutRemove = AnimationUtils.loadAnimation(GroupActivity.this, R.anim.show_layout_remove);
        hideLayoutRemove = AnimationUtils.loadAnimation(GroupActivity.this, R.anim.hide_layout_remove);
        showLayoutMessage = AnimationUtils.loadAnimation(GroupActivity.this, R.anim.show_layout_message);
        hideLayoutMessage = AnimationUtils.loadAnimation(GroupActivity.this, R.anim.hide_layout_message);
        broadcast = (FloatingActionButton)findViewById(R.id.GroupActitvity_BoardcastMessage);
        groupMessage = (FloatingActionButton) findViewById(R.id.GroupActitvity_groupMessage);
        memberReport = (FloatingActionButton) findViewById(R.id.GroupActivity_memberReport);
        token = token.getInstance();
        myUser = myUser.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<WalkingGroups> caller = proxy.getOneGroup(groupID);
        ProxyBuilder.callProxy(GroupActivity.this, caller, returnedGroup->response(returnedGroup));
        setVisibility();
    }

    private void setVisibility() {
        LinearLayout joinBtnLayout = (LinearLayout) findViewById(R.id.GroupActivity_joinGroupLayout);
        LinearLayout removeBtnLayout = (LinearLayout) findViewById(R.id.GroupActivity_RemoveBtnLayout);
        LinearLayout editBtnLayout = (LinearLayout) findViewById(R.id.GroupActivity_EditBtnLayout);
        LinearLayout messageBtnLayout = (LinearLayout) findViewById(R.id.GroupActivity_messageBtnLayout);
        switch(requestCode){
            case 5000: //from all group or accessing from other user's profile
                editBtnLayout.setVisibility(View.GONE);
                joinBtnLayout.setVisibility(View.VISIBLE);
                removeBtnLayout.setVisibility(View.GONE);
                messageBtnLayout.setVisibility(View.GONE);
                break;
            case 6000: //user is a leader
                editBtnLayout.setVisibility(View.VISIBLE);
                joinBtnLayout.setVisibility(View.VISIBLE);
                removeBtnLayout.setVisibility(View.VISIBLE);
                messageBtnLayout.setVisibility(View.VISIBLE);
                break;
            case 7000: //user is a member
                editBtnLayout.setVisibility(View.GONE);
                joinBtnLayout.setVisibility(View.VISIBLE);
                removeBtnLayout.setVisibility(View.VISIBLE);
                messageBtnLayout.setVisibility(View.VISIBLE);
                break;
        }
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
        LinearLayout member = (LinearLayout) findViewById(R.id.GroupActivity_member);
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = GroupMemberActivity.makeIntent(GroupActivity.this, groupID);
                startActivity(intent);
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
                        }
                    });
                    dialog.show();
                }
            }
        });

        FloatingActionButton joinGroup = (FloatingActionButton) findViewById(R.id.GroupActivity_joinGroup);
        LinearLayout joinMeLayout = (LinearLayout) findViewById(R.id.GroupActivity_joinMeLayout);
        LinearLayout joinMonitorLayout = (LinearLayout) findViewById(R.id.GroupActivity_joinMonitorLayout);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (joinMeLayout.getVisibility() == View.VISIBLE && joinMonitorLayout.getVisibility()
                        == View.VISIBLE){
                    joinMeLayout.setVisibility(View.GONE);
                    joinMonitorLayout.setVisibility(View.GONE);
                    joinGroup.startAnimation(hideButton);
                    joinMeLayout.startAnimation(hideLayout);
                    joinMonitorLayout.startAnimation(hideLayout);
                } else{
                    joinMeLayout.setVisibility(View.VISIBLE);
                    joinMonitorLayout.setVisibility(View.VISIBLE);
                    joinGroup.startAnimation(showButton);
                    joinMeLayout.startAnimation(showLayout);
                    joinMonitorLayout.startAnimation(showLayout);
                }
            }
        });

        joinMeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Long> payload = new HashMap<>();
                payload.put("id", myUser.getId());
                Call<List<User>> caller = proxy.addNewGroupMember(groupID, payload);
                ProxyBuilder.callProxy(GroupActivity.this, caller, returnedUser->responseAddNewUser(returnedUser));
            }
        });

        joinMonitorGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ListMonitoringGroup.makeIntent(GroupActivity.this);
                startActivityForResult(intent, REQUEST_CODE_GROUPLIST_ADD);
            }
        });

        FloatingActionButton editGroup = (FloatingActionButton) findViewById(R.id.GroupActivity_EditBtn);
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AditGroup.makeIntent(GroupActivity.this, groupID);
                startActivity(intent);
            }
        });
        FloatingActionButton removeGroup = (FloatingActionButton) findViewById(R.id.GroupActivity_RemoveBtn);
        LinearLayout removeMeLayout = (LinearLayout) findViewById(R.id.GroupActivity_removeMeLayout);
        LinearLayout removeMonitorLayout = (LinearLayout) findViewById(R.id.GroupActivity_removeMonitorLayout);
        removeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removeMeLayout.getVisibility() == View.VISIBLE && removeMonitorLayout.getVisibility()
                        == View.VISIBLE){
                    removeMeLayout.setVisibility(View.GONE);
                    removeMonitorLayout.setVisibility(View.GONE);
                    removeMeLayout.startAnimation(hideLayoutRemove);
                    removeMonitorLayout.startAnimation(hideLayoutRemove);
                } else{
                    removeMeLayout.setVisibility(View.VISIBLE);
                    removeMonitorLayout.setVisibility(View.VISIBLE);
                    removeMeLayout.startAnimation(showLayoutRemove);
                    removeMonitorLayout.startAnimation(showLayoutRemove);
                }
            }
        });
        removeMeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Void> caller = proxy.deleteGroupMember(groupID, myUser.getId());
                ProxyBuilder.callProxy(GroupActivity.this, caller, returnedNothing-> responseRemove(returnedNothing));
            }
        });
        removeMonitorGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ListMonitoringGroup.makeIntent(GroupActivity.this);
                startActivityForResult(intent, REQUEST_CODE_GROUPLIST_REMOVE);
            }
        });

        FloatingActionButton messageBtn = (FloatingActionButton) findViewById(R.id.GroupActivity_messageBtn);
        LinearLayout broadcastLayout = (LinearLayout) findViewById(R.id.GroupActivity_BroadcastMessageLayout);
        LinearLayout groupMessageLayout = (LinearLayout) findViewById(R.id.GroupActivity_groupMessageLayout);
        LinearLayout memberReportLayout = (LinearLayout) findViewById(R.id.GroupActivity_memberReportLayout);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupMessageLayout.getVisibility() == View.VISIBLE){
                    groupMessageLayout.setVisibility(View.GONE);
                    groupMessageLayout.startAnimation(hideLayoutMessage);
                    if (broadcastLayout.getVisibility() == View.VISIBLE){
                        broadcastLayout.setVisibility(View.GONE);
                        broadcastLayout.startAnimation(hideLayoutMessage);
                    }
                    if (memberReportLayout.getVisibility() == View.VISIBLE){
                        memberReportLayout.setVisibility(View.GONE);
                        memberReportLayout.startAnimation(hideLayoutMessage);
                    }
                } else{
                    groupMessageLayout.setVisibility(View.VISIBLE);
                    groupMessageLayout.startAnimation(showLayoutMessage);
                    switch(requestCode){
                        case 6000:
                            broadcastLayout.setVisibility(View.VISIBLE);
                            broadcastLayout.startAnimation(showLayoutMessage);
                            break;
                        case 7000:
                            memberReportLayout.setVisibility(View.VISIBLE);
                            memberReportLayout.startAnimation(showLayoutMessage);
                            break;
                    }
                }
            }
        });
        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SendingActivity.makeIntent(GroupActivity.this,groupID);
                startActivity(intent);
            }
        });
        groupMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProxyBuilder.SimpleCallback<List<Message>> callback = returnMessageList->responseGroupMessage(returnMessageList);
                ServerManager.getGroupMessage(groupID,callback);
            }
        });
        memberReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MemberReportActivity.makeIntent(GroupActivity.this,groupID);
                startActivity(intent);
            }
        });


    }


    private void responseRemove(Void returnedNothing) {
        Log.i(TAG, "Server remove user from group");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GROUPLIST_REMOVE){
            if (resultCode == RESULT_OK){
                userID = ListMonitoringGroup.getUserID(data);
                if (userID == -1){
                    Toast.makeText(GroupActivity.this, "Invalid user: please try again", Toast.LENGTH_LONG);
                }
                else{
                    Call<Void> caller = proxy.deleteGroupMember(groupID, userID);
                    ProxyBuilder.callProxy(GroupActivity.this, caller, returnedNothing-> responseRemove(returnedNothing));
                }

            }
        }
        if (requestCode == REQUEST_CODE_GROUPLIST_ADD){
            if(resultCode == RESULT_OK){
                userID = ListMonitoringGroup.getUserID(data);
                if (groupID == -1){
                    Toast.makeText(GroupActivity.this, "Invalid user: please try again", Toast.LENGTH_LONG);
                }
                else{
                    Map<String, Long> payload = new HashMap<>();
                    payload.put("id", userID);
                    Call<List<User>> caller = proxy.addNewGroupMember(groupID, payload);
                    ProxyBuilder.callProxy(GroupActivity.this, caller, returnedUser->responseAddNewUser(returnedUser));
                }
            }
        }
    }

    private void responseAddNewUser(List<User> returnedUser) {
        Log.w(TAG, "All Users");
        for (User user : returnedUser){
            Log.w(TAG, "    User: "+user.toString());
        }
    }

    private void extract_intent() {
        Intent intent = getIntent();
        groupID = intent.getLongExtra(GROUPID, -1);
        requestCode = intent.getIntExtra(REQUESTCODE, -1);
    }



    public static Intent makeIntent(Context context, Long groupId, int requestCode){
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra(GROUPID, groupId);
        intent.putExtra(REQUESTCODE, requestCode);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    //return
    private void responseGroupMessage(List<Message> messageList){
        Intent intent = GroupMessage.makeIntent(GroupActivity.this,messageList);
        startActivity(intent);
    }

}
