package com.github.marmalade.aRevelation.ui;

import android.support.v4.app.FragmentActivity;

/**
 * Created by sviro on 1/15/14.
 */
public abstract class BlockAccessActivity extends FragmentActivity {
     protected abstract void blockAccess();

    @Override
    protected void onStop() {
        super.onStop();

        if (!isFinishing() && !isChangingConfigurations()) {
            blockAccess();
        }
    }
}
