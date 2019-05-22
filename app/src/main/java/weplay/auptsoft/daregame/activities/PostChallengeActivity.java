package weplay.auptsoft.daregame.activities;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;

import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.BuildConfig;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.ActivityPostChallengeBinding;
import weplay.auptsoft.daregame.fragments.MediaUploadFragment;
import weplay.auptsoft.daregame.fragments.PostChallengeFragment;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.presenters.PostChallengeActivityPresenter;

public class PostChallengeActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPostChallengeBinding binding;
    PostChallengeActivityPresenter postChallengeActivityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_challenge);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postChallengeActivityPresenter = new PostChallengeActivityPresenter();
        binding.setPresenter(postChallengeActivityPresenter);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.post_challenge_frame_one, new PostChallengeFragment()).commit();

        binding.uploadMedia.setOnClickListener(this);

        setSupportActionBar(binding.activityPostToolbar);
        binding.activityPostToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performExit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            showPage(0);
            super.onBackPressed();
        } else {
            performExit();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(binding.uploadMedia)) {
            if (AppState.currentEditChallenge != null) {
                if (BuildConfig.DEBUG) {
                    String challengeJson = new Gson().toJson(AppState.currentEditChallenge, Challenge.class);
                    //showDialog("Challenge Details", challengeJson);
                }

                Media media = new Media();
                media.setExtra("challenger");
                media.setType(AppState.currentEditChallenge.getRequest_media_type());
                media.setMedia_source(AppState.currentEditChallenge.getMedia_source());
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.post_challenge_frame_two, MediaUploadFragment.newInstance(media))
                        .addToBackStack("")
                        .commit();
                showPage(1); */
                String introText = "After you upload your file the challenge is submitted automatically. " +
                        "Make sure your challenge follows the terms and rules guiding this game. ";
                MediaUploadFragment.newInstance(media, introText, true).show(getSupportFragmentManager(), "upload media for edit");
            }
        }
    }

    void performExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm close")
                .setMessage("Edit in progress. Are you sure you want leave this page?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

    /**
     * Show media edit view or media upload View
     *
     * @param pageNo either 0 or 1
     */
    void showPage(int pageNo) {
        binding.postChallengeFrameScrollContainer.setVisibility(pageNo == 0 ? View.VISIBLE : View.GONE);
        binding.postChallengeFrameTwo.setVisibility(pageNo == 1 ? View.VISIBLE : View.GONE);
    }
}
