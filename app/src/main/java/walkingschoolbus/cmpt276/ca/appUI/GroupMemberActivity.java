package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import walkingschoolbus.cmpt276.ca.fragment.GroupMember_leader_fragment;
import walkingschoolbus.cmpt276.ca.fragment.GroupMember_member_fragment;
import walkingschoolbus.cmpt276.ca.fragment.ProfileGroup_leader_fragement;
import walkingschoolbus.cmpt276.ca.fragment.ProfileGroup_member_fragment;
import walkingschoolbus.cmpt276.ca.fragment.SectionsPageAdapter;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

public class GroupMemberActivity extends AppCompatActivity {

    private static String GROUPID = "walkingschoolbus.cmpt276.ca.appUI-GroupMemberActivity-GroupID";
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private Long groupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        extract_intent();

        Toolbar toolbar = findViewById(R.id.GroupMember_toolbar);
        setSupportActionBar(toolbar);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.GroupMember_container);
        setUpViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.GroupMember_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        sectionsPageAdapter.addFragment(new GroupMember_leader_fragment(),"LEADER");
        sectionsPageAdapter.addFragment(new GroupMember_member_fragment(), "MEMBER");
        viewPager.setAdapter(sectionsPageAdapter);
    }

    public Long getGroupID(){
        return groupId;
    }

    private void extract_intent() {
        Intent intent = getIntent();
        groupId = intent.getLongExtra(GROUPID, -1);
        Log.i("ProfileGroupActivity", "userID = "+groupId);
    }

    public static Intent makeIntent(Context context, Long userID){
        Intent intent = new Intent(context, ProfileGroupActivity.class);
        intent.putExtra(GROUPID, userID);
        return intent;
    }
}
