package com.liskovsoft.smartyoutubetv2.common.app.presenters;

import android.annotation.SuppressLint;
import android.content.Context;

import com.liskovsoft.mediaserviceinterfaces.SignInService;
import com.liskovsoft.mediaserviceinterfaces.oauth.Account;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.base.BasePresenter;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.dialogs.AccountSelectionPresenter;
import com.liskovsoft.smartyoutubetv2.common.app.views.ProfilePickerView;
import com.liskovsoft.smartyoutubetv2.common.app.views.ViewManager;
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager;

import java.util.Collections;
import java.util.List;

public class ProfilePickerPresenter extends BasePresenter<ProfilePickerView> {
    @SuppressLint("StaticFieldLeak")
    private static ProfilePickerPresenter sInstance;

    protected ProfilePickerPresenter(Context context) {
        super(context);
    }

    public static ProfilePickerPresenter instance(Context context) {
        if (sInstance == null) {
            sInstance = new ProfilePickerPresenter(context);
        }

        sInstance.setContext(context);

        return sInstance;
    }

    public void show() {
        ViewManager.instance(getContext()).startView(ProfilePickerView.class);
    }

    public List<Account> getAccounts() {
        List<Account> accounts = getSignInService().getAccounts();

        return accounts != null ? accounts : Collections.emptyList();
    }

    public void select(Account account) {
        AccountSelectionPresenter.instance(getContext()).selectAccount(account);
        close();
    }

    public void removeAccount(Account account) {
        getSignInService().removeAccount(account);
    }

    public void close() {
        if (getView() != null) {
            getView().close();
        }
    }

    public void unhold() {
        sInstance = null;
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unhold();
    }

    private SignInService getSignInService() {
        return YouTubeServiceManager.instance().getSignInService();
    }
}
