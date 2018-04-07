package walkingschoolbus.cmpt276.ca.appUI;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * custom list view to show rewards
 */

public class CustomListView extends ArrayAdapter<String> {

    private String[] weeksWalked;
    private Integer[] imageId;
    private Activity context;

    public CustomListView(Activity context, String[] weeksWalked, Integer[] imageId) {
        super(context, R.layout.listview_layout, weeksWalked);

        this.context = context;
        this.weeksWalked = weeksWalked;
        this.imageId = imageId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            r = inflater.inflate(R.layout.listview_layout,null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.rewardImg.setImageResource(imageId[position]);
        viewHolder.descriptionOfReward.setText(weeksWalked[position] + " consecutive weeks walked");

        return r;
    }

    class ViewHolder {
        TextView reward;
        TextView descriptionOfReward;
        ImageView rewardImg;

        public ViewHolder(View view) {
            reward = (TextView) view.findViewById(R.id.reward);
            descriptionOfReward = (TextView) view.findViewById(R.id.rewardsDescription);
            rewardImg = (ImageView) view.findViewById(R.id.rewardsImage);
        }
    }
}
