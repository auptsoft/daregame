package weplay.auptsoft.daregame.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.util.ArrayList;


import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.databinding.ItemChallengeSimpleBinding;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.view_models.ChallengeViewModel;

/**
 * Created by Andrew on 21.3.19.
 */

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeSimpleViewHolder> {
    private ArrayList<Challenge> challenges;
    private int resource_layout;
    private Context context;

    public ChallengeAdapter(Context context, ArrayList<Challenge> challenges, int resource_layout) {
        this.context = context;
        this.challenges = challenges;
        this.resource_layout = resource_layout;
    }

    @NonNull
    @Override
    public ChallengeSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, resource_layout, parent, false);

        return new ChallengeSimpleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeSimpleViewHolder holder, int position) {
        ChallengeViewModel challengeViewModel = new ChallengeViewModel(challenges.get(position), context);
        holder.bind(challengeViewModel);
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    class ChallengeSimpleViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;
        public ChallengeSimpleViewHolder(ViewDataBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            //NavigationTabStrip tabStrip = new NavigationTabStrip().v
        }

        public void bind(Object object) {
            binding.setVariable(BR.item, object);
            binding.executePendingBindings();
        }
    }
}
