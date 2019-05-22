package weplay.auptsoft.daregame.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yovenny.videocompress.MediaController;

import java.io.File;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.BuildConfig;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.models.Media;
import weplay.auptsoft.daregame.services.RESTUtil;
import weplay.auptsoft.daregame.services.Utility;
import weplay.auptsoft.daregame.services.response.GeneralResponse;

public class MediaUploadFragmentPresenter extends BaseObservable {
    private DialogFragment dialogFragment;
    Media media = new Media();
    private String introText = "";
    private boolean reloadVisible = true;
    private String buttonText = "upload";
    private String mediaPath = "";
    private boolean loading = false;
    private boolean selectBtnVisible = true;

    private boolean newChallenge = true;

    public MediaUploadFragmentPresenter(DialogFragment dialogFragment, Media media) {
        this.dialogFragment = dialogFragment;
        this.media = media;
    }

    public MediaUploadFragmentPresenter(String introText, boolean reloadVisible, String buttonText) {
        this.introText = introText;
        this.reloadVisible = reloadVisible;
        this.buttonText = buttonText;
        this.loading = false;
    }

    public MediaUploadFragmentPresenter(DialogFragment dialogFragment, Media media, String introText, boolean reloadVisible, String buttonText, String mediaPath, boolean loading) {
        this.dialogFragment = dialogFragment;
        this.media = media;
        this.introText = introText;
        this.reloadVisible = reloadVisible;
        this.buttonText = buttonText;
        this.mediaPath = mediaPath;
        this.loading = loading;
    }

    @Bindable
    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
        notifyPropertyChanged(BR.media);
    }

    @Bindable
    public String getIntroText() {
        return introText;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
        notifyPropertyChanged(BR.introText);
    }

    @Bindable
    public boolean isReloadVisible() {
        return reloadVisible;
    }

    public void setReloadVisible(boolean reloadVisible) {
        this.reloadVisible = reloadVisible;
        notifyPropertyChanged(BR.reloadVisible);
    }

    @BindingAdapter("android:tag")
    public static void setReloadVisible(Button btn, boolean reloadVisible) {
        btn.setVisibility(reloadVisible ? View.VISIBLE : View.GONE);
    }

    @Bindable
    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
        notifyPropertyChanged(BR.buttonText);
    }

    @Bindable
    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
        if (media.getMedia_source().equals("live")) uploadChallengeAndWithMedia();
        notifyPropertyChanged(BR.mediaPath);
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }

//    @BindingAdapter("android:visibility")
//    public static void setLoading(ProgressBar progressBar, boolean loading) {
//        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
//    }

    @Bindable
    public boolean isSelectBtnVisible() {
        return selectBtnVisible;
    }

    public void setSelectBtnVisible(boolean selectBtnVisible) {
        this.selectBtnVisible = selectBtnVisible;
        notifyPropertyChanged(BR.selectBtnVisible);
    }

    @Bindable
    public boolean isNewChallenge() {
        return newChallenge;
    }

    public void setNewChallenge(boolean newChallenge) {
        this.newChallenge = newChallenge;
        notifyPropertyChanged(BR.newChallenge);
    }

    public void uploadChallengeAndWithMedia() {
        if (newChallenge) {
            uploadNewChallenge();
        } else {
            uploadAttempt();
        }
    }

    private void uploadAttempt() {
        //File mediaFile = new File(mediaPath);
        media.setExtra("challenged");
        setLoading(true);
        uploadMediaToServer("/challenge/storeAttempt/" + AppState.currentChallenge.getId(), mediaPath, media, media, new Callback<GeneralResponse<String>>() {
            @Override
            public void onResponse(Call<GeneralResponse<String>> call, Response<GeneralResponse<String>> response) {
                if (response.message().equals("OK")) {
                    Toasty.success(dialogFragment.getContext(), response.body().getMessage()).show();

                    AppState.setDirty(2);

                    dialogFragment.dismiss();
                } else {
                    if (BuildConfig.DEBUG) {
                        Toasty.error(dialogFragment.getContext(), response.message()).show();
                    } else {
                        Toasty.error(dialogFragment.getContext(), "Error occurred while uploading").show();
                    }
                    setIntroText("Could not upload media");
                    setButtonText("Retry");
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<GeneralResponse<String>> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Toasty.error(dialogFragment.getContext(), t.getMessage()).show();
                } else {
                    Toasty.error(dialogFragment.getContext(), "Error occurred while upload").show();
                }
                setIntroText("Could not upload media");
                setButtonText("Retry");
                setLoading(false);
            }
        });
    }

    public void uploadNewChallenge() {
        File mediaFile = new File(mediaPath);
        String fileName = mediaFile.getName();

        /*media.setFile_name(fileName);  // not needed. Already set in the server site;
        media.setOwner_type("challenge"); */

        AppState.currentEditChallenge.getChallenger_media().add(media);
        setLoading(true);
        setIntroText("Please wait... Upload in progress");
        uploadMediaToServer("/uploadNewChallenge", mediaPath, media, AppState.currentEditChallenge, new Callback<GeneralResponse<String>>() {
            @Override
            public void onResponse(Call<GeneralResponse<String>> call, Response<GeneralResponse<String>> response) {
                setLoading(false);
                if (BuildConfig.DEBUG) {
                    Toast.makeText(dialogFragment.getContext(), response.message() + " " + (response.body() == null ? "" : response.body().getData()), Toast.LENGTH_LONG).show();
                }

                if (response.message().equals("OK")) {
                    Toasty.success(dialogFragment.getContext(), "Uploaded successfully").show();
                    setIntroText("Media Uploading Successfully");

                    AppState.setDirty(3);

                    dialogFragment.dismiss();
                    dialogFragment.getActivity().finish();
                } else {
                    String data = "data=" + (new Gson().toJson(AppState.currentEditChallenge, Challenge.class));
                    Utility.useWebView(dialogFragment.getContext(), AppState.BASE_URL + AppState.INITIAL_PATH + "/uploadNewChallenge", "POST", data);

                    setIntroText("Could not upload media");
                    setButtonText("Retry");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse<String>> call, Throwable t) {
                setLoading(false);
                if (BuildConfig.DEBUG) {
                    Toasty.error(dialogFragment.getContext(), t.getMessage()).show();
                }
                setIntroText("Could not upload media");
                setButtonText("Retry");
            }
        });
    }

    public <T> void uploadMediaToServer(final String urlPath, final String pathToMediaFile, final Media media, final T customData, final Callback<GeneralResponse<String>> responseCallback) {

        /*Media media = new Media(pathToMediaFile, "Image From Android",
                "video", "Description of video", mediaFile.getName(), 1,  "challenge"); */
        setLoading(true);
        Toasty.info(dialogFragment.getContext(), "Compressing video").show();

        if (media.getType().equals("video")) {
            compressVideo(pathToMediaFile, getFileDestinationPath(), new OnCompressResult() {
                @Override
                public void onResult(String path) {
                    RESTUtil.uploadMedia(AppState.BASE_URL, AppState.INITIAL_PATH + urlPath, path, media.getType(), customData, responseCallback);
                }

                @Override
                public void onError(String errorString) {
                    setLoading(false);
                    Toasty.error(dialogFragment.getContext(), "Could not compress video. Make sure you have enough space in your device").show();
                }
            });
        } else {
            RESTUtil.uploadMedia(AppState.BASE_URL, AppState.INITIAL_PATH + urlPath, pathToMediaFile, media.getType(), customData, responseCallback);
        }
    }


    private void compressVideo(final String inPath, final String outPath, final OnCompressResult onCompressResult) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MediaController.getInstance().convertVideo(inPath, outPath)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onCompressResult.onResult(outPath);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onCompressResult.onError("Could not compress");
                        }
                    });

                }
            }
        }).start();
    }

    interface OnCompressResult {
        void onResult(String path);

        void onError(String errorString);
    }

    private String getFileDestinationPath() {
        String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment+"/video/");
        if(!directoryFolder.exists()) {
            directoryFolder.mkdir();
        }
        return filePathEnvironment+"/video/"+generatedFilename+".mp4";
    }
}