package in.niooz.app.model;

/**
 * Created by aditya on 2/25/15.
 */
public class Article {

    private int articleId;
    private String title;
    private String headlineBackgroundURL;
    private int likes;
    private int views;
    private int timesSubmitted;
    private int noOfFollowers;
    private int noOfShares;
    private String articleUrl;


    public Article(int articleId, String headline, String headlineBackgroundURL, int likes, int views, int timesSubmitted, int noOfFollowers, int noOfShares) {
        this.articleId = articleId;
        this.title = headline;
        this.headlineBackgroundURL = headlineBackgroundURL;
        this.likes = likes;
        this.views = views;
        this.timesSubmitted = timesSubmitted;
        this.noOfFollowers = noOfFollowers;
        this.noOfShares = noOfShares;
    }


    public String getArticleUrl(){ return articleUrl; }

    public void setArticleUrl(String url){ this.articleUrl = url; }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String headline) {
        this.title = headline;
    }

    public String getHeadlineBackgroundURL() {
        return headlineBackgroundURL;
    }

    public void setHeadlineBackgroundURL(String headlineBackgroundURL) {
        this.headlineBackgroundURL = headlineBackgroundURL;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getTimesSubmitted() {
        return timesSubmitted;
    }

    public void setTimesSubmitted(int timesSubmitted) {
        this.timesSubmitted = timesSubmitted;
    }

    public int getNoOfFollowers() {
        return noOfFollowers;
    }

    public void setNoOfFollowers(int noOfFollowers) {
        this.noOfFollowers = noOfFollowers;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(int noOfShares) {
        this.noOfShares = noOfShares;
    }


}
