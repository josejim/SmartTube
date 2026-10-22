package com.liskovsoft.smartyoutubetv2.common.app.models.data;

public class ProfileAction {
    public static final int ADD = 0;
    public static final int REMOVE = 1;

    public final int type;
    public final String title;

    public ProfileAction(int type, String title) {
        this.type = type;
        this.title = title;
    }
}
