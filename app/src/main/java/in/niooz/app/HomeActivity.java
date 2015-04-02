package in.niooz.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.Session;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.nirhart.parallaxscroll.views.ParallaxListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;



import in.niooz.app.app.AppController;
import in.niooz.app.adapter.NewsAdapter;
import in.niooz.app.model.News;
import in.niooz.app.util.DataBaseHandler;



public class HomeActivity extends Fragment {


    public static final String ARG_PAGE = "page";
    private int mPageNumber;
    private FrameLayout fl;
    private FragmentActivity fa;
    private Timer timer;
    private Handler handler = new Handler();
    private TimerTask doAsynchronousTask;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView hl1,hl2,hl3,hl4;
    private ProgressDialog pDialog;
    private ImageButton imageButton;
    private ParallaxListView newsListView;
    private List<News> newsList = new ArrayList<News>();
    private NewsAdapter adapter;
    private String TAG = "HomeActivity";
    private String TRENDING_URL = "http://itechnospot.com/temp/trending.php";
    private String BASE_URL = "http://www.itechnospot.com/api/newstest.php";
    String th1,th2,th3,th4;
    private GoogleApiClient mGoogleApiClient;
    private int pageToLoad = 2;
    private int pageLoaded = 1;
    private View footer;
    private DataBaseHandler dataBaseHandler;
    private View v;
    private ProgressBar progressBar;
    private LayoutInflater inflater1;
    private boolean couldNotLoadNews = false;
    private String api_access_token;
    private int i = 0;
    private ProgressBar pb;
    private RelativeLayout pbrl;

    public static HomeActivity create(int pageNumber) {
        HomeActivity fragment = new HomeActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        setHasOptionsMenu(true);
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fa = super.getActivity();
        ViewGroup fl = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) fl.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange,R.color.green,R.color.blue);

        mSwipeRefreshLayout.canChildScrollUp();



        //get api access token for further requests

        try {
            SharedPreferences pref;
            pref = fa.getSharedPreferences("niooz", fa.MODE_PRIVATE);
            api_access_token = pref.getString("api_access_token", null);
            Log.d("Api_access_token", api_access_token);
        } catch (Exception ex) {
            Log.d("Api_access_token","Not found");
        }

        imageButton = (ImageButton) fl.findViewById(R.id.imgBt);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                imageButton.setColorFilter(Color.argb(255,255,235,59));
                Intent addnews = new Intent(HomeActivity.this,AddNews.class);
                startActivity(addnews);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                */

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.activity_add_news);
                dialog.setTitle("Add News");
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();

                EditText urlInput = (EditText) dialog.findViewById(R.id.urlInput);
                urlInput.setSelection(urlInput.getText().length());

                Button submitBt = (Button) dialog.findViewById(R.id.submitNewsButton);
                submitBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Request for Submit",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        callAsynchronousTask();
                    }
                });

            }
        });


        inflater1 = (LayoutInflater)getActivity().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        v = inflater1.inflate(R.layout.news_home_header,null);



        hl1 = (TextView) v.findViewById(R.id.trendingHead1);
        hl2 = (TextView) v.findViewById(R.id.trendingHead2);
        hl3 = (TextView) v.findViewById(R.id.trendingHead3);
        hl4 = (TextView) v.findViewById(R.id.trendingHead4);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        try {
            hl1.setText(fa.getIntent().getExtras().getString("th1"));
            hl2.setText(fa.getIntent().getExtras().getString("th2"));
            hl3.setText(fa.getIntent().getExtras().getString("th3"));
            hl4.setText(fa.getIntent().getExtras().getString("th4"));
        }catch (Exception ex)
        {
            new LoadTrendingNews().execute();
        }


        newsListView = (ParallaxListView) fl.findViewById(R.id.news_list_view);
        newsListView.addParallaxedHeaderView(v);
        footer = inflater.inflate(R.layout.news_item_footer,null);
        //newsListView.addFooterView(footer);
        adapter = new NewsAdapter(getActivity(),newsList);
        newsListView.setAdapter(adapter);


        pDialog = new ProgressDialog(fa);
        // Showing progress dialog before making http request
        //pDialog.setMessage("Loading...");
        //pDialog.show();

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pbrl = new RelativeLayout(fa);
        pbrl.setGravity(Gravity.CENTER);
        pb = new ProgressBar(getActivity());
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);
        pb.setLayoutParams(params);
        pbrl.addView(pb);

        dataBaseHandler = new DataBaseHandler(getActivity());

        try{
            if(dataBaseHandler.getNewsCount() > 0)
            {
                List<News> tempnewslist = dataBaseHandler.getAllNews();
                adapter = new NewsAdapter(getActivity(),tempnewslist);
                newsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("DBSTORAGE","Retrieved from Database");
                pDialog.dismiss();
            }else {
                Log.d("DBSTORAGE","Cant Retrieved from Database = 0");
                Log.d("DBSTORAGE","Cannot Retrieved from Database");
                //pDialog.setMessage("Loading from Internet");
                //pDialog.show();
                fl.addView(pbrl);

                setupNews();
            }
        }catch (Exception ex){
            Log.d("DBSTORAGE","Cannot Retrieved from Database");
            //pDialog.setMessage("Loading from Internet");
            //pDialog.show();
            fl.addView(pbrl);
            setupNews();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);

                Log.d("Swipe", "Refreshing News");
                newsList.clear();
                adapter = new NewsAdapter(getActivity(),newsList);
                newsListView.setAdapter(adapter);

                Map<String, String> params = new HashMap<>();
                params.put("api_access_token", api_access_token);
                Log.d("Api_access_token",api_access_token);


                final CustomRequest newsReq = new CustomRequest(Request.Method.POST,BASE_URL,params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                hidePDialog();
                                Log.d("Api_access_token",response.toString());



                                try {
                                    String next_page_url = response.getString("next_page_url");
                                    JSONArray headlines = response.getJSONArray("headlines");

                                    // Parsing json
                                    for (i = 0; i < headlines.length(); i++) {


                                        JSONObject obj = headlines.getJSONObject(i);
                                        News news = new News();
                                        news.setId(Integer.parseInt(obj.getString("id")));
                                        news.setHeadlineBackgroundURL(obj.getString("image"));
                                        news.setHeadline(obj.getString("title"));
                                        news.setLikes(Integer.parseInt(obj.getString("likes_count")));
                                        news.setViews(obj.getInt("report_count"));
                                        news.setArticlesSubmitted(Integer.parseInt(obj.getString("submission_count")));
                                        news.setNoOfFollowers(Integer.parseInt(obj.getString("sources_count")));
                                        news.setFollowing(false);
                                        news.setLiked(Boolean.parseBoolean(obj.getString("liked")));
                                        //get category for color
                                        news.setCategory(obj.getString("category"));

                                        newsList.add(news);


                                    }
                                }catch (JSONException e) {
                                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                                    Log.d("Api_access_token","Parse Error");
                                    e.printStackTrace();
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                                addNewsToDatabase(newsList);
                                couldNotLoadNews = true;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Api_access_token", "Error: " + error.getMessage());
                                Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_LONG).show();
                                hidePDialog();

                                View errorView = inflater1.inflate(R.layout.error_msg,null);
                                TextView errTv = (TextView)errorView.findViewById(R.id.errorTv);
                                errTv.setText("Couldn't load News.");

                                //newsListView.removeFooterView(footer);
                                if(!couldNotLoadNews) {
                                    newsListView.addFooterView(errorView);
                                    couldNotLoadNews = true;
                                }

                            }

                        });

                AppController.getInstance().addToRequestQueue(newsReq);

                //mSwipeRefreshLayout.setRefreshing(false);
                new LoadTrendingNews().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }


        });

        /*
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
        */

        return fl;
    }




    public boolean setupNews(){

        Map<String, String> params = new HashMap<>();
        params.put("api_access_token", api_access_token);
        Log.d("Api_access_token",api_access_token);

        final CustomRequest newsReq = new CustomRequest(Request.Method.POST,BASE_URL,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        Log.d("Api_access_token",response.toString());



                        try {
                            String next_page_url = response.getString("next_page_url");
                            JSONArray headlines = response.getJSONArray("headlines");

                            // Parsing json
                            for (i = 0; i < headlines.length(); i++) {


                                JSONObject obj = headlines.getJSONObject(i);
                                News news = new News();
                                news.setId(Integer.parseInt(obj.getString("id")));
                                news.setHeadlineBackgroundURL(obj.getString("image"));
                                news.setHeadline(obj.getString("title"));
                                news.setLikes(Integer.parseInt(obj.getString("likes_count")));
                                news.setViews(obj.getInt("report_count"));
                                news.setArticlesSubmitted(Integer.parseInt(obj.getString("submission_count")));
                                news.setNoOfFollowers(Integer.parseInt(obj.getString("sources_count")));
                                news.setFollowing(false);
                                news.setLiked(Boolean.parseBoolean(obj.getString("liked")));
                                //get category for color
                                news.setCategory(obj.getString("category"));

                                newsList.add(news);


                            }
                        }catch (JSONException e) {
                            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                            Log.d("Api_access_token","Parse Error");
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                        addNewsToDatabase(newsList);
                        couldNotLoadNews = true;
                        pb.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Api_access_token", "Error: " + error.getMessage());
                        Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_LONG).show();
                        hidePDialog();

                        View errorView = inflater1.inflate(R.layout.error_msg,null);
                        TextView errTv = (TextView)errorView.findViewById(R.id.errorTv);
                        errTv.setText("Couldn't load News.");

                        //newsListView.removeFooterView(footer);
                        if(!couldNotLoadNews) {
                            newsListView.addFooterView(errorView);
                            couldNotLoadNews = true;
                        }

                    }

                });
        AppController.getInstance().addToRequestQueue(newsReq);
        return true;
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
            fa.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(),"Async Task to load page : " + pgno,Toast.LENGTH_LONG).show();
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
            progressBar.setVisibility(View.VISIBLE);
            /*
            hl1.setVisibility(View.GONE);
            hl2.setVisibility(View.GONE);
            hl3.setVisibility(View.GONE);
            hl4.setVisibility(View.GONE);
            */
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


            } catch (Exception e){

                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Couldn't load trending news.Try again",Toast.LENGTH_SHORT).show();
                    }
                });


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
            progressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);

            /*
            hl1.setVisibility(View.VISIBLE);
            hl2.setVisibility(View.VISIBLE);
            hl3.setVisibility(View.VISIBLE);
            hl4.setVisibility(View.VISIBLE);
            */
        }
    }

    /*
    public Activity getActivity(){
        return super.getActivity();
    }
    */

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        fa.getMenuInflater().inflate(R.menu.main_activity_menu, menu);
    }



    public void logoutGoogle(){

        new AlertDialog.Builder(getActivity())
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
                            pref = getActivity().getSharedPreferences("niooz",fa.MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("register","false");
                            edit.putString("provider", "none");
                            edit.apply();
                            Log.d("Register Status", "Unregistered");
                        }catch (Exception ex){
                            Log.d("Register Status","UnRegistered but not saved SharedPrefs Error");
                        }
                        startActivity(new Intent(getActivity(),MainActivity.class));
                        getActivity().finish();
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
        final Session session = Session.openActiveSessionFromCache(getActivity());
        if (session != null) {
            if(session.isOpened())
            {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                session.closeAndClearTokenInformation();
                                try{
                                    SharedPreferences pref;
                                    pref = getActivity().getSharedPreferences("niooz",fa.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = pref.edit();
                                    edit.putString("register","false");
                                    edit.putString("provider", "none");
                                    edit.apply();
                                    Log.d("Register Status", "Unregistered");
                                }catch (Exception ex){
                                    Log.d("Register Status","UnRegistered but not saved SharedPrefs Error");
                                }
                                startActivity(new Intent(getActivity(),MainActivity.class));
                                getActivity().finish();
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
            Toast.makeText(getActivity(),"Session doesn't exist",Toast.LENGTH_LONG).show();
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
                pref = getActivity().getSharedPreferences("niooz", fa.MODE_PRIVATE);
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
            case R.id.action_category :
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public class UpdateHeadline extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            final ServiceHandler sh = new ServiceHandler();

            final String resp = sh.makeServiceCall("http://api.itechnospot.com/status.php",ServiceHandler.GET);

            fa.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (resp.equals("OK")) {
                        handler.removeCallbacks(this);
                        Toast.makeText(getActivity(), "GOT OK FROM SERVER", Toast.LENGTH_LONG).show();

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.select_headline_layout);
                        dialog.setTitle("Add News");
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.show();

                        ListView shlv = (ListView) dialog.findViewById(R.id.headlineSuggestionListView);

                        String[] countryArray = {"India", "Pakistan", "USA", "UK"};

                        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_textview_layout, countryArray);
                        shlv.setAdapter(adapter);


                        timer.cancel();
                        timer.purge();
                    }
                }
            });




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    public void callAsynchronousTask() {
        timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            UpdateHeadline performBackgroundTask = new UpdateHeadline();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();

                            performBackgroundTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            //AsyncTask.Status st = performBackgroundTask.getStatus();
                            //Toast.makeText(getApplicationContext(),st.toString(),Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000); //execute in every 10000 ms
    }



}
