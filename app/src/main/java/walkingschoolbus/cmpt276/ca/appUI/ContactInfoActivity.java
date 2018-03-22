package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ContactInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        setupLayout();
    }

    private void setupLayout() {
        Button btn = (Button) findViewById(R.id.ContactActivity_continueBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.makeIntent(ContactInfoActivity.this);
                startActivity(intent);
            }
        });

        TextView skip = (TextView) findViewById(R.id.ContactActivity_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.makeIntent(ContactInfoActivity.this);
                startActivity(intent);
            }
        });
    }


    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, ContactInfoActivity.class);
        return intent;
    }
}
