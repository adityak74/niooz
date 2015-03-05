package in.niooz.niooz;

    import android.annotation.TargetApi;
    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.content.IntentSender;
    import android.content.SharedPreferences;
    import android.content.pm.PackageInfo;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.Canvas;
    import android.graphics.Typeface;
    import android.graphics.drawable.BitmapDrawable;
    import android.os.AsyncTask;
    import android.os.Build;
    import android.renderscript.Allocation;
    import android.renderscript.RenderScript;
    import android.renderscript.ScriptIntrinsicBlur;
    import android.os.Bundle;
    import android.util.Base64;
    import android.util.Log;
    import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.ViewTreeObserver;
    import android.view.animation.AccelerateInterpolator;
    import android.view.animation.Animation;
    import android.view.animation.AnimationUtils;
    import android.view.animation.TranslateAnimation;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.RelativeLayout;
    import android.widget.TextView;
    import android.widget.Toast;
    import android.widget.ViewFlipper;

    import com.facebook.Session;
    import com.facebook.SessionState;
    import com.facebook.UiLifecycleHelper;
    import com.facebook.widget.LoginButton;
    import com.google.android.gms.auth.GoogleAuthException;
    import com.google.android.gms.auth.GoogleAuthUtil;
    import com.google.android.gms.auth.UserRecoverableAuthException;
    import com.google.android.gms.common.ConnectionResult;
    import com.google.android.gms.common.GooglePlayServicesUtil;
    import com.google.android.gms.common.Scopes;
    import com.google.android.gms.common.SignInButton;
    import com.google.android.gms.common.api.GoogleApiClient;
    import com.google.android.gms.plus.Plus;
    import com.google.android.gms.plus.model.people.Person;

    import org.apache.http.NameValuePair;
    import org.apache.http.message.BasicNameValuePair;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.security.Signature;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private ImageView image;
    private String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 0;
    private static int SPLASH_TIME_OUT = 3000;
    private String TRENDING_URL = "http://itechnospot.com/temp/trending.php";
    private String TOKEN_VALIDATE_URL = "http://itechnospot.com/temp/validateToken.php";
    private TextView textView;
    private UiLifecycleHelper uiHelper;
    private boolean mIntentInProgress;
    private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked;
    private static final int PROFILE_PIC_SIZE = 400;
    private ConnectionResult mConnectionResult;
    private String email,accessToken;
    private LoginButton authButton;
    private SignInButton btnSignIn;
    private String providerName;
    private String th1,th2,th3,th4;
    private ProgressDialog pDialog1;
    private int sessioncount = 1;
    private ViewFlipper viewFlipper;
    private float lastX;
    private String provider;
    Animation slide_in_left, slide_out_right;


    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //textView = (TextView) findViewById(R.id.textView);
        //image = (ImageView) findViewById(R.id.imageView);
        authButton = (LoginButton) findViewById(R.id.fbAuthButton);
        authButton.setReadPermissions(Arrays.asList("public_profile"));
        btnSignIn = (SignInButton) findViewById(R.id.gplusAuthButton);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;

                    resolveSignInError();
                }
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


        //applyBlur();

        Typeface custom_font = Typeface.createFromAsset(getAssets(),
                "fonts/segoe-ui.ttf");
        //textView.setTypeface(custom_font);

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        },SPLASH_TIME_OUT);
        */
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
    }

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }
    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }
    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction())
        {
            // when user first touches the screen to swap
            case MotionEvent.ACTION_DOWN:
            {
                lastX = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                float currentX = touchevent.getX();

                // if left to right swipe on screen
                if (lastX < currentX)
                {
                    // If no more View/Child to flip
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    // set the required Animation type to ViewFlipper
                    // The Next screen will come in form Left and current Screen will go OUT from Right

                    viewFlipper.setInAnimation(inFromLeftAnimation());
                    viewFlipper.setOutAnimation(outToRightAnimation());
                    // Show the next Screen
                    viewFlipper.showPrevious();
                }

                // if right to left swipe on screen
                if (lastX > currentX)
                {
                    if (viewFlipper.getDisplayedChild() == 3)
                        break;
                    // set the required Animation type to ViewFlipper
                    // The Next screen will come in form Right and current Screen will go OUT from Left
                    viewFlipper.setInAnimation(inFromRightAnimation());
                    viewFlipper.setOutAnimation(outToLeftAnimation());
                    // Show The Previous Screen
                    viewFlipper.showNext();
                }
                break;
            }
        }
        return false;

    }

    public Activity getActivity(){
        return this;
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }





    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            updateUI(false);
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(mSignInClicked==true) {
            if (requestCode == RC_SIGN_IN) {
                if (resultCode != RESULT_OK) {
                    if (mSignInClicked == true)
                        mSignInClicked = false;
                }

                mIntentInProgress = false;

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onConnected(Bundle arg0) {
        if(mSignInClicked==true) {
            Log.d("Connected GPLUS","Getting Token");
            new GetAccessToken().execute();
        }
        mSignInClicked = false;
        //Toast.makeText(this, "User is connected using Google Plus Login!", Toast.LENGTH_LONG).show();
        // Get user's information
        providerName = "GooglePlus";
        provider = "GPLUS";
        updateUI(true);
        // Update the UI after signin
        //updateUI(true);
        //pDialog.dismiss();
    }

    public class GetAccessToken extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi
                            .getCurrentPerson(mGoogleApiClient);
                    String personName = currentPerson.getDisplayName();
                    String personPhotoUrl = currentPerson.getImage().getUrl();
                    String personGooglePlusProfile = currentPerson.getUrl();
                    email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    accessToken = GoogleAuthUtil.getToken(getApplicationContext(), email, "oauth2:" + Scopes.PLUS_LOGIN + " https://www.googleapis.com/auth/plus.profile.emails.read");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(),accessToken,Toast.LENGTH_LONG).show();
                            Log.d("AccessToken",accessToken);
                        }
                    });

                    Log.d(TAG, "Name: " + personName + ", plusProfile: "
                            + personGooglePlusProfile + ", email: " + email
                            + ", Image: " + personPhotoUrl);

                    return null;
                }
            }catch (UserRecoverableAuthException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Intent recover = e.getIntent();
                startActivityForResult(recover, 125);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new ValidateAccessToken().execute();
            Log.d("Register Status","GooglePlus GetAccessToken Called Validate");
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
        finish();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {

        if (state.isOpened()) {

            //Toast.makeText(getApplicationContext(),session.getAccessToken(),Toast.LENGTH_LONG).show();
            updateUI(true);
            providerName = "Facebook";
            provider = "FB";
            accessToken = session.getAccessToken();
            if(sessioncount==1) {
                Log.d("Register Status", "Logged in...");
                new ValidateAccessToken().execute();
                sessioncount=0;
                Log.d("Register Status","FB session activity called validate token and sessioncount = " + sessioncount);
            }


        } else if (state.isClosed()) {
            sessioncount=1;
            Log.i(TAG, "Logged out...");
            updateUI(false);
        }
    }

    private void applyBlur() {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();
                blur(bmp, textView);
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();

        float radius = 5f;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
                (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(MainActivity.this);

        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());

        blur.setInput(overlayAlloc);

        blur.setRadius(radius);

        blur.forEach(overlayAlloc);

        overlayAlloc.copyTo(overlay);

        view.setBackground(new BitmapDrawable(
                getResources(), overlay));

        rs.destroy();
        //statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
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

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            //btnSignIn.setVisibility(View.GONE);
            //authButton.setVisibility(View.GONE);
        } else {
            //btnSignIn.setVisibility(View.VISIBLE);
            //authButton.setVisibility(View.VISIBLE);
        }
    }

    public class ValidateAccessToken extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(MainActivity.this);
            pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog1.setMessage("Logging In...");
            pDialog1.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair("access_token", accessToken));
            nameValuePair.add(new BasicNameValuePair("provider", providerName));
            //String url = TOKEN_VALIDATE_URL + accessToken;
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
            pDialog1 = new ProgressDialog(MainActivity.this);
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


            try{
                SharedPreferences pref;
                pref = getSharedPreferences("niooz",MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                //provider = 1 Google Plus and provider = 2 Facebook
                edit.putString("provider",provider);
                edit.putString("register","true");
                edit.commit();
                Log.d("Register Status","Registered and Saved");
            }catch (Exception ex){
                Log.d("Register Status","Registered but not saved SharedPrefs Error");
            }


            Intent i = new Intent(MainActivity.this,HomeActivity.class);
            i.putExtra("th1",th1);
            i.putExtra("th2",th2);
            i.putExtra("th3",th3);
            i.putExtra("th4",th4);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }



    }


}
