package weplay.auptsoft.daregame.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.fragments.LoginFragment;
import weplay.auptsoft.daregame.fragments.RegisterFragment;

/**
 * Created by Andrew on 15.3.19.
 */

public class AuthenticateActivity extends AppCompatActivity {
    public static final String PAGE_KEY = "page_key";
    int page;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        page = getIntent().getIntExtra(PAGE_KEY, 0);
        if(page == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.autheticate_main_frame, new LoginFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.autheticate_main_frame, new RegisterFragment()).commit();
        }
    }
}
