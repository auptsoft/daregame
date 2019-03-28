package weplay.auptsoft.daregame.models;

/**
 * Created by Andrew on 6.3.19.
 */

public class User {
    private int id;
    private String name;
    private String email;
    private User username;
    private String password;
    private String country;
    private String birthday;
    private String gender;
    private String created_at;
    private String updated_at;
    private String profile_picture_url;

    public User() {
        this(0, "", "", "", "", "", "", "", "");
    }

    public User(String name, String email, String password, String country, String birthday, String gender) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
        this.country = country;
        this.birthday = birthday;
        this.gender = gender;
    }

    public User(int id, String name, String email, String password, String country, String birthday, String gender, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.country = country;
        this.birthday = birthday;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }
}
