package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.lang.reflect.Proxy;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class EditUserProfile extends AppCompatActivity {

    private static final String USERID = "package walkingschoolbus.cmpt276.ca.appUI-EditUserProfile-userID";

    private long userId;
    private User user;
    ApiInterface proxy;
    Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        extract_intent();
        initiate();
    }

    private void initiate() {
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<User> caller = proxy.getUserById(userId);
        ProxyBuilder.callProxy(EditUserProfile.this, caller, returnedUser->response(returnedUser));
    }

    private void response(User returnedUser) {
        user = returnedUser;
        setText();
    }

    private void setText() {
        EditText userNameTxt = (EditText) findViewById(R.id.EditUserProfile_userName);
        EditText userEmailTxt = (EditText) findViewById(R.id.EditUserProfile_email);
        EditText userBirthMonthTxt = (EditText) findViewById(R.id.EditUserProfile_birthMonth);
        EditText userBirthYearTxt = (EditText) findViewById(R.id.EditUserProfile_birthYear);
        EditText userAddressTxt = (EditText) findViewById(R.id.EditUserProfile_address);
        EditText userHomePhoneTxt = (EditText) findViewById(R.id.EditUserProfile_homePhone);
        EditText userCellPhoneTxt = (EditText) findViewById(R.id.EditUserProfile_cellPhone);
        EditText userGradeTxt = (EditText) findViewById(R.id.EditUserProfile_grade);
        EditText userTeacherNameTxt = (EditText) findViewById(R.id.EditUserProfile_teacherName);
        EditText userEmergencyContactTxt = (EditText) findViewById(R.id.EditUserProfile_emergencyContactInfo);
        userNameTxt.setText(user.getName());
        userEmailTxt.setText(user.getEmail());

        if (user.getBirthMonth() != 0){
            userBirthMonthTxt.setText(user.getBirthMonth());
        }

        if (user.getBirthYear() != 0){
            userBirthYearTxt.setText(user.getBirthYear());
        }

        if (user.getAddress() != null){
            userAddressTxt.setText(user.getAddress());
        }
        if (user.getHomePhone() != null){
            userHomePhoneTxt.setText(user.getHomePhone());
        }
        if (user.getCellPhone() != null){
            userCellPhoneTxt.setText(user.getCellPhone());
        }
        if (user.getGrade() != null){
            userGradeTxt.setText(user.getGrade());
        }
        if (user.getTeacherName() != null){
            userTeacherNameTxt.setText(user.getTeacherName());
        }
        if (user.getEmergencyContactInfo() != null){
            userEmergencyContactTxt.setText(user.getEmergencyContactInfo());
        }
    }


    private void extract_intent() {
        Intent intent = getIntent();
        userId = intent.getLongExtra(USERID, -1);
    }

    public static Intent makeIntent(Context context, long userID){
        Intent intent = new Intent(context, EditUserProfile.class);
        intent.putExtra(USERID, userID);
        return intent;
    }
}
