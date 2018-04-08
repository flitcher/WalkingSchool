package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import walkingschoolbus.cmpt276.ca.dataObjects.Permission;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class PermissionRequest extends AppCompatActivity {
    private User UserManager = User.getInstance();
    private static List<Permission> permissionList ;
    private static int clickPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);

        ServerManager.connectToServerWithToken(PermissionRequest.this);
        intialize();
        clickMessage();

    }

    private void intialize(){
        permissionList = UserManager.getAllPerssionRequest();
        updateList();
    }
    private void clickMessage(){
        ListView listView = findViewById(R.id.PermissionRequest_ListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickPosition = position;
                ProxyBuilder.SimpleCallback<Permission> callback = returnedPermission->respondPermission(returnedPermission);
                ServerManager.getPermissionRequestsById(permissionList.get(position).getId(),callback);
            }
        });
    }



    private void updateList(){
        ArrayAdapter<Permission> adapter = new PermissionRequest.MyListAdapter();
        ListView listView = (ListView) findViewById(R.id.PermissionRequest_ListView);
        listView.setAdapter(adapter);
    }


    private class MyListAdapter extends ArrayAdapter<Permission> {
        public MyListAdapter(){
            super(PermissionRequest.this,R.layout.permission_layout,permissionList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View messageView = convertView;
            if(messageView  == null){
                messageView  = getLayoutInflater().inflate(R.layout.permission_layout,parent,false);
            }


            TextView ID = (TextView) messageView .findViewById(R.id.PermissionLayout_ID);
            ID.setText("ID: "+permissionList.get(position).getId());

            TextView action = (TextView) messageView .findViewById(R.id.PermissionLayout_Action);
            action.setText("Action: "+permissionList.get(position).getAction());

            TextView status = (TextView) messageView .findViewById(R.id.PermissionLayout_Status);
            status.setText(""+permissionList.get(position).getStatus());

            return messageView;
        }
    }

    @Override
    protected void onResume() {
        intialize();
        super.onResume();
    }

    private void respondPermission(Permission permission) {
        Intent intent = ReadingRequestActivity.makeIntent(PermissionRequest.this,clickPosition,permission);
        startActivity(intent);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context,PermissionRequest.class);
    }
}
