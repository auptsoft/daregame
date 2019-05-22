package weplay.auptsoft.daregame.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.databinding.ActivityEmailVerificationBinding;
import weplay.auptsoft.daregame.js_interfaces.EmailVerificationJSInterface;
import weplay.auptsoft.daregame.models.User;
import weplay.auptsoft.daregame.presenters.EmailVerificationActivityPresenter;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.response.GeneralResponse;
import weplay.auptsoft.daregame.view_models.ChallengeViewModel;
import weplay.auptsoft.daregame.view_models.UserViewModel;

public class EmailVerificationActivity extends AppCompatActivity {
    ActivityEmailVerificationBinding binding;
    EmailVerificationActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url = intent.getDataString();

        Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_verification);
        presenter = new EmailVerificationActivityPresenter(false);
        binding.setPresenter(presenter);

        if (url == null || url.equals("")) {
            presenter.setNotVerified(true);
        }

        WebSettings webSettings = binding.mainWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        binding.mainWebview.addJavascriptInterface(new EmailVerificationJSInterface(this), "Android");
        //binding.mainWebview.loadUrl(AppState.BASE_URL + "daregame/public/verified");
        binding.mainWebview.loadUrl(url);

        binding.mainWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                //super.onReceivedClientCertRequest(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                presenter.setLoading(true);
                presenter.setErrorOccurred(false);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                presenter.setLoading(false);
                presenter.setErrorOccurred(false);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                presenter.setLoading(false);
                presenter.setErrorOccurred(true);
                super.onReceivedError(view, request, error);
            }

        });

        binding.retryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainWebview.reload();
            }
        });

        binding.resendAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setLoading(true);
                presenter.setNotVerified(false);

                RESTUtil.send(AppState.BASE_URL, AppState.INITIAL_PATH + "/email/resend",
                        RESTUtil.Method.GET, "verify", new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        presenter.setLoading(false);
                        if (response.message().equals("OK")) {
                            try {
                                String jsonResponse = response.body().string();
                                Type type = new TypeToken<GeneralResponse<String>>(){}.getType();
                                GeneralResponse<String> generalResponse = new Gson().fromJson(jsonResponse, type);
                                if (generalResponse.getMessage().equals("verified")) {
                                    presenter.setLoading(true);
                                    new UserViewModel(new User(), "", getParent()).getProfile();
                                } else if(generalResponse.getMessage().equals("success")){
                                    presenter.setNotVerified(true);
                                }
                                Toasty.success(getBaseContext(), generalResponse.getData()).show();
                            } catch (IOException ioe) {
                                presenter.setNotVerified(true);
                                if (BuildConfig.DEBUG) {
                                    Toasty.error(getBaseContext(), ioe.getMessage()).show();
                                }
                            }

                        } else {
                            presenter.setNotVerified(true);
                            Toasty.info(getBaseContext(), response.message()+(response.body()==null?response.body():"")).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        presenter.setLoading(false);
                        if (BuildConfig.DEBUG) {
                            Toasty.error(getBaseContext(), t.getMessage()).show();
                        }
                    }
                });
            }
        });

    }
}
