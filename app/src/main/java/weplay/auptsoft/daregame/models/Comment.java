package weplay.auptsoft.daregame.models;

public class Comment {
    private int id;
    private String title;
    private String content;
    private int commentable_id;
    private String commentable_type;
    private int user_id;

    private Commetable commetable;
    private User user;

    public Comment() {
        this("", "", 0);
    }

    public Comment(String title, String content, int commentable_id) {
        this(0, "", "", 0, "");
        this.title = title;
        this.content = content;
        this.commentable_id = commentable_id;
    }

    public Comment(int id, String title, String content, int commentable_id, String commentable_type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.commentable_id = commentable_id;
        this.commentable_type = commentable_type;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentable_id() {
        return commentable_id;
    }

    public void setCommentable_id(int commentable_id) {
        this.commentable_id = commentable_id;
    }

    public String getCommentable_type() {
        return commentable_type;
    }

    public void setCommentable_type(String commentable_type) {
        this.commentable_type = commentable_type;
    }


    public Commetable getCommetable() {
        return commetable;
    }

    public void setCommetable(Commetable commetable) {
        this.commetable = commetable;
    }

    public User getUser() {
        return user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
