package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_INFO;


public class MainActivity extends AppCompatActivity {

    public static final String USER_ID = "id";
    private static String TAG = "MainActivity";

    private User userManager = User.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // ServerManager.getProxy(MainActivity.this);
        getUser();
        refreshSharedPreferences();

        setBtn();
    }

    private void getUser(){
        String email = userManager.getEmail();
        String pass = userManager.getPassword();

        Log.i(TAG, ""+email);
        Log.i(TAG, ""+pass);
        //ServerManager.getUserByEmail();
    }


    private void refreshSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID, String.valueOf(userManager.getId()));
        editor.apply();
    }

    private void setBtn(){
        Button parentListBtn = (Button)findViewById(R.id.parentList);
        parentListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ParentActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button childListBtn = (Button)findViewById(R.id.Childlist);
        childListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChildActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button mapBtn = (Button) findViewById(R.id.MainActivity_mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MapsActivity.makeIntent(MainActivity.this);
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
                ServerManager.setDoLogin(false);
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

