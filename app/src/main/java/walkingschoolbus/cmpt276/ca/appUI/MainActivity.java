package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMapBtn();
        setParentListBtn();
        setChildListBtn();

    }

    private void setParentListBtn() {
        Button btn = (Button)findViewById(R.id.parentList);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ParentActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
    private void setChildListBtn() {
        Button btn = (Button)findViewById(R.id.Childlist);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChildActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
    private void setMapBtn(){
        Button mapBtn = (Button) findViewById(R.id.MainActivity_mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MapsActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}

