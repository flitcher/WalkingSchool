package walkingschoolbus.cmpt276.ca.appUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static walkingschoolbus.cmpt276.ca.dataObjects.User.emailPattern;

public class LoginActivity extends AppCompatActivity {



    private EditText userPassword;
    private EditText userEmail;

    private String validatePassword;
    private String validateEmail;


    private User userManager = User.getInstance();


    private static final String TAG = "Proxy";
    public static final String USER_INFO = "userInfo";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_TOKEN ="token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ServerManager.connectToServerWithoutToken(LoginActivity.this);

        //setMainBtn();
        loginSetUp();
        setUpActivityLayout();
        //TODO: save username and password feature
    }
/*
    private void setMainBtn(){
        Button mapBtn = (Button) findViewById(R.id.login_button);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.makeIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }
    */


    private void setUpActivityLayout(){
        userEmail = (EditText) findViewById(R.id.ActivityLogin_email);
        userPassword = (EditText) findViewById(R.id.ActivityLogin_password);
    }

    private void initialize(){
        validateEmail = userEmail.getText().toString().trim();
        validatePassword = userPassword.getText().toString();
    }

    private boolean validate(){
        boolean valid = true;
        if(validatePassword.isEmpty() || validatePassword.length() <= 4) {
            userPassword.setError("Please enter a password with 4 or more characters");
            valid = false;
        }
        if(!validateEmail.matches(emailPattern)) {
            userEmail.setError("Please enter a valid email");
            valid = false;
        }
        return valid;
    }


    private void loginSetUp() {
        Button btn = (Button) findViewById(R.id.LoginActivity_loginButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initialize();

                if(validate()) {

                    ServerManager.refreshToken();
                    ServerManager.Login();


                    userManager.setEmail(validateEmail);
                    userManager.setPassword(validatePassword);
                    if(ServerManager.doLogin()) {


                        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(USER_EMAIL, validateEmail);
                        editor.putString(USER_PASSWORD, validatePassword);
                        editor.apply();

                        Intent intent = MainActivity.makeIntent(LoginActivity.this);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }


    public static Intent makeIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }


}
