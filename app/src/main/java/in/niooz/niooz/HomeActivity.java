package in.niooz.niooz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.Session;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.nirhart.parallaxscroll.views.ParallaxListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.niooz.niooz.app.AppController;
import in.niooz.niooz.adapter.NewsAdapter;
import in.niooz.niooz.model.News;
import in.niooz.niooz.util.DataBaseHandler;


public class HomeActivity extends ActionBarActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView hl1,hl2,hl3,hl4;
    private ProgressDialog pDialog;
    private ImageButton imageButton;
    private ParallaxListView newsListView;
    private List<News> newsList = new ArrayList<News>();
    private NewsAdapter adapter;
    private String TAG = "HomeActivity";
    private String TRENDING_URL = "http://itechnospot.com/temp/trending.php";
    private String BASE_URL = "http://www.itechnospot.com/api/news.json";
    String th1,th2,th3,th4;
    private GoogleApiClient mGoogleApiClient;
    private int pageToLoad = 2;
    private int pageLoaded = 1;
    private View footer;
    private DataBaseHandler dataBaseHandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A82400")));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange,R.color.green,R.color.blue);

        mSwipeRefreshLayout.canChildScrollUp();




        //Log.d("Home",getIntent().getExtras().getString("th1"));


        imageButton = (ImageButton) findViewById(R.id.imgBt);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton.setColorFilter(Color.argb(255,255,235,59));
                Intent addnews = new Intent(HomeActivity.this,AddNews.class);
                startActivity(addnews);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });


        final LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.news_home_header,null);




        hl1 = (TextView) v.findViewById(R.id.trendingHead1);
        hl2 = (TextView) v.findViewById(R.id.trendingHead2);
        hl3 = (TextView) v.findViewById(R.id.trendingHead3);
        hl4 = (TextView) v.findViewById(R.id.trendingHead4);

        try {
            hl1.setText(getIntent().getExtras().getString("th1"));
            hl2.setText(getIntent().getExtras().getString("th2"));
            hl3.setText(getIntent().getExtras().getString("th3"));
            hl4.setText(getIntent().getExtras().getString("th4"));
        }catch (Exception ex)
        {
            new LoadTrendingNews().execute();
        }


        newsListView = (ParallaxListView) findViewById(R.id.news_list_view);
        newsListView.addParallaxedHeaderView(v);
        footer = inflater.inflate(R.layout.news_item_footer,null);
        //newsListView.addFooterView(footer);
        adapter = new NewsAdapter(this,newsList);
        newsListView.setAdapter(adapter);


        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        dataBaseHandler = new DataBaseHandler(this);





        final JsonArrayRequest newsReq = new JsonArrayRequest(BASE_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                News news = new News();
                                news.setId(Integer.parseInt(obj.getString("id")));
                                news.setHeadlineBackgroundURL(obj.getString("headlineBackURL"));
                                news.setHeadline(obj.getString("headline"));
                                news.setLikes(obj.getInt("likes"));
                                news.setViews(obj.getInt("views"));
                                news.setArticlesSubmitted(obj.getInt("articlesSubmitted"));
                                news.setNoOfFollowers(obj.getInt("noOfFollowers"));
                                news.setFollowing(false);

                                newsList.add(news);
                                dataBaseHandler.addNewsItem(news);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                        //Toast.makeText(getApplicationContext(),String.valueOf(newsList.size()),Toast.LENGTH_LONG).show();
                        //addNewsToDatabase(newsList);

                        try{
                            List<News> tempnewslist = dataBaseHandler.getAllNews();
                            for(News n1 : tempnewslist){
                                String log = "ID : " + n1.getId() + " HEADLINE : " + n1.getHeadline();
                                Log.d("Result",log);
                            }
                            if(dataBaseHandler.getNewsCount() > 0)
                            {
                                Toast.makeText(getApplicationContext(),"NEWS COUNT : " + dataBaseHandler.getNewsCount(),Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(),"DB EMPTY",Toast.LENGTH_LONG).show();
                        }


                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
                /*
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Oops!!!")
                        .setMessage("Couldn't load News.Maybe no Internet connection or the link is down.Try restarting the application.")
                        .setCancelable(true)
                        .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                        */

                View errorView = inflater.inflate(R.layout.error_msg,null);
                TextView errTv = (TextView)errorView.findViewById(R.id.errorTv);
                errTv.setText("Couldn't load News.");

                //newsListView.removeFooterView(footer);
                newsListView.addFooterView(errorView);

            }
        });



        AppController.getInstance().addToRequestQueue(newsReq);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);

                Log.d("Swipe", "Refreshing News");
                newsList.clear();
                final JsonArrayRequest newsReq = new JsonArrayRequest(BASE_URL,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                hidePDialog();

                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        News news = new News();
                                        news.setId(Integer.parseInt(obj.getString("id")));
                                        news.setHeadlineBackgroundURL(obj.getString("headlineBackURL"));
                                        news.setHeadline(obj.getString("headline"));
                                        news.setLikes(obj.getInt("likes"));
                                        news.setViews(obj.getInt("views"));
                                        news.setArticlesSubmitted(obj.getInt("articlesSubmitted"));
                                        news.setNoOfFollowers(obj.getInt("noOfFollowers"));
                                        news.setFollowing(false);

                                        newsList.add(news);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();
                /*
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Oops!!!")
                        .setMessage("Couldn't load News.Maybe no Internet connection or the link is down.Try restarting the application.")
                        .setCancelable(true)
                        .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                        */

                        View errorView = inflater.inflate(R.layout.error_msg,null);
                        TextView errTv = (TextView)errorView.findViewById(R.id.errorTv);
                        errTv.setText("Couldn't load News.");

                        //newsListView.removeFooterView(footer);
                        newsListView.addFooterView(errorView);

                    }
                });
                AppController.getInstance().addToRequestQueue(newsReq);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Log.d("HomeActivity","GetCount : " + String.valueOf(newsListView.getCount()-1) + " LastVisible : " + (newsListView.getLastVisiblePosition()));
                int visible = newsListView.getLastVisiblePosition();
                int allchildcount = newsListView.getCount();

                    if (visible == (allchildcount - 1)) {

                        if(pageToLoad > pageLoaded) {
                            Log.d("HomeActivity", "Load page number "  + pageToLoad);
                            new LoadPage().execute(pageToLoad);
                            pageLoaded++;
                        }
                    }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    public class LoadPage extends AsyncTask<Integer,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newsListView.addFooterView(footer);
        }

        @Override
        protected Void doInBackground(Integer... params) {

            final int pgno = params[0];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Async Task to load page : " + pgno,Toast.LENGTH_LONG).show();
                }
            });
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            newsListView.removeFooterView(footer);
            pageToLoad++;

        }
    }

    public class LoadTrendingNews extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandler sh = new ServiceHandler();
            final String respStr = sh.makeServiceCall(TRENDING_URL, ServiceHandler.POST);
            try {

                //Log.d("Response: ", "> " + res);
                JSONObject jsonObject = new JSONObject(respStr);
                th1 = jsonObject.getString("t1");
                th2 = jsonObject.getString("t2");
                th3 = jsonObject.getString("t3");
                th4 = jsonObject.getString("t4");


            } catch (JSONException ex) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hl1.setText(th1);
            hl2.setText(th2);
            hl3.setText(th3);
            hl4.setText(th4);
            setProgressBarIndeterminateVisibility(false);
        }
    }

    public Activity getActivity(){
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void addNewsToDatabase(List<News> allNewsList){

    }

    public void setUpUsingDatabase(){

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    public void logoutGoogle(){

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mGoogleApiClient.isConnected()) {
                            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                            mGoogleApiClient.disconnect();
                            mGoogleApiClient.connect();
                        }
                        try{
                            SharedPreferences pref;
                            pref = getSharedPreferences("niooz",MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("register","false");
                            edit.putString("provider", "none");
                            edit.apply();
                            Log.d("Register Status", "Unregistered");
                        }catch (Exception ex){
                            Log.d("Register Status","UnRegistered but not saved SharedPrefs Error");
                        }
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void logoutFacebook(){
        final Session session = Session.openActiveSessionFromCache(getApplicationContext());
        if (session != null) {
            if(session.isOpened())
            {
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                session.closeAndClearTokenInformation();
                                try{
                                    SharedPreferences pref;
                                    pref = getSharedPreferences("niooz",MODE_PRIVATE);
                                    SharedPreferences.Editor edit = pref.edit();
                                    edit.putString("register","false");
                                    edit.putString("provider", "none");
                                    edit.apply();
                                    Log.d("Register Status", "Unregistered");
                                }catch (Exception ex){
                                    Log.d("Register Status","UnRegistered but not saved SharedPrefs Error");
                                }
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Session doesn't exist",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case R.id.logout :
                SharedPreferences pref;
                pref = getSharedPreferences("niooz", MODE_PRIVATE);
                try {

                    String res = pref.getString("provider", null);
                    if(res.equals("FB")){
                        logoutFacebook();
                    }
                    else if(res.equals("GPLUS")){
                        logoutGoogle();
                    }
                }
                catch (Exception ex){
                    Log.d("Error",ex.toString());
                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
