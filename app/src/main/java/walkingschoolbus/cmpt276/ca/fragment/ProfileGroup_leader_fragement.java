package walkingschoolbus.cmpt276.ca.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Proxy;
import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.appUI.AditGroup;
import walkingschoolbus.cmpt276.ca.appUI.GroupActivity;
import walkingschoolbus.cmpt276.ca.appUI.ProfileGroupActivity;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by seungdobaek on 2018-03-13.
 */

public class ProfileGroup_leader_fragement extends Fragment {
    private static final int REQEUST_CODE = 1000;
    Long userID;
    View view;
    Context context;
    private static String TAG = "ProfileGroupLeaderFrag";
    ListView groupList;
    ApiInterface proxy;
    Token token;
    User myUser;
    List<WalkingGroups> leaderGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profilegroup_leader_fragment, container, false);

        initiate();
        return view;
    }

    private void initiate() {
        userID = ((ProfileGroupActivity) getActivity()).getUserID();
        myUser = myUser.getInstance();
        Log.i(TAG, "userID = "+userID);
        context = getContext();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<User> caller = proxy.getUserById(userID);
        ProxyBuilder.callProxy(context, caller, returnedUser->resonseUser(returnedUser));
        setBtnVisibility();
    }

    private void setBtnVisibility() {
        if (!userID.equals(myUser.getId())){
            FloatingActionButton createGroup = (FloatingActionButton) view.findViewById(R.id.LeaderFrag_createBtn);
            createGroup.setVisibility(View.GONE);
        }
    }

    private void resonseUser(User returnedUser) {
        leaderGroup = returnedUser.getLeadsGroups();
        showGroupList();
    }

    private void showGroupList() {
        ArrayAdapter<WalkingGroups> adapter = new ProfileGroup_leader_fragement.MyListadapter();
        groupList = (ListView) view.findViewById(R.id.LeaderFrag_groupListView);
        groupList.setAdapter(adapter);
        clickGroup();
        clickBtn();
    }

    private void clickBtn() {
        FloatingActionButton createGroup = (FloatingActionButton) view.findViewById(R.id.LeaderFrag_createBtn);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AditGroup.makeIntent(context, (long) -1);
                startActivityForResult(intent, REQEUST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQEUST_CODE){
            if (resultCode == RESULT_OK){
                initiate();
            }
        }
    }

    private void clickGroup() {
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WalkingGroups itemSelected = leaderGroup.get(position);
                Long groupId = itemSelected.getId();
                Intent intent;
                Long myUserID = myUser.getId();
                if (myUserID.equals(userID)) {
                    intent = GroupActivity.makeIntent(context, groupId, 6000);
                } else{
                    intent = GroupActivity.makeIntent(context, groupId, 5000);
                }
                startActivity(intent);
            }
        });
    }

    private class MyListadapter extends ArrayAdapter<WalkingGroups> {
        public MyListadapter(){
            super(context, R.layout.grouplistlayout, leaderGroup);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View groupView = convertView;
            if (groupView == null){
                groupView = getLayoutInflater().inflate(R.layout.grouplistlayout, parent, false);
            }
            final View mView = groupView;
            WalkingGroups curWalkingGroups = leaderGroup.get(position);
            if (curWalkingGroups != null) {
                Call<WalkingGroups> caller = proxy.getOneGroup(curWalkingGroups.getId());
                ProxyBuilder.callProxy(context, caller, returnedGroup->responseGroup(returnedGroup, mView));
            }
            return mView;
        }
    }

    private void responseGroup(WalkingGroups returnedGroup, View view) {
        TextView groupDescriptoin = (TextView) view.findViewById(R.id.GroupLayout_groupDescription);
        TextView StartingPoint = (TextView) view.findViewById(R.id.GroupLayout_startingPosition);
        TextView Destination = (TextView) view.findViewById(R.id.GroupLayout_Destination);
        if (returnedGroup.getGroupDescription()!= null) {
            Log.i(TAG, ""+returnedGroup.getGroupDescription());
            groupDescriptoin.setText(returnedGroup.getGroupDescription());
        }
        if (returnedGroup.getRouteLatArray() == null || returnedGroup.getRouteLngArray() == null
                || returnedGroup.getRouteLngArray().length < 2 || returnedGroup.getRouteLatArray().length <2){
            StartingPoint.setText("Initial point: "+"Not available");
            Destination.setText("Destination: "+"Not available");
        }
        else{
            StartingPoint.setText("Initial point: "+ Double.toString(returnedGroup.getRouteLatArray()[0]) + ", " +
                    Double.toString(returnedGroup.getRouteLngArray()[0]));
            Destination.setText("Destination: "+Double.toString(returnedGroup.getRouteLatArray()[1]) + ", " +
                    Double.toString(returnedGroup.getRouteLngArray()[1]));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initiate();
    }
}
