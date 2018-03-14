package walkingschoolbus.cmpt276.ca.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.appUI.GroupActivity;
import walkingschoolbus.cmpt276.ca.appUI.ProfileGroupActivity;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * Created by seungdobaek on 2018-03-13.
 */

public class ProfileGroup_member_fragment extends Fragment {
    Long userID;
    View view;
    Context context;
    private static String TAG = "ProfileGroupLeaderFrag";
    ListView groupList;
    ApiInterface proxy;
    Token token;
    List<WalkingGroups> memberGroup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profilegroup_member_fragment, container, false);

        initiate();
        return view;    }

    private void initiate() {
        userID = ((ProfileGroupActivity) getActivity()).getUserID();
        Log.i(TAG, "userID = "+userID);
        context = getContext();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<User> caller = proxy.getUserById(userID);
        ProxyBuilder.callProxy(context, caller, returnedUser->resonseUser(returnedUser));
    }

    private void resonseUser(User returnedUser) {
        memberGroup = returnedUser.getMemberOfGroups();
        showGroupList();
    }

    private void showGroupList() {
        ArrayAdapter<WalkingGroups> adapter = new ProfileGroup_member_fragment.MyListadapter();
        groupList = (ListView) view.findViewById(R.id.MemberFrag_groupListView);
        groupList.setAdapter(adapter);
        clickGroup();
    }

    private void clickGroup() {
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WalkingGroups itemSelected = memberGroup.get(position);
                Long groupId = itemSelected.getId();
                Intent intent = GroupActivity.makeIntent(context, groupId);
                startActivity(intent);
            }
        });
    }

    private class MyListadapter extends ArrayAdapter<WalkingGroups> {
        public MyListadapter(){
            super(context, R.layout.grouplistlayout, memberGroup);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View groupView = convertView;
            if (groupView == null){
                groupView = getLayoutInflater().inflate(R.layout.grouplistlayout, parent, false);
            }
            final View mView = groupView;
            WalkingGroups curWalkingGroups = memberGroup.get(position);
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
}
