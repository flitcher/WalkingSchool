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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.appUI.GroupMemberActivity;
import walkingschoolbus.cmpt276.ca.appUI.ProfileActivity;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * Created by seungdobaek on 2018-03-21.
 */

public class GroupMember_leader_fragment extends Fragment {
    private static final int REQEUST_CODE = 1000;
    Long groupID;
    View view;
    Context context;
    private static String TAG = "groupMemberLeaderFrag";
    ListView groupList;
    ApiInterface proxy;
    Token token;
    User leader;
    long leaderID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.groupmember_leader_fragment, container, false);

        initiate();
        return view;
    }

    private void initiate() {
        groupID = ((GroupMemberActivity) getActivity()).getGroupID();
        Log.i(TAG, "groupID = "+groupID);
        context = getContext();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<WalkingGroups> caller = proxy.getOneGroup(groupID);
        ProxyBuilder.callProxy(context, caller, returnedGroup->responseGroup(returnedGroup));
    }

    private void responseGroup(WalkingGroups returnedGroup) {
        leader = returnedGroup.getLeader();
        Call<User> caller = proxy.getUserById(leader.getId());
        ProxyBuilder.callProxy(context, caller, returnedUser->responseUser(returnedUser));
    }

    private void responseUser(User returnedUser) {
        TextView leaderNameText = (TextView) view.findViewById(R.id.GroupMember_Leader_Name);
        TextView leaderEmailText = (TextView) view.findViewById(R.id.GroupMember_Leader_Email);
        TextView leaderIDText =  (TextView) view.findViewById(R.id.GroupMember_Leader_ID);

        leaderNameText.setText(returnedUser.getName());
        leaderEmailText.setText(returnedUser.getEmail());
        leaderID = returnedUser.getId();
        leaderIDText.setText(""+leaderID);

        setClick();
    }

    private void setClick() {
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.GroupMember_Leader_Layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ProfileActivity.makeIntent(context, leaderID);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        initiate();
    }
}
