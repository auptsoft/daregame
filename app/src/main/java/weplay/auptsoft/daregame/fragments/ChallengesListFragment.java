package weplay.auptsoft.daregame.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.adapters.ChallengeAdapter;
import weplay.auptsoft.daregame.databinding.FragmentChallengesListBinding;
import weplay.auptsoft.daregame.models.Challenge;
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

    ChallengeAdapter challengeAdapter;

    final static String QUERY = "query";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Type type = new TypeToken<List<RESTUtil.SearchQueryItem>>(){}.getType();
        searchQueryItems = new Gson().fromJson(getArguments().getString(QUERY), type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenges_list, container, false);
        View view = binding.getRoot();

        challenges = new ArrayList<>();

        initializeTestData();  //debug

        //Toasty.info(getContext(), searchQueryItems.get(0).getValue()).show();

        return view;
    }

    public void initialize() {
        Factory.searchChallenges(searchQueryItems, new Factory.OnChallengeResponseListener() {
            @Override
            public void onChallengeResponse(PaginateResponse<Challenge> challengePaginateResponse) {
                challenges = (ArrayList<Challenge>) challengePaginateResponse.getData();
                challengeAdapter = new ChallengeAdapter(getContext(), challenges, R.layout.item_challenge_wide);
                binding.challengesListRecyclerView.setAdapter(challengeAdapter);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                binding.challengesListRecyclerView.setLayoutManager(layoutManager);
                binding.recyclerViewShimmer.stopShimmerAnimation();
            }

            @Override
            public void onError(String msg, String errorDetails) {
                Toasty.error(getContext(), errorDetails).show();
                binding.recyclerViewShimmer.stopShimmerAnimation();
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
        challenges.add(new Challenge("Title", "description of Challenge", 2.3, "user1", "user2", 1));
        challenges.add(new Challenge("Title2", "description of Challenge2", 2.3, 2, 1));
        challenges.add(new Challenge("Title3", "description of Challenge3", 2.3, 1, 0));
        challenges.add(new Challenge("Title4", "description of Challenge4", 2.3, 1, 0));
        challenges.add(new Challenge("Title5", "description of Challenge5", 2.3, 1, 0));

        challengeAdapter = new ChallengeAdapter(getContext(), challenges, R.layout.item_challenge_wide);
        binding.challengesListRecyclerView.setAdapter(challengeAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.challengesListRecyclerView.setLayoutManager(layoutManager);
        binding.recyclerViewShimmer.stopShimmerAnimation();

    }
}
