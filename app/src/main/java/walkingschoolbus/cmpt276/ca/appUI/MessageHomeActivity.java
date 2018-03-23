package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class MessageHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_home);

        setUpBtn();
    }

    private void setUpBtn() {
        Button unreadbtn = (Button)findViewById(R.id.messageHome_unread);
        unreadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UnreadMessageActivity.makeIntent(MessageHomeActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MessageHomeActivity.class);
    }
}
