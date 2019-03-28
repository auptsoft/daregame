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
import weplay.auptsoft.daregame.databinding.FragmentLoginBinding;
import weplay.auptsoft.daregame.models.User;
import weplay.auptsoft.daregame.view_models.UserViewModel;

/**
 * Created by Andrew on 18.3.19.
 */

public class LoginFragment extends Fragment {

    FragmentLoginBinding fragmentLoginBinding;
    UserViewModel userViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        View view = fragmentLoginBinding.getRoot();

        User user = new User();
        userViewModel = new UserViewModel(user, "", getActivity());
        userViewModel.setLoading(false);

        fragmentLoginBinding.setUserViewModel(userViewModel);

        return view;
    }
}
