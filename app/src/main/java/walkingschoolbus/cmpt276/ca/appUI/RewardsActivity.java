package walkingschoolbus.cmpt276.ca.appUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * rewards page
 */
public class RewardsActivity extends AppCompatActivity {

    ListView rewardsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
    }
}
