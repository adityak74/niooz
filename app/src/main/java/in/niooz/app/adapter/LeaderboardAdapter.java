package in.niooz.app.adapter;

/**
 * Created by aditya on 4/21/15.
 */
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import in.niooz.app.R;
import in.niooz.app.app.AppController;
import in.niooz.app.model.LeaderboardUser;

public class LeaderboardAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<LeaderboardUser> leaderboardUserList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public LeaderboardAdapter(Activity activity, List<LeaderboardUser> leaderboardUserList) {
        this.activity = activity;
        this.leaderboardUserList = leaderboardUserList;
    }

    @Override
    public int getCount() {
        return leaderboardUserList.size();
    }

    @Override
    public Object getItem(int location) {
        return leaderboardUserList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.leaderboard_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView username = (TextView) convertView.findViewById(R.id.title);
        TextView rank = (TextView) convertView.findViewById(R.id.rating);
        TextView articles_count = (TextView) convertView.findViewById(R.id.genre);
        TextView user_id = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        LeaderboardUser m = leaderboardUserList.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        username.setText(m.getUsername());

        // rating
        rank.setText("Rank: " + String.valueOf(m.getRank()));

        // genre

        articles_count.setText("Articles : " + articles_count);

        // release year
        user_id.setText("UID :" + String.valueOf(m.getUser_id()));

        return convertView;
    }

}
