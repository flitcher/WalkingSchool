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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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

    CircularProgressButton loadingBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ServerManager.connectToServerWithoutToken(LoginActivity.this);

        //setMainBtn();
        login();
        setUpActivityLayout();
    }

    private void setUpActivityLayout(){
        userEmail = (EditText) findViewById(R.id.ActivityLogin_email);
        userPassword = (EditText) findViewById(R.id.ActivityLogin_password);

        final TextView textView = (TextView) findViewById(R.id.LoginActivity_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RegisterActivity.makeIntent(LoginActivity.this);
                startActivity(intent);
                finish();
            }
        });
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

    private void login(){
        loadingBtn = (CircularProgressButton) findViewById(R.id.LoginActivity_loginButton2);
        loadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
                if(validate()) {
                    userManager.setEmail(validateEmail);
                    userManager.setPassword(validatePassword);
                    ServerManager.refreshToken();
                    ProxyBuilder.SimpleCallback<Void> callback = returnedNothing->responseLogin(returnedNothing);
                    ServerManager.Login(callback);
                    @SuppressLint("StaticFieldLeak") AsyncTask<String,String,String> login = new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... params) {
                            try{
                                Thread.sleep(3000);


                                if(ServerManager.doLogin()) {

                                    SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString(USER_EMAIL, validateEmail);
                                    editor.putString(USER_PASSWORD, validatePassword);
                                    editor.apply();

                                }
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }
                            return "done";
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if(ServerManager.doLogin()) {
                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = BirthdayActivity.makeIntent(LoginActivity.this);
                                    startActivity(intent);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Login Unsuccessful. Try again.", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        }
                    };

                    loadingBtn.startAnimation();
                    login.execute();
                }


            }
        });
    }


//    private void loginSetUp() {
//        Button btn = (Button) findViewById(R.id.LoginActivity_loginButton);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                initialize();
//                if(validate()) {
//
//                    ServerManager.refreshToken();
//                    ProxyBuilder.SimpleCallback<Void> callback = returnedNothing->responseLogin(returnedNothing);
//                    ServerManager.Login(callback);
//
//
//                    userManager.setEmail(validateEmail);
//                    userManager.setPassword(validatePassword);
//                    if(ServerManager.doLogin()) {
//
//
//                        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                        editor.putString(USER_EMAIL, validateEmail);
//                        editor.putString(USER_PASSWORD, validatePassword);
//                        editor.apply();
//
//                        Intent intent = MainActivity.makeIntent(LoginActivity.this);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            }
//        });
//    }


    public static Intent makeIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }


    //server returns
    private void responseLogin(Void Nothing){
        ProxyBuilder.SimpleCallback<User> callback = returedUser->responseAutoLogin(returedUser);
        Log.w(TAG, "Server replied to login request (no content was expected).");
        ServerManager.getUserByEmail(callback);
    }

    private  void responseAutoLogin(User user){
        userManager.setUser(user);
        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList->resetParentList(returnedList)   ;
        //in response getParentList it also reset ChildList
        ServerManager.LoginInitilizePartOne(callback);

    }
    private  void resetParentList(List<User> list) {
        userManager.setMonitoredByUsers(list);
        ProxyBuilder.SimpleCallback<List<User>> callback = returnedList->resetChildList(returnedList);
        ServerManager.LoginInitilizePartTwo(callback);
    }

    private  void resetChildList(List<User> list) {
        userManager.setMonitorsUsers(list);

    }

}
