package in.niooz.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class AddNewsReceive extends ActionBarActivity {

    ActionBarActivity actionBarActivity = this;
    private MenuItem menuItem;
    private ProgressBar progressBar;
    private WebView wv;
    private EditText urlInputEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news_receive);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add You News");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A82400")));

        urlInputEt = (EditText) findViewById(R.id.urlInput);
        wv = (WebView) findViewById(R.id.urlPreviewWv);
        wv.setWebViewClient(new MyBrowser());
        wv.setWebChromeClient(new MyChromeBrowser());

        progressBar = (ProgressBar) findViewById(R.id.progressBar4);


        progressBar.getIndeterminateDrawable().setColorFilter( Color.parseColor("#9A0000"), PorterDuff.Mode.SRC_IN);
        progressBar.getProgressDrawable().setColorFilter( Color.parseColor("#9A0000"), PorterDuff.Mode.SRC_IN);

        Intent i = getIntent();
        String action = i.getAction();
        String type = i.getType();

        if(Intent.ACTION_SEND.equals(action) && type != null)
        {
            if("text/plain".equals(type))
            {
                String getMessage = i.getStringExtra(Intent.EXTRA_TEXT);
                //Toast.makeText(getApplicationContext(), getMessage, Toast.LENGTH_LONG).show();

                Log.d("NewsLink",getMessage);
                String [] parts = getMessage.split("\\s+");

                // Attempt to convert each item into an URL.
                for( String item : parts ) try {
                    URL url = new URL(item);
                    wv.loadUrl(url.toString());
                    urlInputEt.setText(url.toString());
                    // If possible then replace with anchor...
                    //Toast.makeText(getApplicationContext(), url.toString(), Toast.LENGTH_LONG).show();
                    //System.out.print("The URL is " + url + "\n" );
                } catch (MalformedURLException e) {
                    // If there was an URL that was not it!...
                    //System.out.print( item + " " );
                }


            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error in type", Toast.LENGTH_LONG).show();
            }
        }

    }

    public class MyBrowser extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true ;
        }
    }

    public class MyChromeBrowser extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_news_receive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_load:
                menuItem = item;
                menuItem.setActionView(R.layout.progressbar);
                menuItem.expandActionView();
                TestTask task = new TestTask();
                task.execute("test");
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }
        return true;
    }

    private class TestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Simulate something long running
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            menuItem.collapseActionView();
            menuItem.setActionView(null);
        }
    }

}
