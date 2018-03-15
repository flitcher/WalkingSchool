package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Proxy;

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
    Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        extract_intent();
        initiate();
        setBtn();
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
    }

    private void initiate() {
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<User> caller = proxy.getUserById(userId);
        ProxyBuilder.callProxy(ProfileActivity.this, caller, returnedUser->response(returnedUser));
    }

    private void response(User returnedUser) {
        user = returnedUser;
        setText();
    }

    private void setText() {
        TextView userNameTxt = (TextView) findViewById(R.id.ProfileActivity_name);
        TextView userIDTxt = (TextView) findViewById(R.id.ProfileAcitivty_userID);
        TextView userEmailTxt = (TextView) findViewById(R.id.ProfileActivity_email);
        userNameTxt.setText(user.getName());
        userIDTxt.setText(""+user.getId());
        userEmailTxt.setText("Email: "+user.getEmail());
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
}
