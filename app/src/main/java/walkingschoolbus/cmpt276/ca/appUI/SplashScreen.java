package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_EMAIL;
import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_INFO;
import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_PASSWORD;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "splashscreen";

    User userManager = User.getInstance();

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        email  = sharedPreferences.getString(USER_EMAIL, "");
        password = sharedPreferences.getString(USER_PASSWORD, "");

        //might not need

        Log.d(TAG, "email = " + email );
        if(email != "" && password != "") {
            ServerManager.connectToServerWithoutToken(SplashScreen.this);
            loginSetUp();
            Intent intent = MainActivity.makeIntent(SplashScreen.this);
            startActivity(intent);
            finish();
        } else {
            Intent intent = RegisterActivity.makeIntent(SplashScreen.this);
            startActivity(intent);
            finish();
        }

    }

    private void loginSetUp() {




        userManager.setEmail(email);
        userManager.setPassword(password);

        ServerManager.refreshToken();
        //make call
       ServerManager.Login();
    }

}
