package walkingschoolbus.cmpt276.ca.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import walkingschoolbus.cmpt276.ca.appUI.AddActivity;
import walkingschoolbus.cmpt276.ca.appUI.DeleteActivity;
import walkingschoolbus.cmpt276.ca.appUI.ParentActivity;
import walkingschoolbus.cmpt276.ca.appUI.ProfileActivity;
import walkingschoolbus.cmpt276.ca.appUI.ProfileMonitorAcitivty;
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * Created by seungdobaek on 2018-03-14.
 */

public class ProfileMonitor_parent_fragment extends Fragment {
    View view;
    Context context;
    List<User> parentList;
    ListView listView;
    User user;
    private final String PARENTLIST = "parentList";
    ApiInterface proxy;
    Token token;
    Long userID;
    User myUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profilemonitor_parent_fragment, container, false);

        initiate();
        return view;
    }

    private void initiate() {
        userID = ((ProfileMonitorAcitivty) getActivity()).getUserID();
        context = getContext();
        listView = (ListView) view.findViewById(R.id.ParentFrag_groupListView);
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<User> caller = proxy.getUserById(userID);
        ProxyBuilder.callProxy(context, caller, returnedUser->response(returnedUser));
        setBtnVisibility();
    }

    private void setBtnVisibility() {
        myUser = myUser.getInstance();
        if (!userID.equals(myUser.getId())){
            FloatingActionButton createBtn = (FloatingActionButton) view.findViewById(R.id.ParentFrag_createBtn);
            createBtn.setVisibility(View.GONE);
        }
    }

    private void response(User returnedUser) {
        user = returnedUser;
        parentList = user.getMonitoredByUsers();
        showlist();
    }

    private void showlist() {
        ArrayAdapter<User> adapter = new ProfileMonitor_parent_fragment.MyListAdapter();
        listView.setAdapter(adapter);
        clickUser();
        clickBtn();
        setDeleteLongClickView();
    }

    private void clickBtn() {
        FloatingActionButton createBtn = (FloatingActionButton) view.findViewById(R.id.ParentFrag_createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddActivity.makeIntent(context, PARENTLIST);
                startActivity(intent);
            }
        });
    }

    private void clickUser() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Long userID = parentList.get(position).getId();
                Intent intent = ProfileActivity.makeIntent(context, userID);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<User> {
        public MyListAdapter(){
            super(context,R.layout.item, parentList);
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            View itemView = convertView;
            parentList = user.getMonitoredByUsers();
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item,parent,false);
            }
            final View mView = itemView;
            User currentUser = parentList.get(position);
            if (currentUser != null) {
                Call<User> caller = proxy.getUserById(currentUser.getId());
                ProxyBuilder.callProxy(context, caller, returnedUser->responseUserInfo(returnedUser, mView));
            }
            return itemView;
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

    private void setDeleteLongClickView(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User newUser = user.getOneMonitoredByUserByIndex(i);
                Intent intent = DeleteActivity.makeIntent(context,newUser,PARENTLIST);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initiate();
    }
}
