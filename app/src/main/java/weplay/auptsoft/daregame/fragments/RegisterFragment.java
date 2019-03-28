package weplay.auptsoft.daregame.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.FragmentRegisterBinding;
import weplay.auptsoft.daregame.models.User;
import weplay.auptsoft.daregame.view_models.UserViewModel;

/**
 * Created by Andrew on 18.3.19.
 */

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;

    UserViewModel userViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container,  false);
        User user = new User();
        userViewModel = new UserViewModel(user, "", getActivity());
        userViewModel.setLoading(false);

        View view = binding.getRoot();

        binding.setUserViewModel(userViewModel);
        //com.wang.avi.indicators.BallScaleRippleMultipleIndicator

        return view;
    }
}
