package in.niooz.app;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class LeaderBoardFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static String PROFILE_URL = "http://niooz.in/index.php/profile";
    private FragmentActivity fa;
    private String PROFILE_NAME;
    private String PROFILE_PIC;
    private String PROFILE_SCORE;
    private String PROFILE_LEVEL;
    private int position;
    private String api_access_token;
    private Bitmap bm = null;
    private ProgressBar pb;
    private RelativeLayout pbrl;

    private TextView nameTv,levelTv,scoreTv;
    private ImageView picImage;

    public static LeaderBoardFragment newInstance(int position) {
        LeaderBoardFragment f = new LeaderBoardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);

        fa = super.getActivity();

        try {
            SharedPreferences pref;
            pref = fa.getSharedPreferences("niooz", fa.MODE_PRIVATE);
            api_access_token = pref.getString("api_access_token", null);
            Log.d("Api_access_token", api_access_token);
        } catch (Exception ex) {
            Log.d("Api_access_token","Not found");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_fragment_layout,container,false);

        nameTv =  (TextView)v.findViewById(R.id.profileName);
        scoreTv = (TextView)v.findViewById(R.id.profileScoreNumber);
        levelTv = (TextView)v.findViewById(R.id.profileUserLevel);
        picImage = (ImageView)v.findViewById(R.id.profilePic);
        new setupProfile().execute();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pbrl = new RelativeLayout(fa);
        pbrl.setGravity(Gravity.CENTER);
        pb = new ProgressBar(getActivity());
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);
        pb.setLayoutParams(params);
        pbrl.addView(pb);
        return v;
    }

    public class setupProfile extends  AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                ServiceHandler sh = new ServiceHandler();
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("api_access_token", api_access_token));
                String resp = sh.makeServiceCall(PROFILE_URL, ServiceHandler.POST, params1);

                JSONObject obj = new JSONObject(resp);
                PROFILE_NAME = obj.getString("name");
                PROFILE_PIC = obj.getString("profile_pic");
                PROFILE_SCORE = obj.getString("total_score");
                PROFILE_LEVEL = obj.getString("user_level");

            }catch (JSONException ex){
                Toast.makeText(getActivity(), "Some Error Occurred." , Toast.LENGTH_LONG).show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            Log.d("USER",PROFILE_NAME + PROFILE_SCORE + PROFILE_LEVEL + PROFILE_PIC);
            nameTv.setText(PROFILE_NAME);
            scoreTv.setText(PROFILE_SCORE);
            levelTv.setText(PROFILE_LEVEL);

            new DownloadImage().execute(PROFILE_PIC);
            pb.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }

        public class DownloadImage extends AsyncTask<String,Void,Void>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String... params) {

                try {
                    URL aURL = new URL(params[0]);
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                picImage.setImageBitmap(bm);
                super.onPostExecute(aVoid);
            }
        }
    }
}
