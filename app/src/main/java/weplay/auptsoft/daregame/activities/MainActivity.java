package weplay.auptsoft.daregame.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.ActivityMainBinding;
import weplay.auptsoft.daregame.fragments.CategoriesFragment;
import weplay.auptsoft.daregame.fragments.HomeFragment;
import weplay.auptsoft.daregame.fragments.ProfileFragment;
import weplay.auptsoft.daregame.fragments.WalletFrament;
import weplay.auptsoft.daregame.models.User;

public class MainActivity extends AppCompatActivity {
    public static final String PAGE_NO_KEY = "page_no_key";
    int page;
    List<Fragment> fragments;

    BottomNavigationView navigationView;

    ActivityMainBinding binding;
    int[] navigationIds = {R.id.nav_home, R.id.nav_all_categories, R.id.nav_wallet, R.id.nav_profile};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        page = getIntent().getIntExtra(PAGE_NO_KEY, 0);
        if(!handleFirstTime()) ensureAuthentication();

        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.mainToolbar);

        fragments = initFragments();
        showPage(page);

        handleNavigationMenu();
    }

    @Override
    public void onBackPressed() {
        if(page == 0) {
            super.onBackPressed();
        } else {
            binding.mainNavBar.setSelectedItemId(navigationIds[page]);
            page = 0;
            showPage(0);
        }
    }

    private void handleNavigationMenu() {
        binding.mainNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        page = 0;
                        break;

                    case R.id.nav_all_categories:
                        page = 1;
                        break;

                    case R.id.nav_wallet:
                        page = 2;
                        break;

                    case R.id.nav_profile:
                        page = 3;
                        break;
                }
                showPage(page);
                return true;
            }
        });
    }

    private void showPage(int pg) {
        if (pg == 1) {
            //while (getSupportFragmentManager().getBackStackEntryCount()>0) getSupportFragmentManager().popBackStack();
            //fragments.get(pg).onDestroy();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragments.get(pg))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragments.get(pg))
                    //.addToBackStack("")
                    .commit();
        }
    }

    private List<Fragment> initFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new CategoriesFragment());
        fragmentList.add(new WalletFrament());
        fragmentList.add(new ProfileFragment());

        return fragmentList;
    }

    private boolean handleFirstTime() {
        if (!AppState.isFirstTime) return false;
        Intent intent = new Intent(this, StartupActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private void ensureAuthentication() {
        String userJson = AppState.preferences.getString(AppState.USER_PREF_KEY, null);
        if (userJson == null) startAuthenticationActivity();
        try {
            AppState.user = new Gson().fromJson(userJson, User.class);
        } catch (Exception e) {
            startAuthenticationActivity();
        }
    }

    private void startAuthenticationActivity() {
        Intent intent = new Intent(this, AuthenticateActivity.class);
        startActivity(intent);
        finish();
    }

    /*private void auth() {
        RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/profile", RESTUtil.Method.GET, "", new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    } */
}