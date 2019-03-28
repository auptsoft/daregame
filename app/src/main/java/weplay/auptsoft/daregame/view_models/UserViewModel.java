package weplay.auptsoft.daregame.view_models;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.BuildConfig;
import weplay.auptsoft.daregame.MainApplication;
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.activities.MainActivity;
import weplay.auptsoft.daregame.fragments.LoginFragment;
import weplay.auptsoft.daregame.fragments.RegisterFragment;
import weplay.auptsoft.daregame.models.User;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.response.GeneralResponse;

/**
 * Created by Andrew on 18.3.19.
 */

public class UserViewModel extends BaseObservable {
    private User user;
    private String confirmPassword;

    private String emailErrorMsg;
    private String usernameErrorMsg;
    private String passwordErrorMsg;
    private String confirmPasswordErrorMsg;

    private Activity activity;
    private boolean loading;

    public UserViewModel(User user, String confirmPassword, Activity activity) {
        this.user = user;
        this.confirmPassword = confirmPassword;
        this.activity = activity;
    }

    public void gotoRegister() {
        if (((AppCompatActivity) activity).getSupportFragmentManager().getBackStackEntryCount() > 0) {
            ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
        } else {
            ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.autheticate_main_frame, new RegisterFragment())
                    .addToBackStack("")
                    .commit();
        }
    }

    public void gotoLogin() {
        if (((AppCompatActivity) activity).getSupportFragmentManager().getBackStackEntryCount() > 0) {
            ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
        } else {
            ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.autheticate_main_frame, new LoginFragment())
                    .addToBackStack("")
                    .commit();
        }
    }

    public void login(User user) {
        setLoading(true);
        if (BuildConfig.DEBUG) {
            //Toasty.info(activity, user.getEmail() + " " + user.getPassword()).show();
        }
        RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/login", RESTUtil.Method.POST, user, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                setLoading(false);
                //Toasty.info(activity, response.message()).show();
                //if (BuildConfig.DEBUG) Toasty.info(activity, response.message()).show();
                if(!response.message().equals("OK")) {
                    Toasty.error(activity, response.message()).show();
                    return;
                }
                try {
                    String responseString = response.body().string();
                    //Toasty.info(activity, responseString).show();
                    Gson gson = new Gson();
                    Type type = new TypeToken<GeneralResponse<String>>() {}.getType();
                    GeneralResponse<String> userGeneralResponse = gson.fromJson(responseString, type);
                    //if(BuildConfig.DEBUG) Toasty.success(activity, userGeneralResponse.getMessage()).show();
                    /*if (userGeneralResponse.getMessage().equals("success")) {
                        Toasty.success(activity, "You are authenticated").show();
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    } else {
                        Toasty.warning(activity, userGeneralResponse.getData()).show();
                    } */
                    //Toasty.info(activity, responseString).show();
                    if(userGeneralResponse.getMessage().equals("success")) {
                        Toasty.success(activity, userGeneralResponse.getMessage()).show();
                        ((MainApplication)activity.getApplication()).saveApiKey(userGeneralResponse.getData());
                        getProfile();
                    } else {
                        Toasty.warning(activity, userGeneralResponse.getMessage()+" "+userGeneralResponse.getData()).show();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(activity, t.getMessage() + ". Try again").show();
                setLoading(false);
            }
        });
    }

    public void register(User user) {
        boolean valid = validateEmail(user.getEmail())
                && validateUsername(user.getUsername())
                && validatePassword(user.getPassword())
                && validateConfirmPassword(user.getPassword(), getConfirmPassword());
        //Toasty.error(activity, emailErrorMsg).show();
        if (valid) {
            setLoading(true);
            RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/register", RESTUtil.Method.POST, user, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    setLoading(false);
                    if(!response.message().equals("OK")){
                        Toasty.error(activity, response.message()).show();
                        return;
                    }
                    try {

                        Type type = new TypeToken<GeneralResponse<String>>(){}.getType();
                        GeneralResponse<String> userGeneralResponse = new Gson().fromJson(response.body().string(), type);

                        if(userGeneralResponse.getMessage().equals("success")) {
                            Toasty.success(activity, userGeneralResponse.getMessage()).show();
                            ((MainApplication)activity.getApplication()).saveApiKey(userGeneralResponse.getData());
                            getProfile();
                        } else {
                            Toasty.warning(activity, userGeneralResponse.getMessage()+" "+userGeneralResponse.getData()).show();
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        Toasty.info(activity, response.message()).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toasty.info(activity, t.getMessage()).show();
                    setLoading(false);
                }
            });
        }
    }

    public boolean validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);

        if (matcher.find()) {
            setEmailErrorMsg("");
            return true;
        } else {
            setEmailErrorMsg("Invalid email");
            return false;
        }
    }

    public boolean validateUsername(User username) {
        return true;
    }

    public boolean validatePassword(String password) {
        if (password.length() < 6) {
             setPasswordErrorMsg("Password too short. At least 6 characters is required");
             return false;
        } else {
            setPasswordErrorMsg("");
            return true;
        }
    }

    public boolean validateConfirmPassword(String password, String confirmPassword) {
        if (password.equals(password)) {
            setConfirmPasswordErrorMsg("");
            return true;
        } else {
             setConfirmPasswordErrorMsg("Password do not match");
             return false;
        }
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    @BindingAdapter("android:visibility")
    public static void setLoading(AVLoadingIndicatorView avLoadingIndicatorView, boolean loading) {
        //this.loading = loading;
        avLoadingIndicatorView.setVisibility(loading ? View.VISIBLE : View.GONE);
        //notifyPropertyChanged(BR.loading);
    }

    @BindingAdapter("android:visibility")
    public static void setLoading(Button button, boolean loading) {
        button.setVisibility(!loading ? View.VISIBLE : View.GONE);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }


    @Bindable
    public String getEmailErrorMsg() {
        return emailErrorMsg;
    }

    public void setEmailErrorMsg(String emailErrorMsg) {
        this.emailErrorMsg = emailErrorMsg;
        notifyPropertyChanged(BR.emailErrorMsg);
    }

    @Bindable
    public String getUsernameErrorMsg() {
        return usernameErrorMsg;
    }

    public void setUsernameErrorMsg(String usernameErrorMsg) {
        this.usernameErrorMsg = usernameErrorMsg;
        notifyPropertyChanged(BR.usernameErrorMsg);
    }

    @Bindable
    public String getPasswordErrorMsg() {
        return passwordErrorMsg;
    }

    public void setPasswordErrorMsg(String passwordErrorMsg) {
        this.passwordErrorMsg = passwordErrorMsg;
        notifyPropertyChanged(BR.passwordErrorMsg);
    }

    @Bindable
    public String getConfirmPasswordErrorMsg() {
        return confirmPasswordErrorMsg;
    }

    public void setConfirmPasswordErrorMsg(String confirmPasswordErrorMsg) {
        this.confirmPasswordErrorMsg = confirmPasswordErrorMsg;
        notifyPropertyChanged(BR.confirmPasswordErrorMsg);
    }

    @BindingAdapter("android:tag")
    public static void setEmailErrorMsg(TextInputLayout textInputLayout, String emailErrorMsg) {
        //textInputLayout.setErrorEnabled(true);
        //textInputLayout.setError(emailErrorMsg);
    }

    public void getProfile() {
        setLoading(true);
        RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/profile", RESTUtil.Method.GET, "", new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                setLoading(false);
                Toasty.info(activity, response.message()).show();
                //Toasty.info(activity, call.request().header("Authorization")).show();
                //return;
                try {
                    Type type = new TypeToken<GeneralResponse<User>>(){}.getType();
                    GeneralResponse<User> userGeneralResponse = new Gson().fromJson(response.body().string(), type);
                    Toasty.info(activity, response.message()).show();
                    if(userGeneralResponse.getMessage().equals("success")) {
                        if(((MainApplication)activity.getApplication()).loginUser(userGeneralResponse.getData())) {
                            ((MainApplication)activity.getApplication()).disableFirstTimeEvent();
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.putExtra(MainActivity.PAGE_NO_KEY, 0);
                            activity.startActivity(intent);
                        }
                    } else {
                        Toasty.error(activity, userGeneralResponse.getMessage()+": "+userGeneralResponse.getData()).show();
                    }
                } catch (Exception ioe) {
                    Toasty.error(activity, ioe.getMessage()).show();
                    ioe.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setLoading(false);
                Toasty.error(activity, t.getMessage()).show();
                Toasty.info(activity, call.request().header("Authorization")).show();
            }
        });
    }
}