package weplay.auptsoft.daregame.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.BoomButtonBuilder;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
//import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import github.hellocsl.*;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.activities.AuthenticateActivity;
import weplay.auptsoft.daregame.adapters.StartupItemAdapter;
import weplay.auptsoft.daregame.databinding.FragmentStartupBinding;
import weplay.auptsoft.daregame.models.StartupItem;
//import weplay.auptsoft.daregame.adapters.*;

/**
 * Created by Andrew on 15.3.19.
 */

public class StartupFragment extends Fragment {
    FragmentStartupBinding fragmentStartupBinding;

    ArrayList<StartupItem> startupItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStartupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_startup, container, false);
        View view = fragmentStartupBinding.getRoot();

        initStartupItems();
        final StartupItemAdapter startupItemAdapter = new StartupItemAdapter(startupItems, getContext());
        fragmentStartupBinding.startUpRecyclerView.setAdapter(startupItemAdapter);

        final GalleryLayoutManager galleryLayoutManager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        galleryLayoutManager.attach(fragmentStartupBinding.startUpRecyclerView);
        galleryLayoutManager.setItemTransformer(new ScaleTransformer());

        fragmentStartupBinding.startUpRecyclerView.setLayoutManager(galleryLayoutManager); //new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        galleryLayoutManager.setCallbackInFling(true);
        galleryLayoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                if (position < startupItems.size() - 1) {
                    //FancyToast.makeText(getActivity(), "Swipe Left to continue", FancyToast.LENGTH_LONG, FancyToast.INFO, R.drawable.security).show();
                    //Toasty.info(getActivity(), "Swipe Left to continue", Fa)
                } else {

                }
            }
        });

        /*fragmentStartupBinding.startUpItemContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(galleryLayoutManager.getCurSelectedPosition() < startupItems.size()-2) {
                    galleryLayoutManager.smoothScrollToPosition(fragmentStartupBinding.startUpRecyclerView, null, galleryLayoutManager.getCurSelectedPosition() + 1);
                }

            }
        }); */

        for (int i = 0; i < 3; i++) {
            HamButton.Builder builder = new HamButton.Builder();
            switch (i) {
                case 0:
                    builder = new HamButton.Builder()
                            .normalText("Login")
                            .subNormalText("For already registered users")
                            .normalImageRes(R.drawable.ic_play_for_work_black_24dp)
                            .listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {
                                    Intent intent = new Intent(getContext(), AuthenticateActivity.class);
                                    intent.putExtra(AuthenticateActivity.PAGE_KEY, 0);
                                    startActivity(intent);
                                }
                            });
                    break;
                case 1:
                    builder = new HamButton.Builder()
                            .normalText("Register")
                            .subNormalText("For new users")
                            .normalImageRes(R.drawable.ic_person_add_black_24dp)
                            .listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {
                                    Intent intent = new Intent(getContext(), AuthenticateActivity.class);
                                    intent.putExtra(AuthenticateActivity.PAGE_KEY, 2);
                                    startActivity(intent);
                                }
                            });
                    break;
                case 2:
                    builder = new HamButton.Builder()
                            .normalText("About")
                            .subNormalText("Know more about this app before you start...")
                            .normalImageRes(R.drawable.ic_info_outline_black_24dp)
                            .listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {

                                }
                            });
                    break;
            }
            //builder.normalColor(Color.BLACK);
            fragmentStartupBinding.startUpItemContinue.addBuilder(builder);

        }

        return view;
    }

    void initStartupItems() {
        startupItems = new ArrayList<>();
        startupItems.add(new StartupItem("Welcome", "You are welcome. Here you can play and earn easily", R.drawable.image1));
        startupItems.add(new StartupItem("Safe & Secured", "This platform is safe and secured.", R.drawable.security));
        startupItems.add(new StartupItem("Privacy", "You privacy is protected. ", R.drawable.image2));
        startupItems.add(new StartupItem("Get Started", "Let the fun begin. Register now", R.drawable.image1));
    }

    class ScaleTransformer implements GalleryLayoutManager.ItemTransformer {
        @Override
        public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
            item.setPivotX(item.getWidth() / 2);
            item.setPivotY(item.getHeight() / 2);
            float scale = 1 - 0.3f * Math.abs(fraction);
            item.setScaleX(scale);
            item.setScaleY(scale);
            float value = 0.5f - Math.abs(0.5f - fraction);
            fragmentStartupBinding.startUpItemContinue.setTranslationY(value * 80);
            fragmentStartupBinding.startUpItemContinue.setRotation((1 - fraction) * 360);
        }
    }
}
