package weplay.auptsoft.daregame.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import es.dmoral.toasty.Toasty;
import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.BR;
import weplay.auptsoft.daregame.fragments.MediaUploadFragment;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.models.Media;

public class ChallengeAttemptFragmentPresenter extends BaseObservable {
    private DialogFragment dialogFragment;
    private String title;
    private String introText;

    private String detailHtml;

    private Challenge challenge;

    public ChallengeAttemptFragmentPresenter(DialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }

    public DialogFragment getDialogFragment() {
        return dialogFragment;
    }

    public void setDialogFragment(DialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }

    @Bindable
    public String getDetailHtml() {
        return detailHtml;
    }

    public void setDetailHtml(String detailHtml) {
        this.detailHtml = detailHtml;
        notifyPropertyChanged(BR.detailHtml);
    }


    @Bindable
    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        notifyPropertyChanged(BR.challenge);
    }

    @BindingAdapter("android:tag")
    public static void setChallenge(WebView webView, Challenge challenge) {
        //webView.loadUrl(AppState.BASE_URL);

        webView.loadData(getAttemptDetails(AppState.currentChallenge),"text/html", null);
    }

    public void startAction() {
        Media media = new Media();
        media.setExtra("challenged");
        media.setType(AppState.currentChallenge.getAccept_media_type());
        media.setMedia_source(AppState.currentChallenge.getMedia_source());

        Toasty.info(dialogFragment.getContext(), media.getType()+" "+media.getMedia_source()).show();

        MediaUploadFragment.newInstance(media, "", false)
                .show(((AppCompatActivity) dialogFragment.getContext()).getSupportFragmentManager(), "upload media for attempt");
        dialogFragment.dismiss();
    }

    private static String getAttemptDetails(Challenge challenge) {
        String style = "";
        String html = "<html> " +
                "<head>" +
                "</head>" +
                    "<body>" +
                        "<center><h1>Before You Continue...</h1></center>"+
                        "<p> Make sure you have read and understood the description and the "+challenge.getRequest_media_type()+"" +
                        " before your continue. <br /> </p>"+
                        "<p>To win this challenge, upload  <b>"+
                            challenge.getAccept_media_type() +" </b> as described in by the challenger"+
                        "</p>"+
                        "<p> Important: "+
                        (challenge.getMedia_source().equals("live") ?
                                "Your "+challenge.getAccept_media_type()+" must be recorded/captured immediately with this app "
                                : "Your "+challenge.getAccept_media_type()+ " can be uploaded your gallery or file manager")+
                        "</p>"+
                        "<p>If you get this challenge correctly, you will win <center><h1>$"+challenge.getPrice()+"</h1></center>  </p>"+
                         (!challenge.isFree_attempt()? "You lose nothing for not getting it right...":
                        "You lose the amount stated above for not getting it right")+
                        "</p>"+
                        "<br />" +
                        "<center><h2>Goodluck </h2><center>"+
                    "</body>" +
                "</html>";

        return html;
    }
}
