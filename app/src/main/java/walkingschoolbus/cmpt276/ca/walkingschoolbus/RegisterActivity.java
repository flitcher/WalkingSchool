package walkingschoolbus.cmpt276.ca.walkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        alreadyLoggedIn();
    }

    private void alreadyLoggedIn() {
        final TextView textView = (TextView) findViewById(R.id.already_registered);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.makeIntent(RegisterActivity.this);
                startActivity(intent);
            }
        });
    }
}
