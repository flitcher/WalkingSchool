package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class LeaderboardActivity extends AppCompatActivity {
    
    List<User> userList = new ArrayList<>();
    ApiInterface proxy;
    Token token;
    Integer[] milestoneStickers = {R.drawable.walk5,R.drawable.walk10,R.drawable.walk15,R.drawable.walk20,
            R.drawable.walk50,R.drawable.walk75,R.drawable.walk100,R.drawable.walk125,R.drawable.walk150,R.drawable.walk200,R.drawable.walk500};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        token = Token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());

        getAllUsersInApp();


    }

    private void populateListview() {
        ArrayAdapter<User> adapter = new LeaderboardActivity.MyListAdapter();
        ListView list = (ListView) findViewById(R.id.leaderboard_listview);
        list.setAdapter(adapter);
    }

    private void getAllUsersInApp() {
        ProxyBuilder.SimpleCallback<List<User>> callback = returnedUsers-> response(returnedUsers);
        ServerManager.getAllUsers(callback);
    }

    private void response(List<User> list){
        userList = list;
        sortAllUsersByDistanceWalk(userList);
        populateListview();
    }

    private void sortAllUsersByDistanceWalk(List<User> users) {
        Comparator<User> userComparator = new Comparator<User>() {
            @Override
            public int compare(User user, User user2) {
                return user2.getTotalPointsEarned() - user.getTotalPointsEarned();
            }
        };
        Collections.sort(users, userComparator);
//        Log.d("HUE", "" + users.get(0).getEmail());
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, LeaderboardActivity.class);
        return intent;
    }

    private class MyListAdapter extends ArrayAdapter<User> {
        public MyListAdapter() {
            super(LeaderboardActivity.this, R.layout.item, userList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item,parent,false);
            }
            final View mView = itemView;
            User currentUser = userList.get(position);
            TextView id = (TextView) mView.findViewById(R.id.Item_ID);
            id.setText(position + 1 + "");
            if (currentUser != null) {
                Call<User> caller = proxy.getUserById(currentUser.getId());
                ProxyBuilder.callProxy(LeaderboardActivity.this, caller, returnedUser->responseUserInfo(returnedUser, mView));
            }
            return mView;
        }
    }

    private void responseUserInfo(User returnedUser, View view) {
        TextView name = (TextView) view.findViewById(R.id.Item_Name);
        name.setText(returnedUser.getName());

        TextView email = (TextView) view.findViewById(R.id.Item_Email);
        email.setText(returnedUser.getEmail());

        TextView distanceWalked = (TextView) view.findViewById(R.id.distanceWalked);
        distanceWalked.setText("" + returnedUser.getTotalPointsEarned());

        int stickerPosition = ProfileActivity.checkImage(returnedUser);
        if (stickerPosition != -1) {
            ImageView imageView = (ImageView) view.findViewById(R.id.Item_Image);
            imageView.setImageResource(milestoneStickers[stickerPosition]);
        }

    }

}
