package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * Monitoring children and corresponding features
 */
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
        ArrayAdapter<User> adapter = new MyListAdapter();
        ListView listview = (ListView) findViewById(R.id.ChildActivity_Listview);
        listview.setAdapter(adapter);
    }
    private class MyListAdapter extends ArrayAdapter<User>{
        public MyListAdapter(){
            super(ChildActivity.this,R.layout.item,userManager.getMonitorsUsers());
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            View itemView = convertView;
            List<User> childList = userManager.getMonitorsUsers();
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item,parent,false);
            }

            User currentUser = childList.get(position);

            TextView name = (TextView) itemView.findViewById(R.id.Item_Name);
            name.setText(currentUser.getName());

            TextView email = (TextView) itemView.findViewById(R.id.Item_Email);
            email.setText(currentUser.getEmail());

            TextView id = (TextView) itemView.findViewById(R.id.Item_ID);
            id.setText(""+currentUser.getId());

            return itemView;
        }
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
    @Override
    protected void onResume() {
        super.onResume();
        populateParentListView();
    }
}
