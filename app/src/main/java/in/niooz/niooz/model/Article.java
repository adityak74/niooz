package in.niooz.niooz.model;

/**
 * Created by aditya on 2/25/15.
 */
public class Article {

    private int articleId;
    private String headline;
    private String headlineBackgroundURL;
    private int likes;
    private int views;
    private int timesSubmitted;
    private int noOfFollowers;
    private int noOfShares;

    public Article(int articleId, String headline, String headlineBackgroundURL, int likes, int views, int timesSubmitted, int noOfFollowers, int noOfShares) {
        this.articleId = articleId;
        this.headline = headline;
        this.headlineBackgroundURL = headlineBackgroundURL;
        this.likes = likes;
        this.views = views;
        this.timesSubmitted = timesSubmitted;
        this.noOfFollowers = noOfFollowers;
        this.noOfShares = noOfShares;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
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
