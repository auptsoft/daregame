package weplay.auptsoft.daregame.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;

import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.adapters.BaseAdapter;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.services.response.PaginateResponse;
import weplay.auptsoft.daregame.view_models.ChallengeViewModel;

/**
 * Created by Andrew on 25.3.19.
 */

public class ChallengeListPresenter extends BaseObservable {
    private String title;
    private String description;
    private ArrayList<ChallengeViewModel> challengeViewModels;

    private PaginateResponse<Challenge> paginateResponse;

    private String failedText = "";


    //public static

    public PaginateResponse<Challenge> getPaginateResponse() {
        return paginateResponse;
    }

    public void setPaginateResponse(PaginateResponse<Challenge> paginateResponse) {
        this.paginateResponse = paginateResponse;
    }

    @Bindable
    public String getFailedText() {
        return failedText;
    }

    public void setFailedText(String failedText) {
        this.failedText = failedText;
        notifyPropertyChanged(BR.failedText);
    }
}
