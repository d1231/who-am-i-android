package com.danny.whomi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class FragmentTransitionManager {

    private FragmentManager fragmentManager;

    private int contentView;

    public FragmentTransitionManager() {


    }

    public void takeActivity(AppCompatActivity activity, int contentView) {

        this.contentView = contentView;

        fragmentManager = activity.getSupportFragmentManager();
    }

    public void dropActivity() {

        fragmentManager = null;
    }

    public void replaceTo(Fragment fragment) {

        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
//            ft.setCustomAnimations(R.anim.push_right_animation, R.anim.pop_left_animation,
//                    R.anim.push_left_animation, R.anim.pop_right_animation);
            ft.replace(contentView, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void resetTo(Fragment fragment) {

        if (fragmentManager != null) {
            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(contentView, fragment);
            ft.commit();
        }
    }

    public void goBack() {

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }

    }
}
