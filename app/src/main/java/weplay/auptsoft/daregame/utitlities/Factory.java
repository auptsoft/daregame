package weplay.auptsoft.daregame.utitlities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.response.PaginateResponse;

/**
 * Created by Andrew on 22.3.19.
 */

public class Factory {

    public interface OnChallengeResponseListener {
        void onChallengeResponse(PaginateResponse<Challenge> challengePaginateResponse);
        void onError(String msg, String errorDetails);
    }

    public static void searchChallenges(ArrayList<RESTUtil.SearchQueryItem> searchQueryItems, final OnChallengeResponseListener onChallengeResponseListener) {
        RESTUtil.search(AppState.BASE_URL, AppState.INITIAL_PATH + "/search", searchQueryItems, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.message().equals("OK")) {
                    try {
                        String jsonString = response.body().string();
                        Type type = new TypeToken<PaginateResponse<Challenge>>(){}.getType();
                        PaginateResponse<Challenge> paginateResponse = new Gson().fromJson(jsonString, type);
                        onChallengeResponseListener.onChallengeResponse(paginateResponse);
                    } catch (Exception e) {
                        onChallengeResponseListener.onError(e.getMessage(), e.getCause().getMessage());
                        e.printStackTrace();
                    }
                } else {
                    onChallengeResponseListener.onError("failed", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onChallengeResponseListener.onError("failed", t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
