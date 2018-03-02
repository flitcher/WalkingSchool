package walkingschoolbus.cmpt276.ca.walkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import walkingschoolbus.cmpt276.ca.walkingschoolbusLogic.User;

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
    private static final String PASSWORD_KEY = "password";
    private static final String EMAIL_KEY = "email";

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

//    public final static boolean isValidEmail(CharSequence target) {
//        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
//    }


    private void setupRegisterButton() {


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username;
                final String password;
                final String email;

                username = newUsername.getText().toString();
                password = newPassword.getText().toString();
                email = newEmail.getText().toString().trim();

                initialize();
                if(!validate()){
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    User newUser = new User(username, password, email);

                    //Save data onto user phone (remember me)
                    getSharedPreferences(PREFS_USER_KEY,MODE_PRIVATE)
                            .edit()
                            .putString(USERNAME_KEY, username)
                            .putString(PASSWORD_KEY, password)
                            .putString(EMAIL_KEY, email)
                            .apply();

                    //POST new user info to db
                    Intent intent = MainActivity.makeIntent(RegisterActivity.this);
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
        boolean valid = false;
        if(validateUsername.isEmpty() || validateUsername.length() == 0) {
            newUsername.setError("Please enter a valid username");
        }
        if(validatePassword.isEmpty() || validatePassword.length() <= 4) {
            newPassword.setError("Please enter a password with 4 or more characters");
        }
        if(!validateEmail.matches(emailPattern)) {
            newEmail.setError("Please enter a valid email");
        }
        return valid;
    }
}
