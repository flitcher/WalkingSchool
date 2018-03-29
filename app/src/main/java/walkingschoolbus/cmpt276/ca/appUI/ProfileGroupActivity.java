package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import walkingschoolbus.cmpt276.ca.fragment.ProfileGroup_leader_fragement;
import walkingschoolbus.cmpt276.ca.fragment.ProfileGroup_member_fragment;
import walkingschoolbus.cmpt276.ca.fragment.SectionsPageAdapter;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * display group's profile such as members, start and end locations
 */
public class ProfileGroupActivity extends AppCompatActivity {

    private static String USERID = "package walkingschoolbus.cmpt276.ca.appUI-ProfileGroupActivity-USERID";
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_group);

        extract_intent();

        Toolbar toolbar = findViewById(R.id.ProfileGroup_toolbar);
        setSupportActionBar(toolbar);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.ProfileGroup_container);
        setUpViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.ProfileGroup_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setUpViewPager(ViewPager viewPager) {
        sectionsPageAdapter.addFragment(new ProfileGroup_leader_fragement(),"LEADER");
        sectionsPageAdapter.addFragment(new ProfileGroup_member_fragment(), "MEMBER");
        viewPager.setAdapter(sectionsPageAdapter);
    }

    public Long getUserID(){
        return userId;
    }

    private void extract_intent() {
        Intent intent = getIntent();
        userId = intent.getLongExtra(USERID, -1);
        Log.i("ProfileGroupActivity", "userID = "+userId);
    }

    public static Intent makeIntent(Context context, Long userID){
        Intent intent = new Intent(context, ProfileGroupActivity.class);
        intent.putExtra(USERID, userID);
        return intent;
    }
}
