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

/**
 * collect user home phone and cell number
 */
public class PhoneActivity extends AppCompatActivity {

    public static final String TAG = "phoneactivity";
    public static final String HOME_EXTENSION = "(604) ";
    public static final String CELL_EXTENSION = "+1 ";

    User userManager = User.getInstance();

    String validateCellNum;
    String validateHomeNum;

    EditText cellNum;
    EditText homeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        setupLayout();
    }

    private void setupLayout() {
        cellNum = (EditText) findViewById(R.id.PhoneActivity_cellNumber);
        homeNum = (EditText) findViewById(R.id.PhoneActivity_homeNumber);

        Button btn = (Button) findViewById(R.id.PhoneActivity_continueBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
                if(validate()) {

                    editUser();

                    if(userManager.getAddress() == null &&
                            userManager.getEmergencyContactInfo() == null &&
                            userManager.getGrade() == null &&
                            userManager.getTeacherName() == null){
                        Intent intent = ContactInfoActivity.makeIntent(PhoneActivity.this);
                        startActivity(intent);
                    } else {
                        Intent intent = MainActivity.makeIntent(PhoneActivity.this);
                        startActivity(intent);
                    }
                }
            }
        });

        TextView skip = (TextView) findViewById(R.id.PhoneActivity_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ContactInfoActivity.makeIntent(PhoneActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, PhoneActivity.class);
        return intent;
    }

    private void initialize() {
        validateCellNum = cellNum.getText().toString();
        validateHomeNum = homeNum.getText().toString();
    }

    private boolean validate() {
        boolean valid = true;
        if(validateHomeNum.isEmpty()) {
            homeNum.setError("Please enter a valid home phone number");
            valid = false;
        }
        if(validateCellNum.isEmpty()) {
            cellNum.setError("Please enter a valid cellphone number");
            valid = false;
        }
        return valid;
    }

    private void editUser(){
        userManager.setCellPhone(CELL_EXTENSION + cellNum.getText().toString());
        userManager.setHomePhone(HOME_EXTENSION + homeNum.getText().toString());
        ProxyBuilder.SimpleCallback<User> callback = returnedUser->responseEdit(returnedUser);
        ServerManager.editUserProfile(userManager,callback);

    }

    private void responseEdit(User user){
        Log.d(TAG, String.valueOf(user));
    }
}
