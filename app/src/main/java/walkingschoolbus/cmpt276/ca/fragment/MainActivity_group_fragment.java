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
import walkingschoolbus.cmpt276.ca.dataObjects.Token;
import walkingschoolbus.cmpt276.ca.dataObjects.WalkingGroups;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * Created by seungdobaek on 2018-03-12.
 */

public class MainActivity_group_fragment extends Fragment {
    List<WalkingGroups> allGroups;
    ApiInterface proxy;
    Token token;
    private static final String TAG = "GroupFrag";
    Context context;
    View view;
    ListView groupList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mainactivity_group_fragment, container, false);

        initiate();

        return view;
    }

    private void showGroupList() {
        ArrayAdapter<WalkingGroups> adapter = new MyListadapter();
        groupList = (ListView) view.findViewById(R.id.GroupFrag_groupListView);
        groupList.setAdapter(adapter);
        clickGroup();
    }

    private void clickGroup() {
//        groupList = (ListView) view.findViewById(R.id.GroupFrag_groupListView);
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WalkingGroups itemSelected = allGroups.get(position);
                Long groupId = itemSelected.getId();
                Intent intent = GroupActivity.makeIntent(getContext(), groupId);
                startActivity(intent);
            }
        });
    }

    private class MyListadapter extends ArrayAdapter<WalkingGroups> {
        public MyListadapter(){
            super(context, R.layout.grouplistlayout, allGroups);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View groupView = convertView;
            if (groupView == null){
                groupView = getLayoutInflater().inflate(R.layout.grouplistlayout, parent, false);
            }
            WalkingGroups curWalkingGroups = allGroups.get(position);
            if (curWalkingGroups != null) {
                TextView groupDescriptoin = (TextView) groupView.findViewById(R.id.GroupLayout_groupDescription);
                TextView StartingPoint = (TextView) groupView.findViewById(R.id.GroupLayout_startingPosition);
                TextView Destination = (TextView) groupView.findViewById(R.id.GroupLayout_Destination);
                if (curWalkingGroups.getGroupDescription()!= null) {
                    Log.i(TAG, ""+curWalkingGroups.getGroupDescription());
                    groupDescriptoin.setText(curWalkingGroups.getGroupDescription());
                }
                if (curWalkingGroups.getRouteLatArray() == null || curWalkingGroups.getRouteLngArray() == null
                        || curWalkingGroups.getRouteLngArray().length < 2 || curWalkingGroups.getRouteLatArray().length <2){
                    StartingPoint.setText("Initial point: "+"Not available");
                    Destination.setText("Destination: "+"Not available");
                }
                else{
                    StartingPoint.setText("Initial point: "+ Double.toString(curWalkingGroups.getRouteLatArray()[0]) + ", " +
                            Double.toString(curWalkingGroups.getRouteLngArray()[0]));
                    Destination.setText("Destination: "+Double.toString(curWalkingGroups.getRouteLatArray()[1]) + ", " +
                            Double.toString(curWalkingGroups.getRouteLngArray()[1]));
                }
            }
            return groupView;
        }
    }

    private void initiate() {
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
        Call<List<WalkingGroups>> caller = proxy.getGroups();
        ProxyBuilder.callProxy(getContext(), caller, returnedGroupList->response(returnedGroupList));
        context = getContext();
    }

    private void response(List<WalkingGroups> returnedWalkingGroup) {
        allGroups = returnedWalkingGroup;
        for (WalkingGroups walkingGroup : allGroups) {
            Log.w(TAG, "    walkingGroup: " + walkingGroup.toString());
        }
        showGroupList();
    }
}
