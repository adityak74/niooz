package in.niooz.niooz;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends ActionBarActivity {


    TextView loginRespTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loginRespTv = (TextView) findViewById(R.id.loginResponse);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String accessToken = extras.getString("access_token");
            String provider = extras.getString("provider");
            String res = "AccessToken : " + accessToken +"\n Provider : " + provider;
            Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
            loginRespTv.setText(res);
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
