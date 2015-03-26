package in.niooz.app.model;

/**
 * Created by aditya on 2/25/15.
 */
public class HeadlineSuggestion {

    private int headlineId;
    private String headline;

    public HeadlineSuggestion(int headlineId, String headline) {
        this.headlineId = headlineId;
        this.headline = headline;
    }

    public int getHeadlineId() {
        return headlineId;
    }

    public void setHeadlineId(int headlineId) {
        this.headlineId = headlineId;
    }

    public String getHeadline(){
        return headline;
    }

    public void setHeadline(){
        this.headline = headline;
    }




}
