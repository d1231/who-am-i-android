package com.whomi.fragments;

import android.support.annotation.StringRes;

import rx.Observable;

public interface YesNoDialogBuilder {

    YesNoDialogBuilder setTitle(@StringRes int title);

    YesNoDialogBuilder setTitle(String title);

    YesNoDialogBuilder setMsg(@StringRes int msg);

    YesNoDialogBuilder setMsg(String msg);

    YesNoDialogBuilder setPosMsg(@StringRes int posMsg);

    YesNoDialogBuilder setPosMsg(String posMsg);

    YesNoDialogBuilder setNegMsg(@StringRes int negMsg);

    YesNoDialogBuilder setNegMsg(String negMsg);

    Observable<DialogResult> create();

}
