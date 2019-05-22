package weplay.auptsoft.daregame.view_models;


import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.github.chrisbanes.photoview.PhotoView;

import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.models.Media;

/**
 * Created by Andrew on 3.4.19.
 */

public class MediaViewModel extends BaseObservable {
    private Media media;
    private boolean imageViewVisible;
    private boolean audioViewVisible;
    private boolean videoViewVisible;

    private Context context;

    public MediaViewModel(Media media, Context context) {
        this.media = media;
        this.context = context;
        handleVisibility();
    }

    private void handleVisibility(){
        setImageViewVisible(media.getType().equals("image"));
        setAudioViewVisible(media.getType().equals("audio"));
        setVideoViewVisible(media.getType().equals("video"));
    }

    @Bindable
    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
        handleVisibility();
        notifyPropertyChanged(BR.media);
    }

    @Bindable
    public boolean isImageViewVisible() {
        return imageViewVisible;
    }


    public void setImageViewVisible(boolean imageViewVisible) {
        this.imageViewVisible = imageViewVisible;
        notifyPropertyChanged(BR.imageViewVisible);
    }

    @BindingAdapter("android:visibility")
    public static void setImageViewVisible(PhotoView photoView, boolean imageViewVisible) {
        photoView.setVisibility(imageViewVisible ? View.VISIBLE : View.GONE);
    }

    @Bindable
    public boolean isAudioViewVisible() {
        return audioViewVisible;
    }

    public void setAudioViewVisible(boolean audioViewVisible) {
        this.audioViewVisible = audioViewVisible;
        notifyPropertyChanged(BR.audioViewVisible);
    }

    @BindingAdapter("android:visibility")
    public static void setAudioViewVisible(CardView cardView, boolean audioViewVisible) {
        cardView.setVisibility(audioViewVisible ? View.VISIBLE : View.GONE);
    }

    @Bindable
    public boolean isVideoViewVisible() {
        return videoViewVisible;
    }

    public void setVideoViewVisible(boolean videoViewVisible) {
        this.videoViewVisible = videoViewVisible;
        notifyPropertyChanged(BR.videoViewVisible);
    }

    @BindingAdapter("android:visibility")
    public static void setVideoViewVisible(VideoView videoView, boolean videoViewVisible) {
        videoView.setVisibility(videoViewVisible ? View.VISIBLE : View.GONE);
    }


}
