package weplay.auptsoft.daregame.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import weplay.auptsoft.daregame.BuildConfig;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.ItemChallengeWideBinding;
import weplay.auptsoft.daregame.databinding.ItemChallengeWideNewBinding;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.view_models.ChallengeViewModel;

public class ChallengeAdapterWide extends RecyclerView.Adapter<ChallengeAdapterWide.ChallengeViewHolder> {
    private ArrayList<Challenge> challengeArrayList;
    private Context context;

    public ChallengeAdapterWide(ArrayList<Challenge> challengeArrayList, Context context) {
        this.challengeArrayList = challengeArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemChallengeWideNewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_challenge_wide_new, viewGroup, false);

        return new ChallengeViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder challengeViewHolder, int i) {
        ChallengeViewModel challengeViewModel = new ChallengeViewModel(challengeArrayList.get(i), context);
        challengeViewModel.setSharedView(challengeViewHolder.binding.itemSimpleViewId);
        challengeViewModel.setSharedView(challengeViewHolder.binding.itemSimpleViewId);
        challengeViewHolder.binding.setItem(challengeViewModel);
    }

    @Override
    public int getItemCount() {
        return challengeArrayList.size();
    }

    class ChallengeViewHolder extends RecyclerView.ViewHolder {
        ItemChallengeWideNewBinding binding;

        public ChallengeViewHolder(ItemChallengeWideNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
