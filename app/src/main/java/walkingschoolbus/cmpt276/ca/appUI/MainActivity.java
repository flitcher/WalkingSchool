package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Api;

import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class MainActivity extends AppCompatActivity {
    private User user;
    private static String TAG = "MainActivity";
    private ApiInterface proxy;
    Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUser();
        setBtn();
    }

    private void getUser(){
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        user = user.getInstance();
        String email = user.getEmail();
        Log.i(TAG, ""+email);
        Call<User> caller = proxy.getUserByEmail("testuser@sfu.ca");
        ProxyBuilder.callProxy(MainActivity.this, caller, returnedUser->response(returnedUser));
    }

    private void response(User returnedUser) {
        String email = returnedUser.getEmail();
        Long id = returnedUser.getId();
        String name = returnedUser.getName();
        String password = returnedUser.getPassword();
        List<User> MonitoredByUsers = returnedUser.getMonitoredByUsers();
        List<User> MonitorsUsers = returnedUser.getMonitorsUsers();
        List<WalkingGroups> WalkingGroups = returnedUser.getWalkingGroups();
        String Href = returnedUser.getHref();
        user.setEmail(email);
        user.setId(id);
        user.setName(name);
        user.setPassword(password);
        user.setMonitorsUsers(MonitorsUsers);
        user.setMonitoredByUsers(MonitoredByUsers);
        user.setWalkingGroups(WalkingGroups);
        user.setHref(Href);
        Log.i(TAG, user.toString());
    }


    private void setBtn(){
        Button mapBtn = (Button) findViewById(R.id.MainActivity_mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MapsActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        Button groupBtn = (Button) findViewById(R.id.MainActivity_groupBtn);
        groupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = GroupActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}

