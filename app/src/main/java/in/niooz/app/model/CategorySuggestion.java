package in.niooz.app.model;

/**
 * Created by aditya on 4/19/15.
 */
public class CategorySuggestion {
    private  String categoryLabel="";
    private  String code="";
    private int suggested_category=0;


    /*********** Set Methods ******************/

    public void setCategoryLabel(String categoryLabel)
    {
        this.categoryLabel = categoryLabel;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setSuggested_category(int flag){ this.suggested_category = flag;}



    /*********** Get Methods ****************/

    public String getCategoryLabel()
    {
        return this.categoryLabel;
    }

    public String getCode()
    {
        return this.code;
    }

    public int getSuggested_category(){ return  this.suggested_category; }

}
