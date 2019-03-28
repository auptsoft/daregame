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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.adapters.CategoriesViewpagerAdapter;
import weplay.auptsoft.daregame.databinding.FragmentCategoriesBinding;
import weplay.auptsoft.daregame.models.Category;
import weplay.auptsoft.daregame.presenters.CategoriesPresenter;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.Utility;
import weplay.auptsoft.daregame.services.request.GeneralRequest;
import weplay.auptsoft.daregame.services.response.GeneralResponse;

/**
 * Created by Andrew on 15.3.19.
 */

public class CategoriesFragment extends Fragment {
    FragmentCategoriesBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false);
        CategoriesPresenter presenter = new CategoriesPresenter(true);
        binding.setPresenter(presenter);

        View view = binding.getRoot();

        initializeTest();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //
    }

    public void initialize() {
        ArrayList<RESTUtil.SearchQueryItem> searchQueryItems = new ArrayList<>();
        searchQueryItems.add(new RESTUtil.SearchQueryItem("deleted_at", "=", "null"));
        RESTUtil.search(AppState.BASE_URL, AppState.INITIAL_PATH + "/search", searchQueryItems, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.message().equals("OK")) {
                    try {
                        String responseString = response.body().string();
                        Type type = new TypeToken<GeneralRequest<List<Category>>>(){}.getType();
                        GeneralResponse<List<Category>> categoryGeneralResponse = new Gson().fromJson(responseString, type);
                        List<Category> categories = categoryGeneralResponse.getData();

                        CategoriesViewpagerAdapter categoriesViewpagerAdapter =
                                new CategoriesViewpagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager(), categories);
                        binding.categoriesViewPager.setAdapter(categoriesViewpagerAdapter);
                        //binding.categoriesTabsStrip.setViewPager(binding.categoriesViewPager);
                        binding.categoriesTabs.setupWithViewPager(binding.categoriesViewPager);
                        binding.getPresenter().setLoading(false);
                    } catch (Exception e) {
                        Toasty.warning(getContext(), e.getMessage()).show();
                    }
                } else {
                    Toasty.warning(getContext(), response.message()).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(getContext(), t.getMessage()).show();
            }
        });
    }

    void initializeTest() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("Sport", "About sport here"));
        categories.add(new Category("Romance", "About romance here"));
        categories.add(new Category("Food", "About food here"));
        categories.add(new Category("Technology", "About technology here"));
        categories.add(new Category("Academic", "About academic here"));
        categories.add(new Category("Games", "About games here"));


        CategoriesViewpagerAdapter categoriesViewpagerAdapter =
                new CategoriesViewpagerAdapter(/*((AppCompatActivity)getActivity()).getSupportFragmentManager() */
                        getChildFragmentManager() , categories);
        binding.categoriesViewPager.setAdapter(categoriesViewpagerAdapter);
        //binding.categoriesTabsStrip.setTitles(getStringArray(Category.getTitles(categories)));
        //binding.categoriesTabsStrip.setViewPager(binding.categoriesViewPager);
        binding.categoriesTabs.setupWithViewPager(binding.categoriesViewPager);
        binding.getPresenter().setLoading(false);
    }

    String[] getStringArray(ArrayList<String> arrayList) {
        String[] strings = new String[arrayList.size()];
        for (int i=0; i<strings.length; i++) {
            strings[i] = arrayList.get(i);
        }

        return strings;
    }
}
