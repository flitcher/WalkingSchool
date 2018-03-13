package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;


/**
 * Created by Jonathan Chen on 2018/3/6.
 */

public class ParentActivity extends AppCompatActivity {
    User userManager = User.getInstance();
    private final String PARENTLIST = "parentList";
    private final int DELETE_USER = 1;
    private final int ADD_USER = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        setDeleteLongClickView();
        populateParentListView();
        setUpAddBtn();
    }

    private void populateParentListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.item,
                userManager.displayMonitoredByUser());
        ListView listview = (ListView) findViewById(R.id.ParentActivity_Listview);
        listview.setAdapter(adapter);
    }
    private void setUpAddBtn(){
        Button btn = (Button) findViewById(R.id.ParentActivity_addbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddActivity.makeIntent(ParentActivity.this,PARENTLIST );
                startActivityForResult(intent,ADD_USER);
            }
        });
    }
    private void setDeleteLongClickView(){
        ListView listview= (ListView)findViewById(R.id.ParentActivity_Listview);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User newUser = userManager.getOneMonitoredByUserByIndex(i);
                Intent intent = DeleteActivity.makeIntent(ParentActivity.this,newUser,PARENTLIST);
                startActivityForResult(intent,DELETE_USER);
                return true;
            }
        });

    }
    public static Intent makeIntent(Context context){
        return new Intent(context,ParentActivity.class);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DELETE_USER){
            if(resultCode == Activity.RESULT_OK){
                Log.i("test","run1");
                populateParentListView();
            }
        }
        else if (requestCode == ADD_USER){
            if(requestCode ==Activity.RESULT_OK){
                Log.i("test","run2");
                populateParentListView();
            }
        }
    }


}
