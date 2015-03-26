package in.niooz.app.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import in.niooz.app.model.News;

/**
 * Created by aditya on 3/12/15.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "newsdb";
    private static final String TABLE_NEWS = "news";

    private static final String KEY_ID = "id";
    private static final String KEY_HEADLINE = "headline";
    private static final String KEY_HEADLINEBACKURL = "headlineBackgroundURL";
    private static final String KEY_LIKES = "likes";
    private static final String KEY_VIEWS =  "views";
    private static final String KEY_ARTICLESSUBMITTED =  "articlesSubmitted";
    private static final String KEY_NOOFFOLLOWERS = "noOfFollowers";
    private static final String KEY_FOLLOWING =  "following";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_HEADLINE + " TEXT,"
                + KEY_HEADLINEBACKURL + " TEXT,"
                + KEY_LIKES + " INTEGER,"
                + KEY_VIEWS + " INTEGER,"
                + KEY_ARTICLESSUBMITTED + " INTEGER,"
                + KEY_NOOFFOLLOWERS + " INTEGER,"
                + KEY_FOLLOWING + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        // Create tables again to update the new news data
        onCreate(db);

    }

    public void addNewsItem(News news){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,news.getId());
        values.put(KEY_HEADLINE,news.getHeadline());
        values.put(KEY_HEADLINEBACKURL,news.getHeadlineBackgroundURL());
        values.put(KEY_LIKES,news.getLikes());
        values.put(KEY_VIEWS,news.getViews());
        values.put(KEY_ARTICLESSUBMITTED,news.getArticlesSubmitted());
        values.put(KEY_NOOFFOLLOWERS,news.getNoOfFollowers());
        values.put(KEY_FOLLOWING,news.getFollowing());

        db.insert(TABLE_NEWS,null,values);
        db.close();


    }

    News getNews(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEWS,new String[]{KEY_ID,KEY_HEADLINE,KEY_HEADLINEBACKURL,
                KEY_LIKES,KEY_VIEWS,KEY_ARTICLESSUBMITTED,KEY_NOOFFOLLOWERS,KEY_FOLLOWING},KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        News news = new News(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)),Boolean.parseBoolean(cursor.getString(7)));

        return news;

    }

    public List<News> getAllNews(){
        List<News> allNewsList = new ArrayList<News>();

        String selectQuery = "SELECT * FROM " + TABLE_NEWS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                News news = new News();
                news.setId(Integer.parseInt(cursor.getString(0)));
                news.setHeadline(cursor.getString(1));
                news.setHeadlineBackgroundURL(cursor.getString(2));
                news.setLikes(Integer.parseInt(cursor.getString(3)));
                news.setViews(Integer.parseInt(cursor.getString(4)));
                news.setArticlesSubmitted(Integer.parseInt(cursor.getString(5)));
                news.setNoOfFollowers(Integer.parseInt(cursor.getString(6)));
                news.setFollowing(Boolean.parseBoolean(cursor.getString(7)));

                allNewsList.add(news);

            }while (cursor.moveToNext());
        }

        db.close();

        return allNewsList;
    }

    public int getNewsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_NEWS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int items = cursor.getCount();
        cursor.close();
        return items;
    }



}
