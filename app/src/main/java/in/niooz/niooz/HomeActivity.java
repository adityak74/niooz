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
import android.widget.ListView;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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



        hl1 = (TextView) findViewById(R.id.trendingHead1);
        hl2 = (TextView) findViewById(R.id.trendingHead2);
        hl3 = (TextView) findViewById(R.id.trendingHead3);
        hl4 = (TextView) findViewById(R.id.trendingHead4);

        hl1.setText(getIntent().getExtras().getString("th1"));
        hl2.setText(getIntent().getExtras().getString("th2"));
        hl3.setText(getIntent().getExtras().getString("th3"));
        hl4.setText(getIntent().getExtras().getString("th4"));

        final ListView listview = (ListView) findViewById(R.id.headList);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);




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
