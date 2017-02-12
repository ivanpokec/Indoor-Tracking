package hr.foi.dbaccess;

/**
 * Created by Paula on 19.12.2016..
 */

public class CategoryModel {

    public int catId;
    public String catName;

    public CategoryModel(int catId, String catName) {
        this.catId = catId;
        this.catName = catName;

    }
    public  CategoryModel() {

    }

    public int getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }
}
