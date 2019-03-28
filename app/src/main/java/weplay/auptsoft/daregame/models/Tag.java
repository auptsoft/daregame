package weplay.auptsoft.daregame.models;

/**
 * Created by Andrew on 7.3.19.
 */

public class Tag {
    private int id;
    private String title;
    private String description;
    private String created_at;
    private String updated_at;

    public Tag() {
        this(0, "", "", "", "");
    }

    public Tag(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public Tag(int id, String title, String description, String created_at, String updated_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
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
