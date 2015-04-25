package in.niooz.app;

import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.niooz.app.adapter.LeaderboardAdapter;
import in.niooz.app.app.AppController;
import in.niooz.app.model.LeaderboardUser;
import in.niooz.app.model.News;


public class LeaderBoardFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static String LEADERBOARD_URL = "http://niooz.in/index.php/leaderboard";
    private FragmentActivity fa;
    private ProgressDialog pDialog;
    private List<LeaderboardUser> leaderboardUserList = new ArrayList<LeaderboardUser>();
    private ListView listView;
    private LeaderboardAdapter adapter;

    private static final String ARG_POSITION = "position";
    private JSONArray arr;
    private int position;
    private String api_access_token;


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
            Log.d("Api_access_token", "Not found");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.leaderboard_fragment_layout, container, false);


        listView = (ListView) v.findViewById(R.id.list);
        adapter = new LeaderboardAdapter(getActivity(), leaderboardUserList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("api_access_token", api_access_token);

        CustomRequest leadersReq = new CustomRequest(Request.Method.POST, LEADERBOARD_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());


                        Log.d("Api_access_token", response.toString());

                        try {
                            arr = new JSONArray(response);
                        } catch (Exception ex) {

                        }

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = arr.getJSONObject(i);
                                LeaderboardUser leaderboardUser = new LeaderboardUser();
                                leaderboardUser.setUsername(obj.getString("username"));
                                leaderboardUser.setThumbnailUrl(obj.getString("profile_pic"));
                                leaderboardUser.setRank(((int) obj.get("rank")));
                                leaderboardUser.setArticles_count(obj.getString("articles_count"));
                                leaderboardUser.setUser_id(obj.getString("user_id"));

                                // adding movie to movies array
                                leaderboardUserList.add(leaderboardUser);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

                        if (pDialog != null) {
                            pDialog.dismiss();
                            pDialog = null;
                        }
                        //addNewsToDatabase(newsList);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }

                });

        AppController.getInstance().addToRequestQueue(leadersReq);
        return v;
    }
}
