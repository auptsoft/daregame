package weplay.auptsoft.daregame.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.vrgsoft.layoutmanager.RollingLayoutManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.adapters.ChallengeAdapter;
import weplay.auptsoft.daregame.adapters.ChallengeAdapterWide;
import weplay.auptsoft.daregame.databinding.FragmentChallengesListBinding;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.presenters.ChallengeListPresenter;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.response.PaginateResponse;
import weplay.auptsoft.daregame.utitlities.Factory;

/**
 * Created by Andrew on 25.3.19.
 */

public class ChallengesListFragment extends Fragment {
    FragmentChallengesListBinding binding;
    ArrayList<RESTUtil.SearchQueryItem> searchQueryItems;
    ArrayList<Challenge> challenges;

    ChallengeAdapterWide challengeAdapter;

    final static String QUERY = "query";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Type type = new TypeToken<List<RESTUtil.SearchQueryItem>>(){}.getType();
        searchQueryItems = new Gson().fromJson(getArguments().getString(QUERY), type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenges_list, container, false);
        ChallengeListPresenter challengeListPresenter = new ChallengeListPresenter();
        binding.setPresenter(challengeListPresenter);
        View view = binding.getRoot();

        challenges = new ArrayList<>();

        //initializeTestData();  //debug

        //Toasty.info(getDialogFragment(), searchQueryItems.get(0).getValue()).show();

        binding.challengeListRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
            }
        });

        binding.swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initialize();
            }
        });


        initialize();

        return view;
    }

    @Override
    public void onResume() {
        if(AppState.isDirty()) {
            initialize();
            AppState.handleDirty();
        }
        super.onResume();
    }

    public void initialize() {
        binding.recyclerViewShimmer.startShimmerAnimation();
        binding.recyclerViewShimmer.setVisibility(View.VISIBLE);
        binding.challengeListFailedView.setVisibility(View.GONE);
        binding.challengesListContainer.setVisibility(View.GONE);
        Factory.searchChallenges(searchQueryItems, new Factory.OnChallengeResponseListener() {
            @Override
            public void onChallengeResponse(PaginateResponse<Challenge> challengePaginateResponse) {
                challenges = (ArrayList<Challenge>) challengePaginateResponse.getData();
                if(challenges.size() > 0) {
                    challengeAdapter = new ChallengeAdapterWide(challenges, getContext());
                    binding.challengesListRecyclerView.setAdapter(challengeAdapter);


                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    // RollingLayoutManager rollingLayoutManager = new RollingLayoutManager(getDialogFragment());
                    //rollingLayoutManager.
                    binding.challengesListRecyclerView.setLayoutManager(layoutManager);
                    binding.recyclerViewShimmer.setVisibility(View.GONE);
                    binding.recyclerViewShimmer.stopShimmerAnimation();

                    binding.challengeListFailedView.setVisibility(View.GONE);
                    binding.challengesListContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.challengeListFailedView.setTag(View.VISIBLE);
                    binding.getPresenter().setFailedText("No challenge in this category now.");
                    binding.recyclerViewShimmer.stopShimmerAnimation();
                    binding.recyclerViewShimmer.setVisibility(View.GONE);
                }

                binding.swipeRefreshView.setRefreshing(false);
            }

            @Override
            public void onError(String msg, String errorDetails) {
                if(getContext() != null) Toasty.error(getContext(), errorDetails).show();
                binding.recyclerViewShimmer.stopShimmerAnimation();
                binding.recyclerViewShimmer.setVisibility(View.GONE);

                binding.challengeListFailedView.setVisibility(View.VISIBLE);
                binding.getPresenter().setFailedText("Error occured while loading the content. " +
                        "\n Check your internet connection and tap REFRESH try again...");

                binding.swipeRefreshView.setRefreshing(false);
            }
        });
    }

    public static ChallengesListFragment newInstance(ArrayList<RESTUtil.SearchQueryItem> searchQueryItems) {
        ChallengesListFragment challengesListFragment = new ChallengesListFragment();
        //challengesListFragment.setSearchQueryItems(searchQueryItems);

        String jsonString = new Gson().toJson(searchQueryItems);
        Bundle bundle = new Bundle();
        bundle.putString(QUERY, jsonString);
        challengesListFragment.setArguments(bundle);

        return challengesListFragment;
    }

    public ArrayList<RESTUtil.SearchQueryItem> getSearchQueryItems() {
        return searchQueryItems;
    }

    public void setSearchQueryItems(ArrayList<RESTUtil.SearchQueryItem> searchQueryItems) {
        this.searchQueryItems = searchQueryItems;
    }

    private void initializeTestData() {

        Challenge challenge1 = new Challenge("Title2", "description of Challenge2", 2.3, 2, 1);
        Media media = new Media(
                "Media name",
                //"http://192.168.43.32/daregame/storage/app/public/media/android/Vq94JFCMYijHZH1mh1WBIfIUOdhEpGX35jDTUDT2.mp4",
                "http://192.168.43.32/daregame/storage/app/public/media/challenge/audio/R6ov0k6kiyVfEB8aAUCY7rNu1wZbYSt3x9RpjAEo.mpga",
                "audio",
                "image.jpg",
                "challenger",
                1
        );

        ArrayList<Media> challenger_media = new ArrayList<>();
        challenger_media.add(media);
        challenge1.setChallenger_media(challenger_media);

        Challenge challenge2 = new Challenge("Title3", "description of Challenge3", 2.3, 1, 0);
        Media media2 = new Media(
                "Media name",
                "http://192.168.43.32/daregame/storage/app/public/media/android/ZpJig3NiWikSnVlxGmSTThYMcSXkHRASQbwwe2Aw.mp4",
                //"http://192.168.43.32/daregame/storage/app/public/media/challenge/audio/R6ov0k6kiyVfEB8aAUCY7rNu1wZbYSt3x9RpjAEo.mpga",
                "video",
                "vid.mp4",
                "challenger",
                1
        );

        ArrayList<Media> challenger_media2 = new ArrayList<>();
        challenger_media2.add(media2);
        challenge2.setChallenger_media(challenger_media2);

        Challenge challenge3 = new Challenge("Title4", "description of Challenge4", 2.3, 1, 0);
        Media media3 = new Media(
                "Media name",
                //"http://192.168.43.32/daregame/storage/app/public/media/android/Vq94JFCMYijHZH1mh1WBIfIUOdhEpGX35jDTUDT2.mp4",
                "http://192.168.43.32/daregame/storage/app/public/media/challenge/image/ccYPbAI6SrQ74tlWpukgBtcyFXmwzHfvBezRs7hs.png",
                "image",
                "image.jpg",
                "challenger",
                1
        );

        ArrayList<Media> challenger_media3 = new ArrayList<>();
        challenger_media3.add(media3);
        challenge3.setChallenger_media(challenger_media3);

        challenges.add(challenge1);
        challenges.add(challenge2);
        challenges.add(challenge3);
        challenges.add(new Challenge("Title5", "description of Challenge5", 2.3, 1, 0));

        challengeAdapter = new ChallengeAdapterWide(challenges,getContext());
        binding.challengesListRecyclerView.setAdapter(challengeAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.challengesListRecyclerView.setLayoutManager(layoutManager);
        binding.recyclerViewShimmer.stopShimmerAnimation();

    }
}
