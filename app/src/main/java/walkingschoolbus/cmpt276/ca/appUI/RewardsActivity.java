package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * rewards page
 */
public class RewardsActivity extends AppCompatActivity {

    ListView rewardsListView;
    String[] weeksWalked = {"3", "6", "8", "12","16", "24","32", "52"};
    Integer[] imageId = {R.drawable.week3,R.drawable.week6,R.drawable.week8,R.drawable.week12,R.drawable.week16,R.drawable.week24,R.drawable.week32,R.drawable.week52};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        rewardsListView = (ListView) findViewById(R.id.RewardsActivity_listView);
        CustomListView customListView = new CustomListView(this, weeksWalked, imageId);
        rewardsListView.setAdapter(customListView);

    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, RewardsActivity.class);
        return intent;
    }
}
