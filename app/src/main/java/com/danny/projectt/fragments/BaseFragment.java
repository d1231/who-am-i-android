package com.danny.projectt.fragments;

import android.support.v4.app.Fragment;

import com.danny.projectt.views.BaseView;

public class BaseFragment extends Fragment implements BaseView {

    @Override
    public YesNoDialogBuilder getDialogBuilder() {

        return AndroidYesNoDialogBuilder.create(getContext());

    }


}
