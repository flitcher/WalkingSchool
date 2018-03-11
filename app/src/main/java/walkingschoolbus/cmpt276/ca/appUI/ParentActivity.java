package walkingschoolbus.cmpt276.ca.walkingschoolbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.model.User;

/**
 * Created by Jonathan Chen on 2018/3/6.
 */

public class ParentActivity extends AppCompatActivity {
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        populateParentListView();
        setUpAddBtn();
    }

    private void populateParentListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.parentlist,user.getParentList());
        ListView listview = (ListView) findViewById(R.id.Listview);
        listview.setAdapter(adapter);
    }
    private void setUpAddBtn(){
        Button btn = (Button) findViewById(R.id.addbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddActivity.makeIntent(ParentActivity.this);
                startActivity(intent);
            }
        });
    }
    private void setDeleteLongClickView(){
        ListView listview= (ListView)findViewById(R.id.Listview);

    }
}
