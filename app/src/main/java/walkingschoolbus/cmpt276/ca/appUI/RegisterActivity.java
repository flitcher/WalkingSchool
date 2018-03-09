package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import walkingschoolbus.cmpt276.ca.walkingschoolbus.MapsActivity;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;
import walkingschoolbus.cmpt276.ca.dataObjects.User;


public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText newUsername;
    private EditText newPassword;
    private EditText newEmail;

    private String validateUsername;
    private String validatePassword;
    private String validateEmail;

    public static final String PREFS_USER_KEY = "userinfo";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String EMAIL_KEY = "email";
    private static final String API_KEY = "E14DEF58-61CB-4425-B6FB-BDBD807E44CF";
    public static final String SERVER_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";



    //Reference: https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupLayout();
        alreadyLoggedIn();
        setupRegisterButton();
    }

    private void setupLayout() {
        registerBtn = (Button) findViewById(R.id.ActivityRegister_registerButton);
        newUsername = (EditText) findViewById(R.id.ActivityRegister_username);
        newPassword = (EditText) findViewById(R.id.ActivityRegister_password);
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

                    //Save data onto user phone (remember me)
                    getSharedPreferences(PREFS_USER_KEY,MODE_PRIVATE)
                            .edit()
                            .putString(USERNAME_KEY, username)
                            .putString(PASSWORD_KEY, password)
                            .putString(EMAIL_KEY, email)
                            .apply();

                    //POST new user info to db
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    Intent intent = MapsActivity.makeIntent(RegisterActivity.this);
                    startActivity(intent);
                    finish();

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

    public boolean validate(){
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
}
