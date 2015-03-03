package in.niooz.niooz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class SplashScreenActivity extends Activity {

    TextView textView;
    ProgressDialog pDialog1;
    private String TRENDING_URL = "http://itechnospot.com/temp/trending.php";
    private String th1,th2,th3,th4;
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView = (TextView) findViewById(R.id.textView);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),
                "fonts/segoe-ui.ttf");
        textView.setTypeface(custom_font);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences pref;
                pref = getSharedPreferences("niooz", MODE_PRIVATE);
                try {
                    String getStatus = pref.getString("register", null);
                    Log.d("Register Status",getStatus);
                    if(getStatus.equals("true")){
                        new LoadTrendingNews().execute();
                    }
                }catch (Exception ex){
                    Log.d("Register Status","Not Registered Moving to Registration");
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);


}
    public class LoadTrendingNews extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(SplashScreenActivity.this);
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
            if (pDialog1.isShowing()) {
                pDialog1.dismiss();
            }

            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            i.putExtra("th1", th1);
            i.putExtra("th2", th2);
            i.putExtra("th3", th3);
            i.putExtra("th4", th4);
            Log.d("Splash",th1 + th2 + th3 + th4);

            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
