package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Proxy;
import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ProfileActivity extends AppCompatActivity {
    static final String USERID = "walkingschoolbus.cmpt276.ca.appUI-userid";
    private Long userId;
    private User user;
    ApiInterface proxy;
    User myUser;
    Token token;
    FloatingActionButton editBtn;
    FloatingActionButton rewardsBtn;
    Integer[] milestoneStickers = {R.drawable.walk5,R.drawable.walk10,R.drawable.walk15,R.drawable.walk20,
            R.drawable.walk50,R.drawable.walk75,R.drawable.walk100,R.drawable.walk125,R.drawable.walk150,R.drawable.walk200,R.drawable.walk500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        extract_intent();
        initiate();
    }

    private void setBtn() {
        LinearLayout group = (LinearLayout) findViewById(R.id.ProfileActivity_Group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ProfileGroupActivity.makeIntent(ProfileActivity.this, userId);
                startActivity(intent);
            }
        });
        LinearLayout monitor = (LinearLayout) findViewById(R.id.ProfileActivity_monitor);
        monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ProfileMonitorAcitivty.makeIntent(ProfileActivity.this, userId);
                startActivity(intent);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditUserProfile.makeIntent(ProfileActivity.this, userId);
                startActivity(intent);
            }
        });
        rewardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RewardsActivity.makeIntent(ProfileActivity.this);
                startActivity(intent);
            }
        });

    }

    private void initiate() {
        editBtn = (FloatingActionButton) findViewById(R.id.ProfileActivity_editBtn);
        rewardsBtn = (FloatingActionButton) findViewById(R.id.rewardsBtn);
        myUser = myUser.getInstance();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<User> caller = proxy.getUserById(userId);
        ProxyBuilder.callProxy(ProfileActivity.this, caller, returnedUser -> response(returnedUser));
        setBtn();
    }

    private void response(User returnedUser) {
        user = returnedUser;
        int colour = user.getTotalPointsEarned();
        colour = colour/25;
        RelativeLayout colorLayout = (RelativeLayout) findViewById(R.id.ProfileAcitivty_color);
        switch(colour){
            case 0:
                colorLayout.setBackgroundColor(Color.parseColor("#F44336"));
                break;
            case 1:
                colorLayout.setBackgroundColor(Color.parseColor("#FF9800"));
                break;
            case 2:
                colorLayout.setBackgroundColor(Color.parseColor("#FFEB3B"));
                break;
            case 3:
                colorLayout.setBackgroundColor(Color.parseColor("#4CAF50"));
                break;
            case 4:
                colorLayout.setBackgroundColor(Color.parseColor("#00BCD4"));
                break;
            case 5:
                colorLayout.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            default:
                colorLayout.setBackgroundColor(Color.parseColor("#E040FB"));
                break;

        }
        userSetBtnVisibility();
        setText();
    }

    private void userSetBtnVisibility() {
        editBtn.setVisibility(View.GONE);
        rewardsBtn.setVisibility(View.GONE);
        if (userId.equals(myUser.getId())) {
            editBtn.setVisibility(View.VISIBLE);
            rewardsBtn.setVisibility(View.VISIBLE);
            return;
        }
        List<User> parentList = user.getMonitoredByUsers();
        for (User parentUser: parentList){
            if (parentUser.getId().equals(myUser.getId())){
                editBtn.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    private void setText() {
        ImageView userImage = (ImageView) findViewById(R.id.ProfileAcitivty_image);
        TextView userNameTxt = (TextView) findViewById(R.id.ProfileActivity_name);
        TextView userIDTxt = (TextView) findViewById(R.id.ProfileAcitivty_userID);
        TextView userEmailTxt = (TextView) findViewById(R.id.ProfileActivity_email);
        TextView userBirthdayTxt = (TextView) findViewById(R.id.ProfileActivity_birthday);
        TextView userAddressTxt = (TextView) findViewById(R.id.ProfileAcitivty_address);
        TextView userHomePhoneTxt = (TextView) findViewById(R.id.ProfileActivity_homePhone);
        TextView userCellPhoneTxt = (TextView) findViewById(R.id.ProfileActivity_cellPhone);
        TextView userGradeTxt = (TextView) findViewById(R.id.ProfileActivity_grade);
        TextView userTeacherNameTxt = (TextView) findViewById(R.id.ProfileActivity_teacherName);
        TextView userEmergencyContactTxt = (TextView) findViewById(R.id.ProfileActivity_emergency_contactInfo);
        userNameTxt.setText(user.getName());
        userIDTxt.setText(""+user.getId());
        userEmailTxt.setText("Email: "+user.getEmail());
        int birthMonth = user.getBirthMonth();
        int birthYear = user.getBirthYear();
        String birthday = "";
        if (birthMonth != 0 || birthYear != 0){
            if (birthMonth != 0){
                switch(birthMonth){
                    case 1:
                        birthday = birthday+"January";
                        break;
                    case 2:
                        birthday = birthday+"Feburary";
                        break;
                    case 3:
                        birthday = birthday+"March";
                        break;
                    case 4:
                        birthday = birthday+"April";
                        break;
                    case 5:
                        birthday = birthday+"May";
                        break;
                    case 6:
                        birthday = birthday+"June";
                        break;
                    case 7:
                        birthday = birthday+"July";
                        break;
                    case 8:
                        birthday = birthday+"August";
                        break;
                    case 9:
                        birthday = birthday+"September";
                        break;
                    case 10:
                        birthday = birthday+"October";
                        break;
                    case 11:
                        birthday = birthday+"November";
                        break;
                    case 12:
                        birthday = birthday+"December";
                        break;
                }
            }
            if (birthYear != 0){
                if (!birthday.isEmpty()){
                    birthday = birthday+"/";
                }
                birthday = birthday+birthYear;
            }
            userBirthdayTxt.setText("Birthday: "+birthday);
        } else{
            userBirthdayTxt.setText("Birthday: No information");
        }
        if (user.getAddress() != null){
            userAddressTxt.setText("Address: " + user.getAddress());
        } else{
            userAddressTxt.setText("Address: No information");
        }
        if (user.getHomePhone() != null){
            userHomePhoneTxt.setText("Home Phone: " + user.getHomePhone());
        } else{
            userHomePhoneTxt.setText("Home Phone: No information");
        }
        if (user.getCellPhone() != null){
            userCellPhoneTxt.setText("Cellphone: " + user.getCellPhone());
        } else{
            userCellPhoneTxt.setText("Cellphone: No information");
        }
        if (user.getGrade() != null){
            userGradeTxt.setText("Grade: " + user.getGrade());
        } else{
            userGradeTxt.setText("Grade: No information");
        }
        if (user.getTeacherName() != null){
            userTeacherNameTxt.setText("Teacher: "+user.getTeacherName());
        } else{
            userTeacherNameTxt.setText("Teacher: No information");
        }
        if (user.getEmergencyContactInfo() != null){
            userEmergencyContactTxt.setText("Emergency Contact: "+user.getEmergencyContactInfo());
        } else{
            userEmergencyContactTxt.setText("Emergency Contact: No information");
        }
        int stickerPosition = checkImage(user);
        if (stickerPosition != -1) {
            userImage.setImageResource(milestoneStickers[stickerPosition]);
        }
    }

    public static int checkImage(User mUser){
        int points = mUser.getTotalPointsEarned();
        if (points >= 5){
            if (points >= 10){
                if (points >= 15){
                    if (points >= 20){
                        if (points >= 50){
                            if (points >= 75){
                                if (points >= 100){
                                    if (points >= 125){
                                        if (points >= 150){
                                            if (points >= 200){
                                                if (points >= 500){
                                                    return 10;
                                                }
                                                return 9;
                                            }
                                            return 8;
                                        }
                                        return 7;
                                    }
                                    return 6;
                                }
                                return 5;
                            }
                            return 4;
                        }
                        return 3;
                    }
                    return 2;
                }
                return 1;
            }
            return 0;
        }
        return -1;
    }


    private void extract_intent() {
        Intent intent = getIntent();
        userId = intent.getLongExtra(USERID, -1);
    }

    public static Intent makeIntent(Context context, Long userID){
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(USERID, userID);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initiate();
    }
}
