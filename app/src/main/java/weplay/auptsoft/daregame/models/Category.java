package weplay.auptsoft.daregame.models;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import weplay.auptsoft.daregame.services.Utility;

/**
 * Created by Andrew on 11.3.19.
 */

public class Category {
    private int id;
    private String title;
    private String description;
    private String created_at;
    private String updated_at;

    public static int index = 0;

    public Category(String title, String description) {
        this.id = index; index++;
        this.title = title;
        this.description = description;
        this.created_at = Utility.fromGregorianCalender(new GregorianCalendar());
        this.updated_at = Utility.fromGregorianCalender(new GregorianCalendar());
    }

    public static ArrayList<String> getTitles(ArrayList<Category> categories ) {
        ArrayList<String> titleList = new ArrayList<>();
        for (Category cat :
                categories) {
            titleList.add(cat.getTitle());
        }

        return titleList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
