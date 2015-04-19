package in.niooz.app.model;

/**
 * Created by aditya on 2/25/15.
 */
public class HeadlineSuggestion {

    private int headlineId;
    private String headlineTitle;

    public HeadlineSuggestion(){}

    public HeadlineSuggestion(int headlineId, String headlineTitle) {
        this.headlineId = headlineId;
        this.headlineTitle = headlineTitle;
    }

    public int getHeadlineId() {
        return headlineId;
    }

    public void setHeadlineId(int headlineId) {
        this.headlineId = headlineId;
    }

    public String getHeadline(){
        return headlineTitle;
    }

    public void setHeadline(String headlineTitle){
        this.headlineTitle = headlineTitle;
    }




}
