package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class UnreadMessageActivity extends AppCompatActivity {
    User userManager = User.getInstance();
    List<Message> unreadMessage = userManager.getUnreadMessages();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unread_message);

    }

    private class MyListAdapter extends ArrayAdapter<Message> {
        public MyListAdapter(){
            super(UnreadMessageActivity.this,R.layout.message_layout, unreadMessage);
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            View messageView = convertView;
            if(messageView  == null){
                messageView  = getLayoutInflater().inflate(R.layout.message_layout,parent,false);
            }


            TextView ID = (TextView) messageView .findViewById(R.id.message_layout_ID);
            ID.setText(""+ unreadMessage.get(position).getId());

            TextView message = (TextView) messageView .findViewById(R.id.message_layour_Mesage);
            message.setText(""+unreadMessage.get(position).getShortMessage());
            
            return messageView;
        }
    }
    public static Intent makeIntent(Context context){
        return new Intent(context,UnreadMessageActivity.class);
    }
}
