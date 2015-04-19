package in.niooz.app.model;

/**
 * Created by aditya on 4/19/15.
 */
public class CategorySuggestion {
    private  String categoryLabel="";
    private  String code="";


    /*********** Set Methods ******************/

    public void setCategoryLabel(String categoryLabel)
    {
        this.categoryLabel = categoryLabel;
    }

    public void setCode(String code)
    {
        this.code = code;
    }



    /*********** Get Methods ****************/

    public String getCategoryLabel()
    {
        return this.categoryLabel;
    }

    public String getCode()
    {
        return this.code;
    }


}
