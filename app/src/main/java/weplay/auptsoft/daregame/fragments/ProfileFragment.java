package weplay.auptsoft.daregame.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.adapters.GFragmentAdapter;
import weplay.auptsoft.daregame.databinding.FragmentProfileBinding;
import weplay.auptsoft.daregame.presenters.ProfileFragmentPresenter;
import weplay.auptsoft.daregame.services.RESTUtil;

/**
 * Created by Andrew on 15.3.19.
 */

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    ProfileFragmentPresenter presenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        presenter = new ProfileFragmentPresenter((AppCompatActivity)getActivity(), AppState.user);
        binding.setPresenter(presenter);

        View view = binding.getRoot();

        showChallenges();

        return view;
    }

    void showChallenges() {
        ArrayList<RESTUtil.SearchQueryItem> searchQueryItems = new ArrayList<>();
        searchQueryItems.add(new RESTUtil.SearchQueryItem("challenger_id", "=", ""+AppState.user.getId()));
        ChallengesListFragment challengesListFragment = ChallengesListFragment.newInstance(searchQueryItems);

        ArrayList<RESTUtil.SearchQueryItem> searchQueryItems2 = new ArrayList<>();
        searchQueryItems.add(new RESTUtil.SearchQueryItem("challenged_id", "=", ""+AppState.user.getId()));
        ChallengesListFragment challengesListFragment2 = ChallengesListFragment.newInstance(searchQueryItems2);

        ChallengesListFragment[] clist = {challengesListFragment, challengesListFragment2};
        String[] titles = {"CHALLENGES POSTED", "CHALLENGES TAKEN"};

        GFragmentAdapter fragmentAdapter = new GFragmentAdapter(getChildFragmentManager(), clist, titles);
        binding.challengesViewpager.setAdapter(fragmentAdapter);

        binding.challengesTab.setupWithViewPager(binding.challengesViewpager);
    }
}
