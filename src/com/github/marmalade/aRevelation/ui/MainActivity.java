/*
 * Copyright 2013 Aleksey Kislin
 * Copyright 2013 Michal Švirec
 *
 *
 * This file is part of aRevelation.
 *
 * aRevelation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aRevelation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aRevelation.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.marmalade.aRevelation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.github.marmalade.aRevelation.ui.fragment.EntryFragment;
import com.github.marmalade.aRevelation.IBackPressedListener;
import com.github.marmalade.aRevelation.ui.fragment.MainMenuFragment;
import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.ui.fragment.FileEntriesFragment;


/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 5/29/13
 * Time: 11:26 PM
 */
public class MainActivity extends FragmentActivity {

    public final static String MAIN_MENU_FRAGMENT       = "MainMenuFragment";
    public final static String OPEN_FILE_FRAGMENT       = "OpenFileFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mainLayout, new MainMenuFragment(), MAIN_MENU_FRAGMENT);
            fragmentTransaction.commit();
        }
    }


    @Override
    protected void onPause() {
        // Close access
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof FileEntriesFragment)
            ((FileEntriesFragment)currentFragment).blockAccess();
        else if (currentFragment instanceof EntryFragment)
            ((EntryFragment)currentFragment).blockAccess();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof MainMenuFragment) {
            super.onBackPressed();
            return;
        }

        if(currentFragment instanceof IBackPressedListener) {
            ((IBackPressedListener) currentFragment).onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }


    Fragment getCurrentFragment() {
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag(MAIN_MENU_FRAGMENT);
        if (myFragment.isVisible()) return myFragment;
        myFragment = getSupportFragmentManager().findFragmentByTag(OPEN_FILE_FRAGMENT);
        if (myFragment.isVisible()) return myFragment;
        else return null;

    }


    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
