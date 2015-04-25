package in.niooz.app.model;

/**
 * Created by aditya on 4/21/15.
 */
import java.util.ArrayList;

public class LeaderboardUser {


    private String username, thumbnailUrl;
    private int rank;
    private String articles_count;
    private String user_id;

    public LeaderboardUser() {
    }

    public LeaderboardUser(String name, String thumbnailUrl, int rank, String articles_count,
                 String user_id) {
        this.username = name;
        this.thumbnailUrl = thumbnailUrl;
        this.rank = rank;
        this.articles_count = articles_count;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getArticles_count() {
        return articles_count;
    }

    public void setArticles_count(String articles_count) {
        this.articles_count = articles_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
