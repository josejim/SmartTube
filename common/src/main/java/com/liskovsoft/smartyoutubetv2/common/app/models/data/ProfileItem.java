package com.liskovsoft.smartyoutubetv2.common.app.models.data;

import android.graphics.drawable.Drawable;

import com.liskovsoft.mediaserviceinterfaces.oauth.Account;

public class ProfileItem {
    public final Account account;
    public final String title;
    public final Drawable icon;

    public ProfileItem(Account account, String title, Drawable icon) {
        this.account = account;
        this.title = title;
        this.icon = icon;
    }
}
