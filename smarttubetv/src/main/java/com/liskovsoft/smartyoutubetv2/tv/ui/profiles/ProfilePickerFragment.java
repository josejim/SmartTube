package com.liskovsoft.smartyoutubetv2.tv.ui.profiles;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;

import com.liskovsoft.mediaserviceinterfaces.oauth.Account;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.smartyoutubetv2.common.R;
import com.liskovsoft.smartyoutubetv2.common.app.models.data.ProfileAction;
import com.liskovsoft.smartyoutubetv2.common.app.models.data.ProfileItem;
import com.liskovsoft.smartyoutubetv2.common.app.models.playback.ui.UiOptionItem;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.AppDialogPresenter;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.ProfilePickerPresenter;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.YTSignInPresenter;
import com.liskovsoft.smartyoutubetv2.common.app.views.ProfilePickerView;
import com.liskovsoft.smartyoutubetv2.common.misc.ProfileGridLayout;
import com.liskovsoft.smartyoutubetv2.common.prefs.AccountsData;
import com.liskovsoft.smartyoutubetv2.common.utils.GlideIconFetcher;
import com.liskovsoft.smartyoutubetv2.common.utils.SimpleEditDialog;

import java.util.ArrayList;
import java.util.List;

public class ProfilePickerFragment extends VerticalGridSupportFragment implements ProfilePickerView {
    private static final int ROW_HEIGHT_DP = 300;

    private final ClassPresenterSelector mSelector = new ClassPresenterSelector()
            .addClassPresenter(ProfileItem.class, new ProfileCardPresenter())
            .addClassPresenter(ProfileAction.class, new ProfileActionPresenter());
    private final ArrayObjectAdapter mAdapter = new ArrayObjectAdapter(mSelector);
    private ProfilePickerPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = ProfilePickerPresenter.instance(getContext());
        mPresenter.setView(this);

        setTitle(getString(R.string.profile_picker_title));

        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM);
        gridPresenter.setNumberOfColumns(ProfileGridLayout.COLUMNS);
        setGridPresenter(gridPresenter);
        setAdapter(mAdapter);
        setOnItemViewClickedListener(mOnItemClicked);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter.onViewInitialized();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadProfiles();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.onViewDestroyed();
            mPresenter = null;
        }
    }

    private void loadProfiles() {
        if (getContext() == null) {
            return;
        }

        List<Account> accounts = ProfilePickerPresenter.instance(getContext()).getAccounts();

        GlideIconFetcher.fetchDrawables(getContext(),
                Helpers.map(accounts, Account::getAvatarImageUrl),
                icons -> {
                    if (getContext() == null || getView() == null) {
                        return;
                    }

                    mAdapter.clear();

                    List<ProfileItem> items = new ArrayList<>();

                    for (int i = 0; i < accounts.size(); i++) {
                        Drawable icon = icons != null && i < icons.size() ? icons.get(i) : null;
                        String title = accounts.get(i).getName() != null || accounts.get(i).getEmail() == null
                                ? accounts.get(i).getName()
                                : accounts.get(i).getEmail();
                        items.add(new ProfileItem(accounts.get(i), title, icon));
                    }

                    mAdapter.addAll(0, items);
                    mAdapter.add(new ProfileAction(ProfileAction.ADD, getString(R.string.dialog_add_account)));
                    mAdapter.add(new ProfileAction(ProfileAction.REMOVE, getString(R.string.dialog_remove_account)));

                    applyVerticalPadding(items.size());
                    focusSelectedAccount(accounts);
                });
    }

    /** Focus rule: initial focus always lands on an account, never on the action tiles. */
    private void focusSelectedAccount(List<Account> accounts) {
        int index = 0;

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).isSelected()) {
                index = i;
                break;
            }
        }

        if (accounts.size() > index) {
            setSelectedPosition(index);
        }
    }

    private void applyVerticalPadding(int count) {
        View view = getView();

        if (view == null) {
            return;
        }

        int density = (int) getResources().getDisplayMetrics().density;
        int rows = ProfileGridLayout.rowsFor(count);
        int rowPx = ROW_HEIGHT_DP * density;
        int screenPx = getResources().getDisplayMetrics().heightPixels;

        view.setPadding(0, ProfileGridLayout.topPaddingPx(rows, rowPx, 0, screenPx), 0, 0);
    }

    private void selectAccount(Account account) {
        AccountsData data = AccountsData.instance(getContext());
        String password = data.getAccountPassword();

        if (password == null) {
            mPresenter.select(account);
            return;
        }

        SimpleEditDialog.showPassword(getContext(), getString(R.string.enter_account_password), "",
                newValue -> {
                    if (password.equals(newValue)) {
                        data.setPasswordAccepted(true);
                        mPresenter.select(account);
                        return true;
                    }

                    return false;
                });
    }

    private void showRemoveAccountDialog() {
        AppDialogPresenter dialog = AppDialogPresenter.instance(getContext());
        List<Account> accounts = mPresenter.getAccounts();

        for (Account account : accounts) {
            String title = account.getName() != null ? account.getName() : account.getEmail();

            dialog.appendSingleButton(UiOptionItem.from(title, option -> {
                mPresenter.removeAccount(account);
                dialog.closeDialog();
                loadProfiles();
            }));
        }

        dialog.showDialog(getString(R.string.dialog_remove_account));
    }

    private final OnItemViewClickedListener mOnItemClicked = new OnItemViewClickedListener() {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof ProfileItem) {
                selectAccount(((ProfileItem) item).account);
            } else if (item instanceof ProfileAction) {
                int type = ((ProfileAction) item).type;

                if (type == ProfileAction.ADD) {
                    YTSignInPresenter.instance(getContext()).start();
                    close();
                } else {
                    showRemoveAccountDialog();
                }
            }
        }
    };

    @Override
    public void close() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
