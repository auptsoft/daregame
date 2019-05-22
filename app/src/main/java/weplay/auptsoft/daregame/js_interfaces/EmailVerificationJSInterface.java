package weplay.auptsoft.daregame.js_interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import weplay.auptsoft.daregame.AppState;
import weplay.auptsoft.daregame.activities.AuthenticateActivity;

public class EmailVerificationJSInterface {

    Context context;

    public EmailVerificationJSInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void gotoLogin() {
        Intent intent = new Intent(context, AuthenticateActivity.class);
        intent.putExtra(AuthenticateActivity.PAGE_KEY, 0);
        ((Activity)context).finish();
        ((Activity)context).startActivity(intent);
    }
}
