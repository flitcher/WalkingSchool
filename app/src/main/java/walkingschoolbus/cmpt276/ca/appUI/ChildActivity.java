package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ChildActivity extends AppCompatActivity {

    User userManager = User.getInstance();
    private final String CHILDLIST = "childList";;
    private final int DELETE_USER = 5;
    private final int ADD_USER = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        setDeleteLongClickView();
        populateParentListView();
        setUpAddBtn();
    }

    private void populateParentListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.item,
                userManager.displayMonitorUser());
        ListView listview = (ListView) findViewById(R.id.ChildActivity_Listview);
        listview.setAdapter(adapter);
    }
    private void setUpAddBtn(){
        Button btn = (Button) findViewById(R.id.ChildActivity_addbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddActivity.makeIntent(ChildActivity.this,CHILDLIST );
                startActivityForResult(intent,ADD_USER);
            }
        });
    }
    private void setDeleteLongClickView(){
        ListView listview= (ListView)findViewById(R.id.ChildActivity_Listview);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User newUser = userManager.getOneMonitorUserByIndex(i);
                Intent intent = DeleteActivity.makeIntent(ChildActivity.this,newUser,CHILDLIST);
                startActivityForResult(intent,DELETE_USER);
                return true;
            }
        });

    }
    public static Intent makeIntent(Context context){
        return new Intent(context,ChildActivity.class);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DELETE_USER){
            if(resultCode == Activity.RESULT_OK){
                populateParentListView();
            }
        }
        else if (requestCode == ADD_USER){
            if(requestCode ==Activity.RESULT_OK){
                populateParentListView();
            }
        }
    }
}
