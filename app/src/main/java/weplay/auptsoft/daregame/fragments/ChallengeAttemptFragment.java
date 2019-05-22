package weplay.auptsoft.daregame.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.FragmentChallengeAttemptBinding;
import weplay.auptsoft.daregame.presenters.ChallengeAttemptFragmentPresenter;

public class ChallengeAttemptFragment extends DialogFragment {
    FragmentChallengeAttemptBinding binding;

    ChallengeAttemptFragmentPresenter presenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_attempt, container, false);
        View view = binding.getRoot();
        presenter = new ChallengeAttemptFragmentPresenter(this);

        binding.setPresenter(presenter);

        return view;
    }
}
