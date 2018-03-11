package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
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

    private ApiInterface proxy;
    private User user;
    private Token Usertoken;

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
            proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), null);
            loginSetUp();
        } else {
            Intent intent = RegisterActivity.makeIntent(SplashScreen.this);
            startActivity(intent);
        }

    }

    private void loginSetUp() {


        user = user.getInstance();

        user.setEmail(email);
        user.setPassword(password);

        ProxyBuilder.setOnTokenReceiveCallback( token -> onReceiveToken(token));

        //make call
        Call<Void> caller = proxy.login(user);
        ProxyBuilder.callProxy(SplashScreen.this, caller, returnedNothing -> response(returnedNothing));
    }

    private void response(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");
        Intent intent = MainActivity.makeIntent(SplashScreen.this);
        startActivity(intent);
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        Usertoken = Usertoken.getInstance();
        Usertoken.setToken(token);
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token);

    }

}
