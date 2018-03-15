package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class ListMonitoringGroup extends AppCompatActivity {
    User user;
    List<User> userList;
    ListView listView;
    private static final String GROUPID = "walkingschoolbus.cmpt276.ca.appUI-ListMonitoringGroup-GroupID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_monitoring_group);

        populateListView();

    }

    private void populateListView() {
        user = user.getInstance();
        userList = user.getMonitorsUsers();
        ArrayAdapter<User> adapter = new MyListAdapter();
        listView = (ListView) findViewById(R.id.ListMonitoringGroup_ListView);
        listView.setAdapter(adapter);
        setClick();
    }

    private void setClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                User userBack = userList.get(position);
                Intent intent = new Intent();
                intent.putExtra(GROUPID, userBack.getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public static Long getUserID(Intent data) {
        return data.getLongExtra(GROUPID, -1);
    }

    private class MyListAdapter extends ArrayAdapter<User> {
        public MyListAdapter(){
            super(ListMonitoringGroup.this,R.layout.item,user.getMonitorsUsers());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            List<User> parentList = user.getMonitorsUsers();
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

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, ListMonitoringGroup.class);
        return intent;
    }
}
