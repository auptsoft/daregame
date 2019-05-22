package weplay.auptsoft.daregame.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.FragmentHomeBinding;
import weplay.auptsoft.daregame.presenters.HomeFragmentPresenter;

/**
 * Created by Andrew on 15.3.19.
 */

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    HomeFragmentPresenter presenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();
        presenter = new HomeFragmentPresenter();
        binding.setPresenter(presenter);


        return view;
    }
}
