package weplay.auptsoft.daregame.presenters;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import es.dmoral.toasty.Toasty;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.fragments.ChallengeAttemptFragment;
import weplay.auptsoft.daregame.fragments.MediaItemFragment;

/**
 * Created by Andrew on 25.3.19.
 */

public class ChallengeDetailsPresenter extends BaseObservable {

    private boolean commentPostLoading = false;
    private boolean likeActionLoading = false;

    private boolean liked;
    private Context context;

    public ChallengeDetailsPresenter(Context context) {
        this.context = context;
    }

    @Bindable
    public boolean isCommentPostLoading() {
        return commentPostLoading;
    }

    public void setCommentPostLoading(boolean commentPostLoading) {
        this.commentPostLoading = commentPostLoading;
        notifyPropertyChanged(BR.commentPostLoading);
    }

    @Bindable
    public boolean isLikeActionLoading() {
        return likeActionLoading;
    }

    public void setLikeActionLoading(boolean likeActionLoading) {
        this.likeActionLoading = likeActionLoading;
        notifyPropertyChanged(BR.likeActionLoading);
    }

    @Bindable
    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
        notifyPropertyChanged(BR.liked);
    }

    @BindingAdapter("android:tag")
    public static void setLiked(ImageView imgV, boolean liked) {
        if(liked) {
            imgV.setImageResource(R.drawable.ic_thumb_up_black_taken_24dp);
        } else {
            imgV.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void attemptNow() {
        if(AppState.currentChallenge.getChallenged_id() == 0) {
            ChallengeAttemptFragment challengeAttemptFragment = new ChallengeAttemptFragment();
            challengeAttemptFragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "Attempt Challenge");
        } else {
            if(AppState.currentChallenge.getChallenged_media().size() < 1) {
                Toasty.info(context, "Challenge has not be attempted").show();
            } else {
                MediaItemFragment.newInstance(AppState.currentChallenge.getChallenged_media().get(0))
                        .show(((AppCompatActivity)context).getSupportFragmentManager(), "play challenge attempt");
            }
        }
    }
}
