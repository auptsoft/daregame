package weplay.auptsoft.daregame.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.FragmentPostChallengeBinding;
import weplay.auptsoft.daregame.models.Category;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.view_models.ChallengeViewModel;

public class PostChallengeFragment extends Fragment {

    FragmentPostChallengeBinding binding;

    ChallengeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_challenge, container, false);
        View  view = binding.getRoot();

        AppState.currentEditChallenge = new Challenge();
        viewModel = new ChallengeViewModel(AppState.currentEditChallenge, getContext());

        binding.setViewModel(viewModel);

        initializeView();

        return view;
    }


    void initializeView() {
        ArrayAdapter<String> arrayAdapter;
        if(AppState.categories != null) {
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Category.getTitles(AppState.categories));
        } else {
            String[] strings = new String[]{"Sport", "Romance", "Food", "Technology", "Academic", "Games"};
            AppState.categories = new ArrayList<>();

            for (String str : strings) AppState.categories.add(new Category(str, str+" description"));

            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, strings);
        }

        binding.challengeSelectCategory.setAdapter(arrayAdapter);

        AppState.currentEditChallenge.setCategory(AppState.categories.get(0));
        AppState.currentEditChallenge.setCategory_id(AppState.categories.get(0).getId());
        AppState.currentEditChallenge.setChallenger_id(AppState.user.getId());
        binding.challengeSelectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AppState.currentEditChallenge.setCategory(AppState.categories.get(i));
                AppState.currentEditChallenge.setCategory_id(AppState.categories.get(i).getId()+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
