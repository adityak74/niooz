package in.niooz.niooz.model;

import android.media.Image;

/**
 * Created by aditya on 2/21/15.
 */
public class News {
    private int id;
    private String headline;
    private String headlineBackgroundURL;
    private int likes;
    private int views;
    private int articlesSubmitted;
    private int noOfFollowers;
    private boolean following;
    private String category;
    private boolean liked;

    public News(){

    }

    public News(int id,String headline,String headlineBackgroundURL,int likes,int views,int articlesSubmitted,int noOfFollowers,boolean following){
        this.id = id;
        this.headline = headline;
        this.headlineBackgroundURL = headlineBackgroundURL;
        this.likes = likes;
        this.views = views;
        this.articlesSubmitted = articlesSubmitted;
        this.noOfFollowers = noOfFollowers;
        this.following = following;
    }

    public int getId(){ return id;}

    public void setId(int id){ this.id = id;}

    public String getHeadline(){
        return headline;
    }

    public void setHeadline(String headline){
        this.headline = headline;
    }

    public String getHeadlineBackgroundURL(){
        return headlineBackgroundURL;
    }

    public void setHeadlineBackgroundURL(String headlineBackgroundURL){
        this.headlineBackgroundURL = headlineBackgroundURL;
    }

    public int getLikes(){
        return likes;
    }

    public void setLikes(int likes){
        this.likes = likes;
    }

    public int getViews(){ return views;}

    public void setViews(int views){ this.views = views;}

    public int getArticlesSubmitted(){ return articlesSubmitted;}

    public void setArticlesSubmitted(int articlesSubmitted){ this.articlesSubmitted = articlesSubmitted;}

    public int getNoOfFollowers(){ return noOfFollowers;}

    public void setNoOfFollowers(int noOfFollowers){ this.noOfFollowers = noOfFollowers;}

    public boolean getFollowing(){ return following;}

    public void setFollowing(boolean following){ this.following = following;}

    public void setCategory(String category){ this.category = category; }

    public String getCategory(){ return category; }

    public void setLiked(boolean liked){ this.liked = liked; }

    public boolean getLiked(){ return liked; }


}
