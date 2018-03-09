package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static walkingschoolbus.cmpt276.ca.appUI.RegisterActivity.PREFS_USER_KEY;
import static walkingschoolbus.cmpt276.ca.appUI.RegisterActivity.USERNAME_KEY;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        usernameByUser();
    }

    private void usernameByUser() {
        SharedPreferences info = getSharedPreferences(PREFS_USER_KEY, MODE_PRIVATE);

        TextView tv = (TextView)findViewById(R.id.pref);

        String username = info.getString(USERNAME_KEY,null);

        tv.setText(username);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }
}

