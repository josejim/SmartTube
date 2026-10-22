package com.liskovsoft.smartyoutubetv2.tv.ui.profiles;

import android.os.Bundle;

import com.liskovsoft.smartyoutubetv2.tv.R;
import com.liskovsoft.smartyoutubetv2.tv.ui.common.LeanbackActivity;

public class ProfilePickerActivity extends LeanbackActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_picker);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.profile_picker_root, new ProfilePickerFragment())
                    .commit();
        }
    }

    @Override
    public void finish() {
        super.finish();

        finishReally();
    }
}
