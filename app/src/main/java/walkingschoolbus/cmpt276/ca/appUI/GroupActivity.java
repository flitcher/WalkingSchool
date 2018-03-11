package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Proxy;
import java.security.acl.Group;
import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class GroupActivity extends AppCompatActivity {

    private ApiInterface proxy;
    private static final String TAG = "GroupActivity";
    private User user;
    private Token token;
    private WalkingGroups walkingGroups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        user = user.getInstance();
        createGroup();
        getlistgroup();
    }

    private void getlistgroup() {
        Button getListGroup = (Button) findViewById(R.id.GroupActivity_getGroupListbtn);
        getListGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<WalkingGroups>> caller = proxy.getGroups();
                ProxyBuilder.callProxy(GroupActivity.this, caller, returnedGroupList->response(returnedGroupList));
            }
        });
    }

    private void createGroup() {
        Button createBtn = (Button) findViewById(R.id.GroupActivity_createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText groupDescriptionText = (EditText) findViewById(R.id.GroupActivity_groupDescription);
                String groupDescription = groupDescriptionText.getText().toString();
                Log.i(TAG, groupDescription);
                if (!groupDescription.isEmpty()){
                    walkingGroups = new WalkingGroups();
                    walkingGroups.setGroupDescription(groupDescription);
                    walkingGroups.setLeader(user);
                    Log.i(TAG,walkingGroups.toString());
                    Call<Void> caller = proxy.createNewGroup(walkingGroups);
                    ProxyBuilder.callProxy(GroupActivity.this, caller, returnedNothing -> response(returnedNothing));
                }
            }
        });
    }

    private void response(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");
        Log.i(TAG, "WalkingGroups to string: "+ walkingGroups.toString());
        List<WalkingGroups> UserWalkingGroup = user.getWalkingGroups();
        for (WalkingGroups walkingGroup : UserWalkingGroup) {
            Log.w(TAG, "    walkingGroup: " + walkingGroup.toString());
        }
    }

    private void response(List<WalkingGroups> returnedWalkingGroup) {
        Log.w(TAG, "All walkingGroup:");
        for (WalkingGroups walkingGroup : returnedWalkingGroup) {
            Log.w(TAG, "    walkingGroup: " + walkingGroup.toString());
        }
    }
    /*
    private void response(List<User> returnedUsers) {
        Log.w(TAG, "All Users:");
        for (User user : returnedUsers) {
            Log.w(TAG, "    User: " + user.toString());
        }
    }
    */

    private void response(WalkingGroups returnedwalkingGroups) {
        Log.w(TAG, "server replied with user: " + returnedwalkingGroups.toString());
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);

        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, GroupActivity.class);
        return intent;
    }
}
