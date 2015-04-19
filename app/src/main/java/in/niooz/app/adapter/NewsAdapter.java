package in.niooz.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import in.niooz.app.ArticleSourcesActivity;
import in.niooz.app.R;
import in.niooz.app.ServiceHandler;
import in.niooz.app.app.AppController;
import in.niooz.app.model.News;

/**
 * Created by aditya on 2/21/15.
 */
public class NewsAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<News> newsItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private PopupMenu menu;
    private News n;
    private String api_access_token;
    private Button likeButton;
    private int likes;
    private ViewHolder holder;
    ViewHolder holder1;


    public NewsAdapter(Activity activity,List<News> newsItems){
        this.activity = activity;
        this.newsItems = newsItems;
    }

    static class ViewHolder{
        public Button likeBt;
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
        //final int _position = position;

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null){

            convertView = layoutInflater.inflate(R.layout.news_tabitem,null);
            //holder = new ViewHolder();
            //holder.likeBt = (Button) convertView.findViewById(R.id.likes);
            //holder.likeBt.setTag(holder);
            /*
            holder.likeBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder1 = (ViewHolder)v.getTag();
                    int likes = Integer.parseInt(holder1.likeBt.getText().toString());
                    //Toast.makeText(activity,String.valueOf(newsItems.get(position).getLikes()),Toast.LENGTH_LONG).show();

                    if(newsItems.get(position).getLiked()) {
                        holder1.likeBt.setText(String.valueOf(likes - 1));
                        newsItems.get(position).setLiked(false);
                        holder1.likeBt.setBackground(activity.getResources().getDrawable(R.mipmap.ic_star_icon));
                        //new likeHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        holder1.likeBt.setText(String.valueOf(likes + 1));
                        newsItems.get(position).setLiked(true);
                        holder1.likeBt.setBackground(activity.getResources().getDrawable(R.mipmap.ic_star_on));
                        //new likeHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                }
            });
            */
        }

        if(imageLoader == null){
            imageLoader = AppController.getInstance().getImageLoader();
        }

        NetworkImageView headlineThumbnail = (NetworkImageView) convertView.findViewById(R.id.newsBackgroundImage);
        final TextView headline = (TextView) convertView.findViewById(R.id.newsHeadLine);
        View clickView = convertView.findViewById(R.id.headlineClickView);
        RelativeLayout blurLayout = (RelativeLayout) convertView.findViewById(R.id.blurLayout);
        final ImageButton listItemOpBt = (ImageButton) convertView.findViewById(R.id.listItemOptionButton);
        ImageView glassLayer = (ImageView) convertView.findViewById(R.id.glassLayer);
        ImageView triangleImg = (ImageView) convertView.findViewById(R.id.triangleImg);

        final TextView noofviewstv = (TextView) convertView.findViewById(R.id.noOfViewsTv);
        final TextView noofarticlestv = (TextView) convertView.findViewById(R.id.noOfArticlesTv);
        final TextView nooffollowerstv = (TextView) convertView.findViewById(R.id.noOfFollowersTv);
        final TextView likeTv = (TextView) convertView.findViewById(R.id.likes);




        n = newsItems.get(position);

        headlineThumbnail.setImageUrl(n.getHeadlineBackgroundURL(),imageLoader);

        headline.setText(n.getHeadline());

        likeTv.setText(String.valueOf(n.getLikes()));

        likes = n.getLikes();

        noofviewstv.setText(String.valueOf(n.getViews()));

        noofarticlestv.setText(String.valueOf(n.getArticlesSubmitted()));

        nooffollowerstv.setText(String.valueOf(n.getNoOfFollowers()));

        //set like button color and status
        if(n.getLiked()) {
            //likeBt.setBackground(activity.getResources().getDrawable(R.mipmap.ic_star_on));
            //new likeHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            //likeBt.setBackground(activity.getResources().getDrawable(R.mipmap.ic_star_icon));
            //new likeHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }


        /*set category wise color*/

        switch (n.getCategory()){
            case "politics" : //glassLayer.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.politics_color_glass)));
                              triangleImg.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.politics_color)));

                              break;
            case "sport"   : //glassLayer.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.sports_color_glass)));
                              triangleImg.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.sports_color)));

                              break;
            case "scitech"   : //glassLayer.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.scitech_color_glass)));
                               triangleImg.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.scitech_color)));

                               break;
            case "entertainment"   : //glassLayer.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.entertainment_color_glass)));
                                     triangleImg.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.entertainment_color_glass)));

                                     break;
            case "business"   : //glassLayer.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.business_color_glass)));
                                triangleImg.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.business_color)));

                                break;
            case "news"   : //glassLayer.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.news_color_glass)));
                            triangleImg.setImageDrawable(new ColorDrawable(activity.getResources().getColor(R.color.news_color)));

                            break;

        }



        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Clicked on : " + headline.getText(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(activity, ArticleSourcesActivity.class);
                i.putExtra("headline", headline.getText().toString());
                activity.startActivity(i);
            }
        });



        listItemOpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(activity, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            /*
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
                                */

                            case R.id.action_share:
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, headline.getText() + "--Read More at Niooz.in");
                                sendIntent.setType("text/plain");
                                activity.startActivity(sendIntent);
                                break;
                            case R.id.action_report:
                                Toast.makeText(activity, "Reporting " + headline.getText(), Toast.LENGTH_LONG).show();

                                PopupMenu reportMenu = new PopupMenu(activity, v);

                                reportMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {


                                        switch (item.getItemId()) {

                                            case R.id.action_spam: //new reportHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"spam");
                                                break;
                                            case R.id.action_porn: //new reportHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"porn");
                                                break;
                                            case R.id.action_insult:
                                                //new reportHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"insult");
                                                break;
                                            case R.id.action_annoy://new reportHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"annoy");
                                                break;
                                            case R.id.action_fake://new reportHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"fake");
                                                break;
                                            case R.id.action_hate://new reportHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"hate");
                                                break;
                                            case R.id.action_others://new reportHeadline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"others");
                                                break;
                                        }
                                        return true;
                                    }

                                });


                                reportMenu.inflate(R.menu.report_menu);
                                reportMenu.show();


                                break;
                        }
                        return true;
                    }
                });

                popupMenu.inflate(R.menu.news_item_menu);
                /*
                if(n.getFollowing()) {
                    popupMenu.getMenu().findItem(R.id.action_follow).setTitle("Unfollow");
                }else{
                    popupMenu.getMenu().findItem(R.id.action_follow).setTitle("Follow");
                }
                */

                popupMenu.show();

            }
        });



        //applyBlur(headlineThumbnail,blurLayout);

        return convertView;
    }

    public class reportHeadline extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            try{
                SharedPreferences pref;
                pref = activity.getSharedPreferences("niooz", Context.MODE_PRIVATE);
                api_access_token = pref.getString("api_access_token", null);
            }catch (Exception ex){
                Log.d("Like Status","Cannot get API_ACCESS_TOKEN");
            }
            String likeUrl = "http://niooz.in/headline/report";

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair("headline_id", String.valueOf(n.getId())));
            nameValuePair.add(new BasicNameValuePair("api_access_token", api_access_token));
            nameValuePair.add(new BasicNameValuePair("report_reason",params[0]));
            String response = sh.makeServiceCall(likeUrl,ServiceHandler.POST);

            if(response.equals("reported")) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Headline Reported.Will be checked soon.", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,"Some error occurred.Please try again",Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
    }


    public class likeHeadline extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                SharedPreferences pref;
                pref = activity.getSharedPreferences("niooz", Context.MODE_PRIVATE);
                api_access_token = pref.getString("api_access_token", null);
            }catch (Exception ex){
                Log.d("Like Status","Cannot get API_ACCESS_TOKEN");
            }


            String likeUrl = "http://niooz.in/headline/like";

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair("headline_id", String.valueOf(n.getId())));
            nameValuePair.add(new BasicNameValuePair("api_access_token", api_access_token));
            String response = sh.makeServiceCall(likeUrl,ServiceHandler.POST);

            if(response.equals("liked")) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Headline Liked", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else if(response.equals("unliked")){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"Headline Unliked",Toast.LENGTH_LONG).show();
                        }
                    });
            }else{
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,"Some error occurred.Please try again",Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
    }


    /* Blurring API current branch removed
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
    */



}
