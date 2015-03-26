package in.niooz.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class ArticleWebView extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_web_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Article Title");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A82400")));

        int articleId = getIntent().getExtras().getInt("articleId");
        Toast.makeText(getApplicationContext(),"Got Link for Article : niooz.in/article/" + articleId,Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_share :    Intent sendIntent = new Intent();
                                        sendIntent.setAction(Intent.ACTION_SEND);
                                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                                        sendIntent.setType("text/plain");
                                        startActivity(sendIntent);
                                        break;
            case R.id.action_fav :      if(favourite_article()){
                                            item.getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.ADD));
                                            Toast.makeText(getApplicationContext(),"Article favourited",Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(),"Some Error Try Again",Toast.LENGTH_LONG).show();
                                        }
                                        break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean favourite_article(){
        return true;
    }


}
