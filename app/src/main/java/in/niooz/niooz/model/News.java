package in.niooz.niooz.model;

/**
 * Created by aditya on 2/21/15.
 */
public class News {
    private int id;
    private String headline;
    private String headlineBackgroundURL;
    private int likes;

    public News(){

    }

    public News(String headline,String headlineBackgroundURL,int likes){
        this.headline = headline;
        this.headlineBackgroundURL = headlineBackgroundURL;
        this.likes = likes;
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

}
