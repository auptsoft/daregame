package weplay.auptsoft.daregame.adapters;

import java.util.List;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.models.Challenge;

/**
 * Created by Andrew on 22.3.19.
 */

public class SimpleChallengeAdapter extends BaseAdapter {
    List<Challenge> challenges;
    int layoutResourceId;

    public SimpleChallengeAdapter(List<Challenge> challenges, int layoutResourceId) {
        this.challenges = challenges;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    @Override
    public Object getDataAtPosition(int position) {
        return challenges.get(position);
    }

    @Override
    public int getLayoutIdForType(int viewType) {
        return layoutResourceId;
    }
}
