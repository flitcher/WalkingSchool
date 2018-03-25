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

import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ContactInfoActivity extends AppCompatActivity {

    private static final String TAG = "contact" ;

    private EditText address;
    private EditText grade;
    private EditText teacherName;
    private EditText emergencyMsg;

    private String validateAddress;
    private String validateGrade;
    private String validateTeacherName;
    private String validateEmergencyMsg;

    User userManager = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        setupLayout();
    }

    private void setupLayout() {
        address = (EditText) findViewById(R.id.ContactActivity_address);
        grade = (EditText) findViewById(R.id.ContactActivity_grade);
        teacherName = (EditText) findViewById(R.id.ContactActivity_teachername);
        emergencyMsg = (EditText) findViewById(R.id.ContactActivity_emergencymessage);

        Button btn = (Button) findViewById(R.id.ContactActivity_continueBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
                if(validate()) {

                    editUser();
                    Intent intent = MainActivity.makeIntent(ContactInfoActivity.this);
                    startActivity(intent);
                }
            }
        });

        TextView skip = (TextView) findViewById(R.id.ContactActivity_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactInfoActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ContactInfoActivity.this.finish();
            }
        });
    }

    private void initialize() {
        validateAddress = address.getText().toString().trim();
        validateGrade = grade.getText().toString().trim();
        validateTeacherName = teacherName.toString().trim();
        validateEmergencyMsg = emergencyMsg.toString().trim();
    }

    private boolean validate() {
        boolean valid = true;
        if(validateAddress.isEmpty()) {
            address.setError("Please enter your address");
            valid = false;
        }
        if(validateGrade.isEmpty()) {
            grade.setError("Please enter your grade");
            valid = false;
        }
        if(validateTeacherName.isEmpty()) {
            teacherName.setError("Please enter your teacher's name");
            valid = false;
        }
        if(validateEmergencyMsg.isEmpty()) {
            emergencyMsg.setError("Please enter your emergency message");
            valid = false;
        }
        return valid;
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, ContactInfoActivity.class);
        return intent;
    }

    private void editUser(){
        userManager.setAddress(validateAddress);
        userManager.setGrade(validateGrade);
        userManager.setTeacherName(validateTeacherName);
        userManager.setEmergencyContactInfo(validateEmergencyMsg);
        ProxyBuilder.SimpleCallback<User> callback = returnedUser->responseEdit(returnedUser);
        ServerManager.editUserProfile(userManager,callback);

    }

    private void responseEdit(User user){
        Log.d(TAG, String.valueOf(user));
    }
}
