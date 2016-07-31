package com.danny.projectt.presenters;

import android.os.Bundle;

import com.danny.projectt.views.BaseView;

public interface Presenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

}
