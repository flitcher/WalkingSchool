package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * Created by Kawai on 4/8/2018.
 */

public class LeaderboardListView extends ArrayAdapter<String>{

    private String[] leaderboardPlacement;
    private String[] leaderboardNames;
    private Activity context;

    public LeaderboardListView(Activity context, String[] leaderboardPlacement, String[] leaderboardNames) {
        super(context, R.layout.leaderboard_listview, leaderboardNames);

        this.context = context;
        this.leaderboardNames = leaderboardNames;
        this.leaderboardPlacement = leaderboardPlacement;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            r = inflater.inflate(R.layout.leaderboard_listview,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.leaderboardPlace.setText(leaderboardPlacement[position]);
        viewHolder.leaderboardName.setText(leaderboardNames[position]);

        return r;
    }

    class ViewHolder {
        TextView leaderboardName;
        TextView leaderboardPlace;

        public ViewHolder(View view) {
            leaderboardName = (TextView) view.findViewById(R.id.leaderboard_name);
            leaderboardPlace = (TextView) view.findViewById(R.id.leaderboard_place);
        }
    }
}
