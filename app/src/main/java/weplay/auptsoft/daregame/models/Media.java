package weplay.auptsoft.daregame.models;

/**
 * Created by Andrew on 6.3.19.
 */

public class Media {
    private int id;
    private String name;
    private String url;
    private String full_url;
    private String type; //can be 'audio', 'image' or 'video'
    private String description;
    private int owner_id;
    private String owner_type;  //can be video, image or audio
    private String media_source = "live"; //can be live or gallery
    private String file_name;
    private String extra;   //when used with 'challenge' as owner_type, it must be 'challenger' or 'challenged'
    private String create_at;
    private String update_at;

    private String localUrl;

    public Media() {
        this(
                0, "", "", "", "", "", "", 0, "", "", ""
        );
    }

    public Media(String name, String url, String type, String file_name, String extra, int owner_id) {
        this(0, "", "", "", "", "", "", 0,  "challenge", "", "");
        this.name = name;
        this.url = url;
        this.type = type;
        this.extra = extra;
        this.owner_id = owner_id;
        this.file_name = file_name;
    }

    public Media(String localUrl, String name, String type, String description, String file_name, int owner_id, String owner_type) {
        this();
        this.localUrl = localUrl;
        this.name = name;
        this.type = type;
        this.description = description;
        this.file_name = file_name;
        this.owner_id = owner_id;
        this.owner_type = owner_type;
    }

    public Media(int id, String name, String url, String localUrl, String type, String description, String file_name, int owner_id, String owner_type, String create_at, String update_at) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.localUrl = localUrl;
        this.type = type;
        this.description = description;
        this.file_name = file_name;
        this.owner_id = owner_id;
        this.owner_type = owner_type;
        this.create_at = create_at;
        this.update_at = update_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFull_url() {
        return full_url;
    }

    public void setFull_url(String full_url) {
        this.full_url = full_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedia_source() {
        return media_source;
    }

    public void setMedia_source(String media_source) {
        this.media_source = media_source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }


    public String getExtra() {
        return extra;
    }

    /**
     * When used with 'challenge' is the value in owner_type,
     * this must be set to either 'challenger' or 'challenged'
     * @param extra
     */
    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}