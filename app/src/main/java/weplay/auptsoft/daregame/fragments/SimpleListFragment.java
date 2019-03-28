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

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.adapters.ChallengeAdapter;
import weplay.auptsoft.daregame.databinding.FragmentSimpleListBinding;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.response.PaginateResponse;
import weplay.auptsoft.daregame.utitlities.Factory;

/**
 * Created by Andrew on 21.3.19.
 */

public class SimpleListFragment extends Fragment{

    enum Source {ONLINE, LIST};

    private Source source;
    private ArrayList<RESTUtil.SearchQueryItem> searchQueryItems;
    private ArrayList<Challenge> challenges;

    private ChallengeAdapter challengeAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentSimpleListBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple_list, container, false);
        View view = binding.getRoot();

        fetchAndShowChallenges();

        return view;
    }

    public static SimpleListFragment newInstance(ArrayList<RESTUtil.SearchQueryItem> searchQueryItems) {
        SimpleListFragment simpleListFragment = new SimpleListFragment();
        simpleListFragment.setSource(Source.ONLINE);
        simpleListFragment.setSearchQueryItems(searchQueryItems);

        return simpleListFragment;
    }

    private void fetchAndShowChallenges() {
        Factory.searchChallenges(searchQueryItems, new Factory.OnChallengeResponseListener() {
            @Override
            public void onChallengeResponse(PaginateResponse<Challenge> challengePaginateResponse) {
                challenges = (ArrayList<Challenge>)challengePaginateResponse.getData();
                challengeAdapter = new ChallengeAdapter(getContext(), challenges, R.layout.item_challenge_simple);
                binding.listRecyclerView.setAdapter(challengeAdapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                binding.listRecyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onError(String msg, String errorDetails) {
                Toasty.error(getContext(), msg+": "+errorDetails).show();
            }
        });
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public ArrayList<RESTUtil.SearchQueryItem> getSearchQueryItems() {
        return searchQueryItems;
    }

    public void setSearchQueryItems(ArrayList<RESTUtil.SearchQueryItem> searchQueryItems) {
        this.searchQueryItems = searchQueryItems;
    }

    public ArrayList<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(ArrayList<Challenge> challenges) {
        this.challenges = challenges;
    }
}