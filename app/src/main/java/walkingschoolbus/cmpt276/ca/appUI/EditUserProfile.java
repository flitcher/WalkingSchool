package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Proxy;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * allows for user to edit their info
 */
public class EditUserProfile extends AppCompatActivity {

    private static final String USERID = "package walkingschoolbus.cmpt276.ca.appUI-EditUserProfile-userID";

    private long userId;
    private User user;
    ApiInterface proxy;
    User myUser;
    Token token;
    EditText userNameTxt;
    EditText userEmailTxt;
    EditText userBirthMonthTxt;
    EditText userBirthYearTxt;
    EditText userAddressTxt;
    EditText userHomePhoneTxt;
    EditText userCellPhoneTxt;
    EditText userGradeTxt;
    EditText userTeacherNameTxt;
    EditText userEmergencyContactTxt;
    EditText userPasswordTxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        extract_intent();
        initiate();
    }

    private void initiate() {
        initiateEditText();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<User> caller = proxy.getUserById(userId);
        ProxyBuilder.callProxy(EditUserProfile.this, caller, returnedUser->response(returnedUser));
    }

    private void initiateEditText() {
        userNameTxt = (EditText) findViewById(R.id.EditUserProfile_userName);
        userEmailTxt = (EditText) findViewById(R.id.EditUserProfile_email);
        userBirthMonthTxt = (EditText) findViewById(R.id.EditUserProfile_birthMonth);
        userBirthYearTxt = (EditText) findViewById(R.id.EditUserProfile_birthYear);
        userAddressTxt = (EditText) findViewById(R.id.EditUserProfile_address);
        userHomePhoneTxt = (EditText) findViewById(R.id.EditUserProfile_homePhone);
        userCellPhoneTxt = (EditText) findViewById(R.id.EditUserProfile_cellPhone);
        userGradeTxt = (EditText) findViewById(R.id.EditUserProfile_grade);
        userTeacherNameTxt = (EditText) findViewById(R.id.EditUserProfile_teacherName);
        userEmergencyContactTxt = (EditText) findViewById(R.id.EditUserProfile_emergencyContactInfo);
        userPasswordTxt = (EditText) findViewById(R.id.EditUserProfile_password);
    }

    private void response(User returnedUser) {
        user = returnedUser;
        setText();
        setBtn();
    }

    private void setBtn() {
        FloatingActionButton okBtn = (FloatingActionButton) findViewById(R.id.EditUserProfile_okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameTxt.getText().toString();
                String userEmail = userEmailTxt.getText().toString().trim();
                String userPassword = userPasswordTxt.getText().toString();
                String userBirthMonthString = userBirthMonthTxt.getText().toString();
                String userBirthYearString = userBirthYearTxt.getText().toString();
                String userAddress = userAddressTxt.getText().toString();
                String userHomePhone = userHomePhoneTxt.getText().toString();
                String userCellPhone = userCellPhoneTxt.getText().toString();
                String userGrade = userGradeTxt.getText().toString();
                String userTeacherName = userTeacherNameTxt.getText().toString();
                String userEmergencyContact = userEmergencyContactTxt.getText().toString();

                if (!userPassword.isEmpty() && userPassword.length() <= 4){
                    Toast.makeText(EditUserProfile.this, "Your password must have length bigger than 4",
                            Toast.LENGTH_LONG);
                } else if(userEmail.isEmpty()){
                    Toast.makeText(EditUserProfile.this, "Your email should not be empty", Toast.LENGTH_LONG).show();
                } else if(userName.isEmpty()){
                    Toast.makeText(EditUserProfile.this, "Your username should not be empty",Toast.LENGTH_LONG).show();
                } else if(!userBirthMonthString.isEmpty() && Integer.parseInt(userBirthMonthString) < 0){
                    Toast.makeText(EditUserProfile.this, "Your Birth Month is smaller than 0", Toast.LENGTH_LONG).show();
                } else if(!userBirthMonthString.isEmpty() && Integer.parseInt(userBirthMonthString) >  12){
                    Toast.makeText(EditUserProfile.this, "Your Birth Month is bigger than 12", Toast.LENGTH_LONG).show();
                } else{
                    user.setName(userName);
                    user.setEmail(userEmail);
                    if (!userPassword.isEmpty()){
                        user.setPassword(userPassword);
                    }
                    if (!userBirthMonthString.isEmpty()){
                        int userBirthMonth = Integer.parseInt(userBirthMonthString);
                        user.setBirthMonth(userBirthMonth);
                    } else{
                        user.setBirthMonth(0);
                    }
                    if (!userBirthYearString.isEmpty()){
                        int userBirthYear = Integer.parseInt(userBirthYearString);
                        user.setBirthYear(userBirthYear);
                    } else{
                        user.setBirthYear(0);
                    }
                    if (!userAddress.isEmpty()){
                        user.setAddress(userAddress);
                    } else{
                        user.setAddress(null);
                    }
                    if (!userHomePhone.isEmpty()){
                        user.setHomePhone(userHomePhone);
                    } else{
                        user.setHomePhone(null);
                    }
                    if (!userCellPhone.isEmpty()){
                        user.setCellPhone(userCellPhone);
                    } else{
                        user.setCellPhone(null);
                    }
                    if (!userGrade.isEmpty()){
                        user.setGrade(userGrade);
                    } else{
                        user.setGrade(null);
                    }
                    if (!userTeacherName.isEmpty()){
                        user.setTeacherName(userTeacherName);
                    } else{
                        user.setTeacherName(null);
                    }
                    if (!userEmergencyContact.isEmpty()){
                        user.setEmergencyContactInfo(userEmergencyContact);
                    } else{
                        user.setEmergencyContactInfo(null);
                    }
                    Call<User> caller = proxy.editUser(user.getId(), user);
                    ProxyBuilder.callProxy(EditUserProfile.this, caller, returnedUser -> responseEdit(returnedUser));
                }
            }
        });
    }

    private void responseEdit(User returnedUser) {
        myUser = myUser.getInstance();
        if (returnedUser.getId() == myUser.getId()){
            myUser.setUser(returnedUser);
        }
        finish();
    }

    private void setText() {
        userNameTxt.setText(user.getName());
        userEmailTxt.setText(user.getEmail());

        if (user.getBirthMonth() != 0){
            userBirthMonthTxt.setText(""+user.getBirthMonth());
        }

        if (user.getBirthYear() != 0){
            userBirthYearTxt.setText(""+user.getBirthYear());
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
