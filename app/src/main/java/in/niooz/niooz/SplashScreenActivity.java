package in.niooz.niooz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.Plus;

import org.json.JSONException;
import org.json.JSONObject;


public class SplashScreenActivity extends Activity {

    ProgressBar pBar;
    private String TRENDING_URL = "http://itechnospot.com/temp/trending.php";
    private String th1,th2,th3,th4;
    private static int SPLASH_TIME_OUT = 2500;
    private static int PROCESSING_TIMEOUT = 1500;
    private boolean failed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        pBar = (ProgressBar) findViewById(R.id.progressBar3);
        pBar.setVisibility(View.GONE);
        pBar.setIndeterminate(true);
        pBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FAFAFA"), PorterDuff.Mode.MULTIPLY);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences pref;
                pref = getSharedPreferences("niooz", MODE_PRIVATE);
                try {
                    String getStatus = pref.getString("register", null);
                    Log.d("Register Status", getStatus);
                    if (getStatus.equals("true")) {
                        new LoadTrendingNews().execute();
                    } else {
                        Log.d("Register Status", "Not Registered Moving to Registration");
                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        // close this activity
                        finish();
                    }
                } catch (Exception ex) {
                    Log.d("Register Status", "Not Registered Moving to Registration");
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, PROCESSING_TIMEOUT);


}
    public class LoadTrendingNews extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog1 = new ProgressDialog(SplashScreenActivity.this);
            //pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //pDialog1.setMessage("Cooking News for You...");
            //pDialog1.show();

        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                ServiceHandler sh = new ServiceHandler();

                final String respStr = sh.makeServiceCall(TRENDING_URL, ServiceHandler.POST);
                //Log.d("Response: ", "> " + res);
                JSONObject jsonObject = new JSONObject(respStr);
                th1 = jsonObject.getString("t1");
                th2 = jsonObject.getString("t2");
                th3 = jsonObject.getString("t3");
                th4 = jsonObject.getString("t4");


            } catch (Exception ex) {
                failed = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*
                        new AlertDialog.Builder(SplashScreenActivity.this)
                                .setTitle("Oops!!!")
                                .setMessage("Make sure you are connected to a reliable internet connection.")
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

                        Toast.makeText(getApplicationContext(),"No Internet Connection.Ensure you are connected to get Latest News Feeds",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //if (pDialog1.isShowing()) {
            //    pDialog1.dismiss();
            //}

            if(!failed) {

                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("th1", th1);
                i.putExtra("th2", th2);
                i.putExtra("th3", th3);
                i.putExtra("th4", th4);
                Log.d("Splash", th1 + th2 + th3 + th4);

                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
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
