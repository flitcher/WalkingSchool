package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class AditGroup extends AppCompatActivity {
    public static final String GROUP_ID = "walkingschoolbus.cmpt276.ca.appUI-AditGroup-GroupID";
    private static final int REQUEST_EDIT = 8000;
    private static final int REQUEST_CREATE = 9000;
    private static final String TAG = "AditGroup";

    private int requestType;
    private Long groupID;
    private EditText groupDescription;
    private EditText startingLat;
    private EditText startingLong;
    private EditText destinationLat;
    private EditText destinationLong;
    private WalkingGroups walkingGroups;
    private ApiInterface proxy;
    Token token;
    User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adit_group);
        Log.i(TAG, "current");

        extractIntent();

        initiate();
    }

    private void initiate() {
        token = token.getInstance();
        myUser = myUser.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        groupDescription = (EditText) findViewById(R.id.AditGroup_groupDescription);
        startingLat = (EditText) findViewById(R.id.AditGroup_StartingLat);
        startingLong = (EditText) findViewById(R.id.AditGroup_StartingLong);
        destinationLat = (EditText) findViewById(R.id.AditGroup_DestinationLat);
        destinationLong = (EditText) findViewById(R.id.AditGroup_DestinationLong);
        TextView info = (TextView) findViewById(R.id.AditGroup_AditInfo);
        switch(requestType){
            case REQUEST_CREATE:
                walkingGroups = new WalkingGroups();
                info.setText("Create Group");
                break;
            case REQUEST_EDIT:
                Call<WalkingGroups> caller = proxy.getOneGroup(groupID);
                info.setText("Edit Group");
                ProxyBuilder.callProxy(AditGroup.this, caller, returnedGroup->responseGetGroup(returnedGroup));
        }
        setupOKBtn();

    }

    private void responseGetGroup(WalkingGroups returnedGroup) {
        groupDescription.setText(returnedGroup.getGroupDescription());
        double[] routeLatArray = returnedGroup.getRouteLatArray();
        double[] routeLongArray = returnedGroup.getRouteLngArray();
        if (routeLatArray != null && routeLatArray.length == 2){
            startingLat.setText(""+routeLatArray[0]);
            destinationLat.setText(""+routeLatArray[1]);
        }
        if (routeLongArray != null && routeLongArray.length == 2){
            startingLong.setText(""+routeLongArray[0]);
            destinationLong.setText(""+routeLongArray[1]);
        }
        walkingGroups = returnedGroup;
    }

    private void setupOKBtn() {
        FloatingActionButton OKBtn = (FloatingActionButton) findViewById(R.id.AditGroup_OKBtn);
        OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupDescriptionString = groupDescription.getText().toString();
                walkingGroups.setGroupDescription(groupDescriptionString);
                if (!startingLat.getText().toString().isEmpty() && !startingLong.getText().toString().isEmpty()
                        && !destinationLat.getText().toString().isEmpty() && !destinationLong.getText().toString().isEmpty()) {
                    Double startingLatDouble = Double.parseDouble(startingLat.getText().toString());
                    Double startingLongDouble = Double.parseDouble(startingLong.getText().toString());
                    Double destinationLatDouble = Double.parseDouble(destinationLat.getText().toString());
                    Double destinationLongDouble = Double.parseDouble(destinationLong.getText().toString());
                    double[] routeLatArray = new double[2];
                    routeLatArray[0] = startingLatDouble;
                    routeLatArray[1] = destinationLatDouble;
                    double[] routeLongArray = new double[2];
                    routeLongArray[0] = startingLongDouble;
                    routeLongArray[1] = destinationLongDouble;
                    walkingGroups.setRouteLatArray(routeLatArray);
                    walkingGroups.setRouteLngArray(routeLongArray);
                }
                switch(requestType){
                    case REQUEST_CREATE:
                        walkingGroups.setLeader(myUser);
                        Call<WalkingGroups> caller = proxy.createNewGroup(walkingGroups);
                        ProxyBuilder.callProxy(AditGroup.this, caller, returnedGroup -> responseForReturnedGroup(returnedGroup));
                        break;
                    case REQUEST_EDIT:
                        Call<WalkingGroups> callerForEdit = proxy.updateExistingGroup(groupID, walkingGroups);
                        ProxyBuilder.callProxy(AditGroup.this, callerForEdit, returnedGroup -> responseForReturnedGroup(returnedGroup));
                        break;
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private static void responseForReturnedGroup(WalkingGroups returnedGroup) {
        Log.i(TAG, "Server returned group: " + returnedGroup.toString());
    }

    private void extractIntent(){
        Intent intent = getIntent();
        groupID = intent.getLongExtra(GROUP_ID, -2);
        if (groupID == -1){
            requestType = REQUEST_CREATE;
        }
        else{
            requestType = REQUEST_EDIT;
        }
    }

    public static Intent makeIntent(Context context, Long groupID){
        Intent intent = new Intent(context, AditGroup.class);
        intent.putExtra(GROUP_ID, groupID);
        return intent;
    }
}
