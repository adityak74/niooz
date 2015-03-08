package in.niooz.niooz.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;
import java.util.zip.Inflater;

import in.niooz.niooz.R;
import in.niooz.niooz.app.AppController;
import in.niooz.niooz.model.News;

/**
 * Created by aditya on 2/21/15.
 */
public class NewsAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<News> newsItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private PopupMenu menu;


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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.news_tabitem,null);
        }

        if(imageLoader == null){
            imageLoader = AppController.getInstance().getImageLoader();
        }

        NetworkImageView headlineThumbnail = (NetworkImageView) convertView.findViewById(R.id.newsBackgroundImage);
        final TextView headline = (TextView) convertView.findViewById(R.id.newsHeadLine);
        Button likes = (Button) convertView.findViewById(R.id.likes);
        View clickView = convertView.findViewById(R.id.headlineClickView);
        RelativeLayout blurLayout = (RelativeLayout) convertView.findViewById(R.id.blurLayout);
        final ImageButton listItemOpBt = (ImageButton) convertView.findViewById(R.id.listItemOptionButton);

        final News n = newsItems.get(position);

        headlineThumbnail.setImageUrl(n.getHeadlineBackgroundURL(),imageLoader);

        headline.setText(n.getHeadline());

        likes.setText(String.valueOf(n.getLikes()));

        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Clicked on : " + headline.getText(), Toast.LENGTH_LONG).show();
            }
        });



        listItemOpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(activity,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_follow :
                                if(!n.getFollowing()) {
                                    Toast.makeText(activity, "Following at position : " + position, Toast.LENGTH_LONG).show();
                                    n.setFollowing(true);
                                    //item.setTitle("Unfollow");
                                    Toast.makeText(activity, "Following value : " + n.getFollowing(), Toast.LENGTH_LONG).show();
                                }
                                else if(n.getFollowing()){
                                    n.setFollowing(false);
                                    Toast.makeText(activity, "UnFollowing value : " + n.getFollowing(), Toast.LENGTH_LONG).show();
                                    //item.setTitle("Follow");
                                }
                                break;

                            case R.id.action_share :
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, headline.getText() + "--Read More at Niooz.in");
                                sendIntent.setType("text/plain");
                                activity.startActivity(sendIntent);
                                break;
                            case R.id.action_report :
                                Toast.makeText(activity,"Reporting " + headline.getText(),Toast.LENGTH_LONG).show();
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.inflate(R.menu.news_item_menu);
                if(n.getFollowing()) {
                    popupMenu.getMenu().findItem(R.id.action_follow).setTitle("Unfollow");
                }else{
                    popupMenu.getMenu().findItem(R.id.action_follow).setTitle("Follow");
                }

                popupMenu.show();

            }
        });



        //applyBlur(headlineThumbnail,blurLayout);

        return convertView;
    }



    private void applyBlur(final NetworkImageView image, final View view) {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();
                blur(bmp, view);
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();

        float radius = 5f;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
                (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(activity);

        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());

        blur.setInput(overlayAlloc);

        blur.setRadius(radius);

        blur.forEach(overlayAlloc);

        overlayAlloc.copyTo(overlay);

        view.setBackground(new BitmapDrawable(
                activity.getResources(), overlay));

        rs.destroy();
        //statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }



}
