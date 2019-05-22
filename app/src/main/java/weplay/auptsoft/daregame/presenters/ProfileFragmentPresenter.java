package weplay.auptsoft.daregame.presenters;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.BuildConfig;
import weplay.auptsoft.daregame.models.User;
import weplay.auptsoft.daregame.models.UserDetails;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.response.GeneralResponse;

/**
 * Created by Andrew on 20.3.19.
 */

public class ProfileFragmentPresenter extends BaseObservable {
    private Activity activity;
    private User user;
    private UserDetails userDetails;

    public ProfileFragmentPresenter(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
    }

    public void retrieveUser() {
        RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/profile", RESTUtil.Method.GET, "", new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody responseBody = response.body();
                    if(responseBody != null) {
                        Type type = new TypeToken<GeneralResponse<User>>(){}.getType();
                        GeneralResponse<User> userGeneralResponse = new Gson().fromJson(responseBody.string(), type);
                        if (userGeneralResponse.getMessage().equals("success")) {
                            user = userGeneralResponse.getData();
                            if(BuildConfig.DEBUG) {
                                Toasty.info(activity, userGeneralResponse.getMessage()).show();
                            }
                        }
                    }
                } catch (IOException ioe) {
                    Toasty.error(activity, ioe.getMessage()).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(activity,t.getMessage()).show();
            }
        });
    }

    public void retrieveUserDetails() {
        RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/profile/details", RESTUtil.Method.GET, "", new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody responseBody = response.body();
                    if(responseBody != null) {
                        Type type = new TypeToken<GeneralResponse<UserDetails>>(){}.getType();
                        GeneralResponse<UserDetails> userGeneralResponse = new Gson().fromJson(responseBody.string(), type);
                        if (userGeneralResponse.getMessage().equals("success")) {
                            userDetails = userGeneralResponse.getData();
                            if(BuildConfig.DEBUG) {
                                Toasty.info(activity, userGeneralResponse.getMessage()).show();
                            }
                        }
                    }
                } catch (IOException ioe) {
                    Toasty.error(activity, ioe.getMessage()).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(activity,t.getMessage()).show();
            }
        });
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Bindable
    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
