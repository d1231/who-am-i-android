package com.whomi.fragments;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import rx.subjects.PublishSubject;

public class AndroidYesNoDialogBuilder implements YesNoDialogBuilder {

    private final Context context;

    private String title;

    private String msg;

    private String posMsg;

    private String negMsg;

    private AndroidYesNoDialogBuilder(Context context) {

        this.context = context;
    }

    public static AndroidYesNoDialogBuilder create(Context context) {

        return new AndroidYesNoDialogBuilder(context);
    }

    @Override
    public AndroidYesNoDialogBuilder setTitle(@StringRes int title) {

        this.title = context.getString(title);
        return this;
    }

    @Override
    public AndroidYesNoDialogBuilder setTitle(String title) {

        this.title = title;
        return this;
    }

    @Override
    public AndroidYesNoDialogBuilder setMsg(@StringRes int msg) {

        this.msg = context.getString(msg);
        return this;
    }

    @Override
    public AndroidYesNoDialogBuilder setMsg(String msg) {

        this.msg = msg;
        return this;
    }

    @Override
    public AndroidYesNoDialogBuilder setPosMsg(@StringRes int posMsg) {

        this.posMsg = context.getString(posMsg);
        return this;
    }

    @Override
    public AndroidYesNoDialogBuilder setPosMsg(String posMsg) {

        this.posMsg = posMsg;
        return this;
    }

    @Override
    public AndroidYesNoDialogBuilder setNegMsg(@StringRes int negMsg) {

        this.negMsg = context.getString(negMsg);
        return this;
    }

    @Override
    public AndroidYesNoDialogBuilder setNegMsg(String negMsg) {

        this.negMsg = negMsg;
        return this;
    }

    @Override
    public PublishSubject<DialogResult> create() {

        PublishSubject<DialogResult> resultSubject = PublishSubject.create();

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(msg)
                .setTitle(title)
                .setPositiveButton(posMsg, (dialogInterface, i) -> {

                    resultSubject.onNext(DialogResult.POSITIVE_CLICKED);
                })
                .setNegativeButton(negMsg, (dialogInterface, i) -> {

                    resultSubject.onNext(DialogResult.NEGATIVE_CLICKED);
                })
                .create();

        dialog.show();

        return resultSubject;
    }

}