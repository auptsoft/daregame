package weplay.auptsoft.daregame.view_models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.models.Comment;

public class CommentViewModel extends BaseObservable {
    private Comment comment;

    @Bindable
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }
}
