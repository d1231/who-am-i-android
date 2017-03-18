package com.whomi.views;

import android.support.annotation.UiThread;

import com.whomi.fragments.YesNoDialogBuilder;

public interface BaseView {

    @UiThread
    YesNoDialogBuilder getDialogBuilder();

}
