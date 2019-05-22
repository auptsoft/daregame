package weplay.auptsoft.daregame.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import weplay.auptsoft.daregame.BR;

public class EmailVerificationActivityPresenter extends BaseObservable {
    private boolean loading;
    private boolean errorOccurred;
    private boolean notVerified;

    public EmailVerificationActivityPresenter(boolean loading) {
        this.loading = loading;
        errorOccurred = false;
        notVerified = false;
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    public boolean isErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
        notifyPropertyChanged(BR.errorOccurred);
    }

    @Bindable
    public boolean isNotVerified() {
        return notVerified;
    }

    public void setNotVerified(boolean notVerified) {
        this.notVerified = notVerified;
        notifyPropertyChanged(BR.notVerified);
    }
}
