package weplay.auptsoft.daregame.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import weplay.auptsoft.daregame.BR;

/**
 * Created by Andrew on 25.3.19.
 */

public class CategoriesPresenter extends BaseObservable {
    private boolean loading;

    public CategoriesPresenter(boolean loading) {
        this.loading = loading;
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @BindingAdapter("android:visibility")
    public static void setLoading(RelativeLayout loadingIndicator, boolean loading) {
        loadingIndicator.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:visibility")
    public static void setLoading(LinearLayout mainView, boolean loading) {
        mainView.setVisibility(!loading ? View.VISIBLE : View.GONE);
    }
}
