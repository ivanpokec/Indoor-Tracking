package hr.foi.dbaccess;

/**
 * Created by Paula on 20.12.2016..
 */

public class LocationModel {
    private int Id;
    private String name;
    private String macAddress;
    private String description;
    private String category;

//    public CategoryModel(int catId, String catName) {
//        this.catId = catId;
//        this.catName = catName;
//
//    }
//    public  CategoryModel() {
//
//    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }


}
