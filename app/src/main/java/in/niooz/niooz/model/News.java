package in.niooz.niooz.model;

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

    public News(){

    }

    public News(String headline,String headlineBackgroundURL,int likes,int views,int articlesSubmitted,int noOfFollowers){
        this.headline = headline;
        this.headlineBackgroundURL = headlineBackgroundURL;
        this.likes = likes;
        this.views = views;
        this.articlesSubmitted = articlesSubmitted;
        this.noOfFollowers = noOfFollowers;

    }

    public int getId(){ return id;}

    public void setId(){ this.id = id;}

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



}
