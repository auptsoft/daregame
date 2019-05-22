package weplay.auptsoft.daregame.view_models;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.florent37.shapeofview.shapes.CutCornerView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.activities.ChallengeDetialsActivity;
import weplay.auptsoft.daregame.fragments.MediaUploadFragment;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.models.User;

/**
 * Created by Andrew on 21.3.19.
 */

public class ChallengeViewModel extends BaseObservable {
    private Challenge challenge;
    private Context context;

    private String printablePrice;
    private String printableLikes;

    private boolean videoType, imageType, audioType;
    private boolean liveSource, gallerySource;

    private boolean freeAttempt;

    private String challenger_profile_pic;
    private String challenge_image;
    private View sharedView;

    public ChallengeViewModel(Challenge challenge, Context context) {
        this.challenge = challenge;
        this.context = context;
        if (challenge.getChallenger() != null)
            challenger_profile_pic = challenge.getChallenger().getProfile_picture_url();

        /*handleFreeAttempt();
        handleMediaSource();
        handleMediaType(); */

        setFreeAttempt(challenge.isFree_attempt());

        setImageType(challenge.getAccept_media_type().equals("image"));
        setAudioType(challenge.getAccept_media_type().equals("audio"));
        setVideoType(challenge.getAccept_media_type().equals("video"));

        setLiveSource(challenge.getMedia_source().equals("live"));

        setFreeAttempt(challenge.isFree_attempt());
        //setChallenge_image(challenge.getChallenger_media().get(0).getUrl());
    }

    public String getFormatedDate() {
        return challenge.getCreated_at();
    }

    public String getChallenger_profile_pic() {
        return challenger_profile_pic;
    }

    public void setChallenger_profile_pic(String challenger_profile_pic) {
        this.challenger_profile_pic = challenger_profile_pic;
    }

    @BindingAdapter("android:tag")
    public static void setChallenger_profile_pic(ImageView imageView, String challenger_profile_pic) {
        Glide.with(imageView)
                .load(challenger_profile_pic)
                .into(imageView);
    }

    @Bindable
    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        //notifyPropertyChanged();
    }

    public int getVideoIconVisible() {
        if((challenge.getChallenger_media().size() > 0) && challenge.getChallenger_media().get(0).getType().equals("video")) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    @Bindable
    public boolean isVideoType() {
        return videoType;
    }

    public void setVideoType(boolean videoType) {
        this.videoType = videoType;
        notifyPropertyChanged(BR.videoType);
        handleMediaType();
    }


    @Bindable
    public boolean isImageType() {
        return imageType;
    }

    public void setImageType(boolean imageType) {
        this.imageType = imageType;
        notifyPropertyChanged(BR.imageType);
        handleMediaType();
    }

    @Bindable
    public boolean isAudioType() {
        return audioType;
    }

    public void setAudioType(boolean audioType) {
        this.audioType = audioType;
        notifyPropertyChanged(BR.audioType);
        handleMediaType();
    }

    @Bindable
    public boolean isLiveSource() {
        return liveSource;
    }

    public void setLiveSource(boolean liveSource) {
        this.liveSource = liveSource;
        notifyPropertyChanged(BR.liveSource);
        handleMediaSource();
    }

    @Bindable
    public boolean isGallerySource() {
        return gallerySource;
    }

    public void setGallerySource(boolean gallerySource) {
        this.gallerySource = gallerySource;
        notifyPropertyChanged(BR.gallerySource);
        handleMediaSource();
    }

    @Bindable
    public boolean isFreeAttempt() {
        return freeAttempt;
    }

    public void setFreeAttempt(boolean freeAttempt) {
        this.freeAttempt = freeAttempt;
        notifyPropertyChanged(BR.freeAttempt);
        handleFreeAttempt();
    }

    @BindingAdapter("android:visibility")
    public static void setFreeAttempt(CutCornerView cutCornerView, boolean freeAttempt) {
        cutCornerView.setVisibility(freeAttempt ? View.VISIBLE : View.GONE);
    }

    @Bindable
    public String getChallenge_image() {
        return challenge_image;
    }

    public void setChallenge_image(String challenge_image) {
        this.challenge_image = challenge_image;
        notifyPropertyChanged(BR.challenge_image);
    }

    @BindingAdapter("android:src")
    public static void setChallenge_image(ImageView imageView, Challenge challenge) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.example_pic)
                .centerCrop();
        if (challenge.getChallenger_media().size() > 0){
            Media media = challenge.getChallenger_media().get(0);
            String mediaType = media.getType();
            if(mediaType.equals("video") || mediaType.equals("image")) {
                Glide.with(imageView)
                        .load(media.getFull_url())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOptions)
                        .into(imageView);
            } else if (mediaType.equals("audio")) {
                imageView.setImageResource(R.drawable.ic_audiotrack_black_24dp);
                imageView.setScaleX(0.7f);
                imageView.setScaleY(0.7f);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

    } else {
        imageView.setImageResource(R.drawable.ic_chevron_left_black_24dp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

}

    private void handleMediaType() {
        if (videoType) {
            challenge.setAccept_media_type("video");
            challenge.setRequest_media_type("video");
        } else if (imageType) {
            challenge.setAccept_media_type("image");
            challenge.setRequest_media_type("image");
        } else if (audioType) {
            challenge.setAccept_media_type("audio");
            challenge.setRequest_media_type("audio");
        }
    }

    private void handleMediaSource() {
        if (liveSource) {
            challenge.setMedia_source("live");
        } else if (gallerySource) {
            challenge.setMedia_source("gallery");
        }
    }

    private void handleFreeAttempt() {
        challenge.setFree_attempt(freeAttempt);
    }

    public void viewDetails(Challenge challenge) {
        Intent intent = new Intent(context, ChallengeDetialsActivity.class);

        if (sharedView != null) {
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity) context, sharedView, context.getString(R.string.challenge_transition_1));

            AppState.currentChallenge = challenge;
            ((AppCompatActivity) context).startActivity(intent, activityOptionsCompat.toBundle());
        } else {
            AppState.currentChallenge = challenge;
            ((AppCompatActivity) context).startActivity(intent);
        }

    }

    public View getSharedView() {
        return sharedView;
    }

    public void setSharedView(View sharedView) {
        this.sharedView = sharedView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Bindable
    public String getPrintablePrice() {
        return printablePrice;
    }

    @Bindable
    public String getPrintableLikes() {
        return printableLikes;
    }

    public void setPrintableLikes(String printableLikes) {
        this.printableLikes = printableLikes;
    }

    public void uploadMedia() {
        String jsonString = new Gson().toJson(challenge, Challenge.class);
        Toast.makeText(context, jsonString, Toast.LENGTH_LONG).show();

        Media media = new Media();
        media.setExtra("challenger");
        media.setType(challenge.getRequest_media_type());
        media.setMedia_source(challenge.getMedia_source());

        /*((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.post_challenge_frame_one, MediaUploadFragment.newInstance(media))
                .addToBackStack("")
                .commit(); */
        MediaUploadFragment.newInstance(media, "", true).show(((AppCompatActivity) context).getSupportFragmentManager(), "upload media");
    }

    private boolean idExist(List<User> userList, int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
