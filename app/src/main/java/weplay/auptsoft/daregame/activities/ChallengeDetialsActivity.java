package weplay.auptsoft.daregame.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.ActivityChallengeDetailsBinding;
import weplay.auptsoft.daregame.fragments.ChallengeDetailsFragment;
import weplay.auptsoft.daregame.fragments.MediaItemFragment;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.presenters.ChallengeDetailsActivityPresenter;

/**
 * Created by Andrew on 26.3.19.
 */

public class ChallengeDetialsActivity extends AppCompatActivity {
    ActivityChallengeDetailsBinding binding;
    ChallengeDetailsActivityPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_details);
        presenter = new ChallengeDetailsActivityPresenter();
        binding.setPresenter(presenter);

        setSupportActionBar(binding.activityPostToolbar);

        binding.activityPostToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAfterTransition();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.challenge_detail_frame, new ChallengeDetailsFragment())
                .commit();
    }
}