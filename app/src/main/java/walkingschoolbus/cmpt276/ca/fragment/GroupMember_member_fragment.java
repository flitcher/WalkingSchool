package walkingschoolbus.cmpt276.ca.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class GroupMember_member_fragment extends Fragment {
    Long groupID;
    View view;
    Context context;
    private static String TAG = "groupMemberMemberFrag";
    ListView groupList;
    ApiInterface proxy;
    Token token;
    List<User> memberList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.groupmember_member_fragment, container, false);
        initiate();

        return view;
    }

    private void initiate() {
        groupID = ((GroupMemberActivity) getActivity()).getGroupID();
        context = getContext();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        groupList = (ListView) view.findViewById(R.id.GroupMemberFrag_groupListView);
        Call<List<User>> caller = proxy.getGroupMembers(groupID);
        ProxyBuilder.callProxy(context, caller, returnedUser->responseUser(returnedUser));
    }

    private void responseUser(List<User> returnedUser) {
        memberList = returnedUser;
        if (memberList != null) {
            showList();
        }
    }

    private void showList() {
        ArrayAdapter<User> adapter = new GroupMember_member_fragment.MyListAdapter();
        groupList.setAdapter(adapter);
        clickUser();
    }

    private class MyListAdapter extends ArrayAdapter<User> {
        public MyListAdapter(){
            super(context,R.layout.item, memberList);
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup member) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item,member,false);
            }
            final View mView = itemView;
            User currentUser = memberList.get(position);
            if (currentUser != null) {
                Call<User> caller = proxy.getUserById(currentUser.getId());
                ProxyBuilder.callProxy(context, caller, returnedUser->responseUserInfo(returnedUser, mView));
            }
            return mView;
        }
    }

    private void responseUserInfo(User returnedUser, View view) {
        TextView name = (TextView) view.findViewById(R.id.Item_Name);
        name.setText(returnedUser.getName());

        TextView email = (TextView) view.findViewById(R.id.Item_Email);
        email.setText(returnedUser.getEmail());

        TextView id = (TextView) view.findViewById(R.id.Item_ID);
        id.setText("" + returnedUser.getId());
    }

    private void clickUser() {
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Long userID = memberList.get(position).getId();
                Intent intent = ProfileActivity.makeIntent(context, userID);
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
