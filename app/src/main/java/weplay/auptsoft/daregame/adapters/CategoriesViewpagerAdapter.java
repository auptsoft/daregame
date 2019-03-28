package weplay.auptsoft.daregame.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import weplay.auptsoft.daregame.fragments.CategoryItemFragment;
import weplay.auptsoft.daregame.fragments.ChallengesListFragment;
import weplay.auptsoft.daregame.models.Category;
import weplay.auptsoft.daregame.services.RESTUtil;

/**
 * Created by Andrew on 25.3.19.
 */

public class CategoriesViewpagerAdapter extends FragmentStatePagerAdapter {
    private List<Category> categories = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    public CategoriesViewpagerAdapter(FragmentManager fm, List<Category> categories) {
        super(fm);
        this.categories = categories;
        this.titles = Category.getTitles((ArrayList<Category>)categories);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        ArrayList<RESTUtil.SearchQueryItem> searchQueryItems = new ArrayList<>();
        searchQueryItems.add(new RESTUtil.SearchQueryItem("category_id", "=", ""+categories.get(i).getId()));
        ChallengesListFragment challengesListFragment = ChallengesListFragment.newInstance(searchQueryItems);
        return challengesListFragment;

        //return CategoryItemFragment.newInstance(categories.get(i));
    }

    @Override
    public int getCount() {
        return categories.size();
    }
}
