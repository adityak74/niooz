package in.niooz.niooz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends ActionBarActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView hl1,hl2,hl3,hl4;
    private static String TRENDING_URL = "http://itechnospot.com/temp/trending.php";
    private static String TOKEN_VALIDATE_URL = "http://itechnospot.com/temp/validateToken.php";
    private ProgressDialog pDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String accessToken = extras.getString("access_token");
            String provider = extras.getString("provider");
            String res = "AccessToken : " + accessToken +"\n Provider : " + provider;
            Log.d("ACCESS_TOKEN,PROVIDER",res);
        }

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange,R.color.green,R.color.blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                new LoadTrendingNews().execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });



        hl1 = (TextView) findViewById(R.id.trendingHead1);
        hl2 = (TextView) findViewById(R.id.trendingHead2);
        hl3 = (TextView) findViewById(R.id.trendingHead3);
        hl4 = (TextView) findViewById(R.id.trendingHead4);


        new ValidateAccessToken().execute();

    }

    public class ValidateAccessToken extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(HomeActivity.this);
            pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog1.setMessage("Logging In...");
            pDialog1.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("access_token", "myToken"));
            nameValuePair.add(new BasicNameValuePair("provider", "facebook"));
            String resp = sh.makeServiceCall(TOKEN_VALIDATE_URL,ServiceHandler.POST,nameValuePair);

            if(resp.equals("OK"))
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Successfully Logged In",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pDialog1.isShowing()){
                pDialog1.dismiss();
            }
            new LoadTrendingNews().execute();
        }
    }



    public class LoadTrendingNews extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(HomeActivity.this);
            pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog1.setMessage("Cooking News for You...");
            pDialog1.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandler sh = new ServiceHandler();
            final String respStr = sh.makeServiceCall(TRENDING_URL, ServiceHandler.POST);
            try {

                //Log.d("Response: ", "> " + res);
                JSONObject jsonObject = new JSONObject(respStr);
                final String th1 = jsonObject.getString("t1");
                final String th2 = jsonObject.getString("t2");
                final String th3 = jsonObject.getString("t3");
                final String th4 = jsonObject.getString("t4");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hl1.setText(th1);
                        hl2.setText(th2);
                        hl3.setText(th3);
                        hl4.setText(th4);

                    }
                });

            }catch (JSONException ex){

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pDialog1.isShowing()){
                pDialog1.dismiss();
            }
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
