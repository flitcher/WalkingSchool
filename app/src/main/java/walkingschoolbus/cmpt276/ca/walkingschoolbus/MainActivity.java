package walkingschoolbus.cmpt276.ca.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static walkingschoolbus.cmpt276.ca.walkingschoolbus.RegisterActivity.PREFS_USER_KEY;
import static walkingschoolbus.cmpt276.ca.walkingschoolbus.RegisterActivity.USERNAME_KEY;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameByUser();
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

