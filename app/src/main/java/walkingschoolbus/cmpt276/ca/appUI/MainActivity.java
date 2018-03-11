package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_INFO;


public class MainActivity extends AppCompatActivity {

    private static final String TOKEN = "token";
    public static final String USER_ID = "id";
    private static String TAG = "MainActivity";

    private User user;
    private ApiInterface proxy;
    Token token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUser();
        setBtn();
        setMapBtn();
        setParentListBtn();
        setChildListBtn();

    }

    private void getUser(){
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        user = user.getInstance();
        String email = user.getEmail();
        String pass = user.getPassword();
        Log.i(TAG, ""+email);
        Log.i(TAG, ""+pass);

        Call<User> caller = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(MainActivity.this, caller, returnedUser->response(returnedUser));
    }

    private void response(User returnedUser) {
        String email = returnedUser.getEmail();
        Long id = returnedUser.getId();
        String name = returnedUser.getName();
        String password = returnedUser.getPassword();
        Log.d("app", "email = " + email);
        List<User> MonitoredByUsers = returnedUser.getMonitoredByUsers();
        List<User> MonitorsUsers = returnedUser.getMonitorsUsers();
        List<WalkingGroups> WalkingGroups = returnedUser.getWalkingGroups();
        String Href = returnedUser.getHref();
        user.setEmail(email);
        user.setId(id);
        user.setName(name);

        //i don't getUserByEmail returns you a password field
        //check the return Json in the docs
        user.setPassword(password);
        user.setMonitorsUsers(MonitorsUsers);
        user.setMonitoredByUsers(MonitoredByUsers);
        user.setWalkingGroups(WalkingGroups);
        user.setHref(Href);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID, String.valueOf(id));
        editor.apply();
    }


    private void setBtn(){
    private void setParentListBtn() {
        Button btn = (Button)findViewById(R.id.parentList);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ParentActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
    private void setChildListBtn() {
        Button btn = (Button)findViewById(R.id.Childlist);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChildActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
    private void setMapBtn(){
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

        Button logoutBtn = (Button) findViewById(R.id.MainActivity_logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = LoginActivity.makeIntent(MainActivity.this);
                startActivity(intent);

                finish();
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}

