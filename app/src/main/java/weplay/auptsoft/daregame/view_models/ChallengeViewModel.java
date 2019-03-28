package weplay.auptsoft.daregame.view_models;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v7.app.AppCompatActivity;

import weplay.auptsoft.daregame.activities.ChallengeDetialsActivity;
import weplay.auptsoft.daregame.fragments.ChallengesListFragment;
import weplay.auptsoft.daregame.models.Challenge;

/**
 * Created by Andrew on 21.3.19.
 */

public class ChallengeViewModel extends BaseObservable{
    private Challenge challenge;
    private Context context;

    private String printablePrice;
    private String printableLikes;

    public ChallengeViewModel(Challenge challenge, Context context) {
        this.challenge = challenge;
        this.context = context;
    }

    @Bindable
    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        //notifyPropertyChanged();
    }

    void viewDetails(Challenge challenge) {
        Intent intent = new Intent(context, ChallengeDetialsActivity.class);
        ((AppCompatActivity)context).startActivity(intent);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Bindable
    public String getPrintablePrice() {
        return printablePrice;
    }

    public void setPrintablePrice(String printablePrice) {
        this.printablePrice = printablePrice;
    }

    @Bindable
    public String getPrintableLikes() {
        return printableLikes;
    }

    public void setPrintableLikes(String printableLikes) {
        this.printableLikes = printableLikes;
    }
}
