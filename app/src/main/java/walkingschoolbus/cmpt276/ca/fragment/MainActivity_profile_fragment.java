package walkingschoolbus.cmpt276.ca.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.appUI.LoginActivity;
import walkingschoolbus.cmpt276.ca.appUI.MainActivity;
import walkingschoolbus.cmpt276.ca.appUI.MessageHomeActivity;
import walkingschoolbus.cmpt276.ca.appUI.ParentActivity;
import walkingschoolbus.cmpt276.ca.appUI.ProfileActivity;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainactivity_profile_fragment, container, false);

        myUser = myUser.getInstance();
        token = token.getInstance();
        proxy = ProxyBuilder.getProxy(getString(R.string.apiKey), token.getToken());
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
        Button logoutBtn = (Button) view.findViewById(R.id.ProfileFrag_logOut);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).logOut();
            }
        });

        Button messageBtn = (Button) view.findViewById(R.id.ProfileFrag_Message);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MessageHomeActivity.makeIntent(getContext());
                startActivity(intent);
            }
        });

    }

    private void response(List<User> returnedUsers) {
        Log.w(TAG, "All Users:");
        for (User user : returnedUsers) {
            Log.w(TAG, "    User: " + user.toString());
        }
    }
}
