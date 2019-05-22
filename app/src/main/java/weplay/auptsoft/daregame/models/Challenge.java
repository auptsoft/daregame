package weplay.auptsoft.daregame.models;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import weplay.auptsoft.daregame.services.Utility;

/**
 * Created by Andrew on 7.3.19.
 */

public class Challenge {
    private int id;
    private int challenger_id;
    private int challenged_id;
    private String title;
    private String description;
    private double price;
    private int category_id;
    private String accept_media_type;
    private String request_media_type;
    private String media_source;
    private String accepted_at;
    private String performed_at;
    private String max_acceptance_at;
    private boolean free_attempt;
    private String created_at;
    private String updated_at;

    private User challenger;
    private User challenged;
    private List<User> likes;

    private Category category;

    private List<Media> challenger_media;
    private List<Media> challenged_media;
    private List<Tag> tags;
    private List<Comment> comments;

    public Challenge() {
        this(
                0,
                0,
                0,
                "",
                "",
                5.2,
                1,
                "image",
                "image",
                "live",
                "0000-00-00 00:00:00",
                "0000-00-00 00:00:00",
                "0000-00-00 00:00:00",
                Utility.fromGregorianCalender(new GregorianCalendar()),
                Utility.fromGregorianCalender(new GregorianCalendar())
        );

        GregorianCalendar twoDaysFromNow = new GregorianCalendar();
        twoDaysFromNow.add(Calendar.DAY_OF_MONTH, 2);
        setMax_acceptance_at(Utility.fromGregorianCalender(twoDaysFromNow));
        this.challenger = new User();
        this.challenged = new User();
    }

    public Challenge(String title, String description, double price, int challenger_id, int challenged_id) {
        this();
        this.challenger_id = challenger_id;
        this.challenged_id = challenged_id;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public Challenge(String title, String description, double price, User challenger, User challenged, List<User> likes) {
        this();
        this.title = title;
        this.description = description;
        this.challenger = challenger;
        this.challenged = challenged;
        this.price = price;
        this.likes = likes;
    }

    public Challenge(String title, String description, double price, int category_id, User challenger, User challenged) {
        this();
        this.title = title;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.challenger_id = challenger.getId();
        this.challenged_id = challenged.getId();
        this.challenger = challenger;
        this.challenged = challenged;
    }

    public Challenge(int id, int challenger_id, int challenged_id, String title, String description, double price, int category_id, String accept_media_type, String request_media_type, String media_source, String accepted_at, String performed_at, String max_acceptance_at, String created_at, String updated_at /*, String challenger, String challenged */) {
        this.id = id;
        this.challenger_id = challenger_id;
        this.challenged_id = challenged_id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.accept_media_type = accept_media_type;
        this.request_media_type = request_media_type;
        this.media_source = media_source;
        this.accepted_at = accepted_at;
        this.performed_at = performed_at;
        this.max_acceptance_at = max_acceptance_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        //this.challenger = challenger;
        //this.challenged = challenged;

        this.challenger = new User();
        this.challenged = new User();

        this.challenger_media = new ArrayList<>();
        this.challenged_media = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.tags = new ArrayList<>();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChallenger_id() {
        return challenger_id;
    }

    public void setChallenger_id(int challenger_id) {
        this.challenger_id = challenger_id;
    }

    public int getChallenged_id() {
        return challenged_id;
    }

    public void setChallenged_id(int challenged_id) {
        this.challenged_id = challenged_id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getAccept_media_type() {
        return accept_media_type;
    }

    public void setAccept_media_type(String accept_media_type) {
        this.accept_media_type = accept_media_type;
    }

    public String getRequest_media_type() {
        return request_media_type;
    }

    public void setRequest_media_type(String request_media_type) {
        this.request_media_type = request_media_type;
    }

    public String getMedia_source() {
        return media_source;
    }

    public void setMedia_source(String media_source) {
        this.media_source = media_source;
    }

    public String getAccepted_at() {
        return accepted_at;
    }

    public void setAccepted_at(String accepted_at) {
        this.accepted_at = accepted_at;
    }

    public String getPerformed_at() {
        return performed_at;
    }

    public void setPerformed_at(String performed_at) {
        this.performed_at = performed_at;
    }

    public String getMax_acceptance_at() {
        return max_acceptance_at;
    }

    public void setMax_acceptance_at(String max_acceptance_at) {
        this.max_acceptance_at = max_acceptance_at;
    }

    public boolean isFree_attempt() {
        return free_attempt;
    }

    public void setFree_attempt(boolean free_attempt) {
        this.free_attempt = free_attempt;
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

    public User getChallenger() {
        return challenger;
    }

    public void setChallenger(User challenger) {
        this.challenger = challenger;
    }

    public User getChallenged() {
        return challenged;
    }

    public void setChallenged(User challenged) {
        this.challenged = challenged;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Media> getChallenger_media() {
        return challenger_media;
    }

    public void setChallenger_media(List<Media> challenger_media) {
        this.challenger_media = challenger_media;
    }

    public List<Media> getChallenged_media() {
        return challenged_media;
    }

    public void setChallenged_media(List<Media> challenged_media) {
        this.challenged_media = challenged_media;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}