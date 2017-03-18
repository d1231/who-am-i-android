package com.whomi.fragments;

import android.support.v4.app.Fragment;

import com.whomi.views.BaseView;

public class BaseFragment extends Fragment implements BaseView {

    @Override
    public YesNoDialogBuilder getDialogBuilder() {

        return AndroidYesNoDialogBuilder.create(getContext());

    }


}
