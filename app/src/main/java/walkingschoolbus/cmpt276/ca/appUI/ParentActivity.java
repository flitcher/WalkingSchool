package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

        populateParentListView();
        setDeleteLongClickView();
        setUpAddBtn();
    }

    private void populateParentListView() {
        ArrayAdapter<User> adapter = new MyListAdapter();
        ListView listview = (ListView) findViewById(R.id.ParentActivity_Listview);
        listview.setAdapter(adapter);
    }
    private class MyListAdapter extends ArrayAdapter<User>{
        public MyListAdapter(){
            super(ParentActivity.this,R.layout.item,userManager.getMonitoredByUsers());
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            View itemView = convertView;
            List<User> parentList = userManager.getMonitoredByUsers();
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item,parent,false);
            }

            User currentUser = parentList.get(position);

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
    protected void onResume() {
        super.onResume();
        populateParentListView();
    }
}
