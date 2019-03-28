package weplay.auptsoft.daregame.presenters;

import android.databinding.BaseObservable;

import java.util.ArrayList;

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

    //public static

    public PaginateResponse<Challenge> getPaginateResponse() {
        return paginateResponse;
    }

    public void setPaginateResponse(PaginateResponse<Challenge> paginateResponse) {
        this.paginateResponse = paginateResponse;
    }
}
