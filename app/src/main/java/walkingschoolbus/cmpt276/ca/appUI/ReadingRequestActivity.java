package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.UserDictionary;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import walkingschoolbus.cmpt276.ca.dataObjects.Permission;
import walkingschoolbus.cmpt276.ca.dataObjects.PermissionStatus;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ReadingRequestActivity extends AppCompatActivity {
    private static Permission ReadingPermission;
    private static User requestUser;
    private static int position;
    private User userManager = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_request);

        ServerManager.connectToServerWithToken(ReadingRequestActivity.this);
        setUpButton();
        initialize();
    }

    private void update(){
        TextView permission_ID = (TextView) findViewById(R.id.ReadingRequest_ID);
        TextView permission_action = (TextView) findViewById(R.id.ReadingRequest_Action);
        TextView permisison_status =(TextView) findViewById(R.id.ReadingRequest_Status);
        TextView request_userId = (TextView)findViewById(R.id.ReadingRequest_requestUserID);
        TextView request_userEmail = (TextView) findViewById(R.id.ReadingRequest_requestUserEmail);
        TextView request_userName = (TextView) findViewById(R.id.ReadingRequest_requestUserName);
        TextView groupId = (TextView) findViewById(R.id.ReadingRequest_GroupID);
        TextView Message = (TextView)findViewById(R.id.ReadingRequest_Message);

        permission_ID.setText("Permission ID: "+ ReadingPermission.getId());
        permission_action.setText("Action: " + ReadingPermission.getAction());
        permisison_status.setText("Status: " + ReadingPermission.getStatus());
        request_userId.setText("Request User ID: "+ requestUser.getId());
        request_userEmail.setText("Request User Email: "+ requestUser.getEmail());
        request_userName.setText("Request User Name: "+ requestUser.getName());
        groupId.setText("Group ID: " + ReadingPermission.getGroupG().getId());
        Message.setText("Message: "+ ReadingPermission.getMessage());
    }
    private void initialize(){
        ProxyBuilder.SimpleCallback<User> callback = returnedUser->respondUser(returnedUser);
        ServerManager.getUserByID(ReadingPermission.getUserA().getId(),callback);
    }
    private void setUpButton(){
        Button approve = (Button) findViewById(R.id.ReadingReaquest_Approve);
        Button reject  = (Button) findViewById(R.id.ReadingRequest_Reject);
        if (ReadingPermission.getStatus()!=PermissionStatus.PENDING){
            approve.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
        }
        else{
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProxyBuilder.SimpleCallback<Permission> callback = returnedPermission->respondPermission(returnedPermission);
                    ServerManager.anwserPermissionRequest(ReadingPermission.getId(),PermissionStatus.APPROVED,callback);
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProxyBuilder.SimpleCallback<Permission> callback = returnedPermssion->respondPermission(returnedPermssion);
                    ServerManager.anwserPermissionRequest(ReadingPermission.getId(),PermissionStatus.DENIED,callback);
                }
            });
        }
    }

    public static Intent makeIntent(Context context, int curPosition ,Permission permission){
        ReadingPermission = permission;
        position = curPosition;
        return new Intent(context,ReadingRequestActivity.class);
    }
    private void respondUser(User user){
        requestUser = user;
        update();
    }
    private void respondPermission(Permission permission){
        ProxyBuilder.SimpleCallback<List<Permission>> callback = returnListPermission->respondPermissionList(returnListPermission);
        ServerManager.getAllPermissionRequestsForUser(userManager.getId(),callback);
    }

    private void respondPermissionList(List<Permission> permissionList){
        userManager.setAllPerssionRequest(permissionList);
        finish();
    }
}
