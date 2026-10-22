package com.liskovsoft.smartyoutubetv2.tv.ui.profiles;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;

import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.liskovsoft.smartyoutubetv2.common.app.models.data.ProfileAction;

public class ProfileActionPresenter extends Presenter {
    private static final int CARD_WIDTH = 240;   // dp
    private static final int CARD_HEIGHT = 240;  // dp

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView card = new ImageCardView(parent.getContext());
        card.setFocusable(true);
        card.setFocusableInTouchMode(true);
        card.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ProfileAction action = (ProfileAction) item;
        ImageCardView card = (ImageCardView) viewHolder.view;

        card.setTitleText(action.title != null ? action.title : "");
        card.setContentText("");
        card.setMainImage(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ((ImageCardView) viewHolder.view).setMainImage(null);
    }
}
