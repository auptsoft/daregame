package weplay.auptsoft.daregame.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.fragments.StartupFragment;

/**
 * Created by Andrew on 15.3.19.
 */

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportFragmentManager().beginTransaction().replace(R.id.start_up_main_frame, new StartupFragment()).commit();
    }
}
