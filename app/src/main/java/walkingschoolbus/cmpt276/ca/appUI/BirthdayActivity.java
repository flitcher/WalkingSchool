package walkingschoolbus.cmpt276.ca.appUI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class BirthdayActivity extends AppCompatActivity {

    public static final String TAG = "birthdayActivity";
    private EditText displayBirthday;
    private DatePickerDialog.OnDateSetListener dataSetListener;
    private String monthInText;
    private User userManager = User.getInstance();

    private int userYear;
    private int userMonth;
    private String validateBday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        setupLayout();
        setupBirthdayLayout();

    }

    private void setupBirthdayLayout() {
        displayBirthday = (EditText) findViewById(R.id.BirthdayActivity_birthdayEditText);
        displayBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(BirthdayActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dataSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;

                switch (month) {
                    case 1:
                        monthInText = "January";
                        break;
                    case 2:
                        monthInText = "February";
                        break;
                    case 3:
                        monthInText = "March";
                        break;
                    case 4:
                        monthInText = "April";
                        break;
                    case 5:
                        monthInText = "May";
                        break;
                    case 6:
                        monthInText = "June";
                        break;
                    case 7:monthInText = "July";
                        break;
                    case 8:
                        monthInText = "August";
                        break;
                    case 9:
                        monthInText = "September";
                        break;
                    case 10:
                        monthInText = "October";
                        break;
                    case 11:
                        monthInText = "November";
                        break;
                    case 12:
                        monthInText = "December";
                        break;

                }

                Log.d("app", "dd/mm/yyyy " + day + "/" + monthInText + "/" + year);
                String date = day  + " " + monthInText + " " + year;
                displayBirthday.setText(date);
                userMonth = month;
                userYear = year;
            }
        };
    }

    private void setupLayout() {
        Button btn = (Button) findViewById(R.id.BirthdayActivity_continueBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
//                EditText birthday = (EditText) findViewById(R.id.BirthdayActivity_birthdayEditText);
                if(validate()) {
                    editUser();
                    if(userManager.getCellPhone() == null && userManager.getHomePhone() == null) {
                        Intent intent = PhoneActivity.makeIntent(BirthdayActivity.this);
                        startActivity(intent);
                    }
                    else if(userManager.getAddress() == null &&
                            userManager.getEmergencyContactInfo() == null &&
                            userManager.getGrade() == null &&
                            userManager.getTeacherName() == null){
                        Intent intent = ContactInfoActivity.makeIntent(BirthdayActivity.this);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = MainActivity.makeIntent(BirthdayActivity.this);
                        startActivity(intent);
                    }
                }
            }
        });

        TextView skip = (TextView) findViewById(R.id.BirthdayActivity_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PhoneActivity.makeIntent(BirthdayActivity.this);
                startActivity(intent);
            }
        });

    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, BirthdayActivity.class);
        return intent;
    }

    private void initialize() {
        validateBday = displayBirthday.getText().toString();
    }

    private boolean validate() {
        boolean valid = true;
        if(validateBday.isEmpty()) {
            displayBirthday.setError("Please enter your birthday");
            valid = false;
        }
        return valid;
    }

    private void editUser(){
        userManager.setBirthMonth(userMonth);
        userManager.setBirthYear(userYear);
        ProxyBuilder.SimpleCallback<User> callback = returnedUser-> responseEdit(returnedUser);
        ServerManager.editUserProfile(userManager,callback);

    }

    private void responseEdit(User user){
        Log.d(TAG, String.valueOf(user));
        Log.d(TAG, "Birthday entered");
    }

}
