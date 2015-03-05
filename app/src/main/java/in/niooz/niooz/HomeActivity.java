package in.niooz.niooz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.nirhart.parallaxscroll.views.ParallaxListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.niooz.niooz.app.AppController;
import in.niooz.niooz.adapter.NewsAdapter;
import in.niooz.niooz.model.News;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A82400")));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange,R.color.green,R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
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


        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
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
        adapter = new NewsAdapter(this,newsList);
        newsListView.setAdapter(adapter);


        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest newsReq = new JsonArrayRequest(BASE_URL,
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

            }
        });


        AppController.getInstance().addToRequestQueue(newsReq);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
