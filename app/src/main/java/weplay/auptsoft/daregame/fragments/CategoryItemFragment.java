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

import com.google.gson.Gson;

import java.util.ArrayList;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.FragmentCategoryItemBinding;
import weplay.auptsoft.daregame.databinding.FragmentChallengesListBinding;
import weplay.auptsoft.daregame.models.Category;
import weplay.auptsoft.daregame.services.RESTUtil;

/**
 * Created by Andrew on 25.3.19.
 */

public class CategoryItemFragment extends Fragment {
    Category category;
    FragmentCategoryItemBinding binding;

    final static String CATEGORY = "category";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jsonString = getArguments().getString(CATEGORY);
        category = new Gson().fromJson(jsonString, Category.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_item, container, false);
        View view = binding.getRoot();

        ((AppCompatActivity)getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.category_frame, getChallengesListFragment(category))
                .commit();
        return view;
    }

    public static CategoryItemFragment newInstance(Category category) {
        CategoryItemFragment categoryItemFragment = new CategoryItemFragment();

        String jsonString = new Gson().toJson(category);

        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, jsonString);

        categoryItemFragment.setArguments(bundle);

        return categoryItemFragment;
    }

    public ChallengesListFragment getChallengesListFragment(Category category)  {
        ArrayList<RESTUtil.SearchQueryItem> searchQueryItems = new ArrayList<>();
        searchQueryItems.add(new RESTUtil.SearchQueryItem("category_id", "=", ""+category.getId()));

        ChallengesListFragment challengesListFragment = ChallengesListFragment.newInstance(searchQueryItems);
        return challengesListFragment;
    }
}
