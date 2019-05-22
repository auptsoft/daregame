package weplay.auptsoft.daregame.fragments;

import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.rtp.AudioStream;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.MediaController;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.FragmentMediaItemBinding;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.view_models.MediaViewModel;

/**
 * Created by Andrew on 3.4.19.
 */

public class MediaItemFragment extends DialogFragment implements View.OnClickListener, MediaController.MediaPlayerControl {

    FragmentMediaItemBinding binding;
    MediaViewModel viewModel;
    public static final String MEDIA_KEY ="MEDIA_KEY";

    MediaPlayer mediaPlayer;
    MediaController mediaController;

    Handler handler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jsonString = getArguments().getString(MEDIA_KEY);
        Media media = new Gson().fromJson(jsonString, Media.class);

        viewModel = new MediaViewModel(media, getContext());

        mediaPlayer = new MediaPlayer();
        mediaController = new MediaController(getContext());
        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_media_item, container, false);
        View view = binding.getRoot();
        binding.setViewModel(viewModel);

        initializeView();

        binding.audioView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onPause() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(binding.audioView)) {
            mediaController.show();
        }
    }

    void initializeView() {
        if(viewModel.getMedia().getType().equals("image")) {
            Glide.with(this)
                    .load(viewModel.getMedia().getFull_url())
                    .into(binding.photoView);

        } else if (viewModel.getMedia().getType().equals("audio")) {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(viewModel.getMedia().getFull_url());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mediaPlayer) {
                        mediaController.setMediaPlayer(self());
                        mediaController.setAnchorView(binding.audioView);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mediaController.setEnabled(true);
                                mediaController.show();
                                mediaPlayer.start();

                                binding.audioPlayingText.setPivotX(binding.audioPlayingText.getWidth()/2);
                                binding.audioPlayingText.setPivotY(binding.audioPlayingText.getHeight()/2);

                                binding.audioPlayingText.setText("Playing");

                                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.2f, 1, 1.2f);
                                binding.audioPlayingText.setAnimation(scaleAnimation);
                                scaleAnimation.setRepeatCount(Animation.INFINITE);
                                scaleAnimation.setRepeatMode(Animation.REVERSE);
                                scaleAnimation.setDuration(1500);
                                scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                                scaleAnimation.start();
                            }
                        });

                        binding.shimmer.stopShimmerAnimation();
                    }
                });
            } catch (Exception e) {
                Log.d("E", e.getMessage());
                e.printStackTrace();
                Toasty.error(getContext(), e.getMessage()).show();
            }


        } else if (viewModel.getMedia().getType().equals("video")) {
            Uri uri = Uri.parse(viewModel.getMedia().getFull_url());
            binding.viewView.setVideoURI(uri);
            binding.viewView.setMediaController(mediaController);
            mediaController.setAnchorView(binding.viewView);
            binding.viewView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Toasty.info(getContext(), "Duration="+mediaPlayer.getDuration()).show();
                    binding.shimmer.stopShimmerAnimation();
                }
            });
            binding.viewView.start();
        }
    }

    public static MediaItemFragment newInstance(Media media) {
        String mediaJson = new Gson().toJson(media, Media.class);

        Bundle bundle  = new Bundle();
        bundle.putString(MEDIA_KEY, mediaJson);

        MediaItemFragment mediaItemFragment = new MediaItemFragment();
        mediaItemFragment.setArguments(bundle);

        return mediaItemFragment;
    }

    MediaItemFragment self() {
        return this;
    }

    @Override
    public void start() {
        mediaPlayer.start();

        binding.audioPlayingText.setText("Playing");
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.2f, 1, 1.2f);
        binding.audioPlayingText.setAnimation(scaleAnimation);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setDuration(1500);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
        binding.audioPlayingText.clearAnimation();
        binding.audioPlayingText.setText("Paused");
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
