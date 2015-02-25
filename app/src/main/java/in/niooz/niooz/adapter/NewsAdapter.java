package in.niooz.niooz.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import java.net.NetworkInterface;
import java.util.List;

import in.niooz.niooz.R;
import in.niooz.niooz.model.News;

/**
 * Created by aditya on 2/21/15.
 */
public class NewsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<News> newsItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public NewsAdapter(Activity activity,List<News> newsItems){
        this.activity = activity;
        this.newsItems = newsItems;
    }


    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return newsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.list_row,null);
        }

        if(imageLoader == null){
            imageLoader = AppController.getInstance().getImageLoader();
        }

        NetworkImageView headlineThumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView headline = (TextView) convertView.findViewById(R.id.headline);
        TextView likes = (TextView) convertView.findViewById(R.id.likes);

        News n = newsItems.get(position);

        headlineThumbnail.setImageUrl(n.getHeadlineBackgroundURL(),imageLoader);

        headline.setText(n.getHeadline());

        likes.setText(n.getLikes());

        return convertView;
    }
}
