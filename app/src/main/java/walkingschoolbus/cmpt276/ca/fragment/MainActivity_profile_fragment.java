package walkingschoolbus.cmpt276.ca.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import walkingschoolbus.cmpt276.ca.appUI.ChildMessage;
import walkingschoolbus.cmpt276.ca.appUI.MainActivity;
import walkingschoolbus.cmpt276.ca.appUI.ProfileActivity;
import walkingschoolbus.cmpt276.ca.appUI.UnreadMessageActivity;
import walkingschoolbus.cmpt276.ca.appUI.readMessageActivity;
import walkingschoolbus.cmpt276.ca.dataObjects.Message;
import walkingschoolbus.cmpt276.ca.dataObjects.ServerManager;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static android.content.Context.MODE_PRIVATE;
import static walkingschoolbus.cmpt276.ca.appUI.LoginActivity.USER_INFO;

/**
 * Created by seungdobaek on 2018-03-12.
 */

public class MainActivity_profile_fragment extends Fragment {
    User myUser;
    private ApiInterface proxy;
    Token token;
    private static final String TAG = "Profile";
    Context context;
    Animation showLayout;
    Animation hideLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainactivity_profile_fragment, container, false);

        myUser = myUser.getInstance();
        token = token.getInstance();
        ServerManager.connectToServerWithToken(getContext());
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        context = getContext();
        setupBtn(view);
        return view;
    }

    private void setupBtn(View view) {
        Button userProfileBtn = (Button) view.findViewById(R.id.ProfileFrag_profile);
        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ProfileActivity.makeIntent(getContext(), myUser.getId());
                startActivity(intent);
            }
        });
        FloatingActionButton logoutBtn = (FloatingActionButton) view.findViewById(R.id.ProfileFrag_logOut);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).logOut();
            }
        });


        showLayout = AnimationUtils.loadAnimation(context, R.anim.show_layout_message);
        hideLayout = AnimationUtils.loadAnimation(context, R.anim.hide_layout_message);

        FloatingActionButton messageInfoBtn = (FloatingActionButton) view.findViewById(R.id.ProfileFrag_MessageInfo);
        LinearLayout readMessageLayout = (LinearLayout) view.findViewById(R.id.ProfileFrag_readMessageLayout);
        LinearLayout unreadMessageLayout = (LinearLayout) view.findViewById(R.id.ProfileFrag_unreadMessageLayout);
        messageInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readMessageLayout.getVisibility() == View.VISIBLE &&
                        unreadMessageLayout.getVisibility() == View.VISIBLE){
                    readMessageLayout.setVisibility(View.GONE);
                    readMessageLayout.startAnimation(hideLayout);
                    unreadMessageLayout.setVisibility(View.GONE);
                    unreadMessageLayout.startAnimation(hideLayout);
                } else{
                    readMessageLayout.setVisibility(View.VISIBLE);
                    readMessageLayout.startAnimation(showLayout);
                    unreadMessageLayout.setVisibility(View.VISIBLE);
                    unreadMessageLayout.startAnimation(showLayout);
                }
            }
        });

        FloatingActionButton readMessageBtn = (FloatingActionButton) view.findViewById(R.id.ProfileFrag_readMessage);
        readMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = readMessageActivity.makeIntent(context);
                startActivity(intent);
            }
        });

        FloatingActionButton unreadMessageBtn = (FloatingActionButton) view.findViewById(R.id.ProfileFrag_unreadMessage);
        unreadMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UnreadMessageActivity.makeIntent(context);
                startActivity(intent);
            }
        });

        Button panicBtn = (Button) view.findViewById(R.id.ProfileFrag_Panic);
        panicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickEmergencyMessage();
            }
        });
        panicBtn.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Intent intent = ChildMessage.makeIntent(getContext());
                startActivity(intent);
                return true;
            }
        });
    }

    private void quickEmergencyMessage(){
        String text = "Warning!!! "+ myUser.getName()+ " need help!! "+"ID: "+myUser.getId()+" Lat: "+ myUser.getLastGpsLocation().getLat()+" Lng: "+ myUser.getLastGpsLocation().getLng();
        for(int i = 0; i < myUser.getMemberOfGroups().size(); i++) {
            ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseCallGroup(returnedMessage);
            ServerManager.callToGroup(myUser.getMemberOfGroups().get(i).getId(),text,callback);
        }
        ProxyBuilder.SimpleCallback<Message> callback = returnedMessage->responseCallGroup(returnedMessage);
        ServerManager.callToParent(myUser.getId(),text,callback);
    }

    //return
    private void response(List<User> returnedUsers) {
        Log.w(TAG, "All Users:");
        for (User user : returnedUsers) {
            Log.w(TAG, "    User: " + user.toString());
        }
    }
    private void responseCallGroup(Message message){
        Log.i("User","send successful!");
    }
}
