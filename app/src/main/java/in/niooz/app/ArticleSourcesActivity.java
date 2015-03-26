package in.niooz.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class ArticleSourcesActivity extends ActionBarActivity {

    ListView listView;
    String headline;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_article_sources);

        headline = getIntent().getStringExtra("headline");
        if(savedInstanceState != null){
            headline = savedInstanceState.getString("headline");
        }
        //Toast.makeText(getApplicationContext(),headline,Toast.LENGTH_LONG).show();

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Source:"+headline);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A82400")));





        listView = (ListView) findViewById(R.id.sourceList);
        String[] values = new String[] {    "Article Source 1",
                                            "Article Source 2",
                                            "Article Source 3",
                                            "Article Source 4",
                                            "Article Source 5",
                                            "Article Source 6",
                                            "Article Source 7",
                                            "Article Source 8",
                                            "Article Source 9",
                                            "Article Source 10",
                                            "Article Source 11",
                                            "Article Source 12",
                                            "Article Source 13",
                                            "Article Source 14",
                                            "Article Source 15"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Position : "+position+"\nListItem : " + listView.getItemAtPosition(position) , Toast.LENGTH_LONG)
                        .show();
                Intent i = new Intent(ArticleSourcesActivity.this,ArticleWebView.class);
                i.putExtra("link",(String)listView.getItemAtPosition(position));
                startActivity(i);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("headline",headline);
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        headline = savedInstanceState.getString("headline");
        actionBar.setTitle("Source:"+headline);
        Toast.makeText(getApplicationContext(),"OnRestSIS",Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),headline,Toast.LENGTH_LONG).show();
        //super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_article_sources, menu);
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
