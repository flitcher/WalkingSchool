package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.UserManager;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;
import walkingschoolbus.cmpt276.ca.dataObjects.User;

import static walkingschoolbus.cmpt276.ca.dataObjects.User.emailPattern;


public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText newUsername;
    private EditText newPassword;
    private EditText newEmail;

    private String validateUsername;
    private String validatePassword;
    private String validateEmail;

    UserManager userManager = UserManager.getInstance();


    public static final String PREFS_USER_KEY = "userinfo";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String EMAIL_KEY = "email";
//    private static final String API_KEY = "E14DEF58-61CB-4425-B6FB-BDBD807E44CF";
//    public static final String SERVER_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";



    private long userId = 0;
    //Reference: https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Build the server proxy
        ServerManager.connectToServerWithoutToken(RegisterActivity.this);

        setupLayout();
        alreadyLoggedIn();
        setupRegisterButton();
    }

    private void setupLayout() {
        registerBtn = (Button) findViewById(R.id.ActivityRegister_registerButton);
        newUsername = (EditText) findViewById(R.id.ActivityLogin_email);
        newPassword = (EditText) findViewById(R.id.ActivityLogin_password);
        newEmail = (EditText) findViewById(R.id.ActivityRegister_email);
    }

    private void setupRegisterButton() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username;
                final String password;
                final String email;

                initialize();
                if(!validate()){
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    username = validateUsername;
                    password = validatePassword;
                    email = validateEmail;

                    //POST new user info to db

                    userManager.setUserName(username);
                    userManager.setUserEmail(email);
                    userManager.setUserPassword(password);
                    userManager.setUserId(userId);

                    ServerManager.createNewUser(userManager.getUser());

                    Intent intent = LoginActivity.makeIntent(RegisterActivity.this);
//                    Intent intent = MainActivity.makeIntent(RegisterActivity.this);
//                    Intent intent = MapsActivity.makeIntent(RegisterActivity.this);
                    startActivity(intent);
                }
            }
        });
    }




    private void alreadyLoggedIn() {
        final TextView textView = (TextView) findViewById(R.id.already_registered);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.makeIntent(RegisterActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initialize(){
        validateUsername = newUsername.getText().toString();
        validatePassword = newPassword.getText().toString();
        validateEmail = newEmail.getText().toString().trim();
    }

    private boolean validate(){
        boolean valid = true;
        if(validateUsername.isEmpty() || validateUsername.length() == 0) {
            newUsername.setError("Please enter a valid username");
            valid = false;
        }
        if(validatePassword.isEmpty() || validatePassword.length() <= 4) {
            newPassword.setError("Please enter a password with 4 or more characters");
            valid = false;
        }
        if(!validateEmail.matches(emailPattern)) {
            newEmail.setError("Please enter a valid email");
            valid = false;
        }
        return valid;
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }
}
