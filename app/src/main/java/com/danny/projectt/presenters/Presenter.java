package com.danny.projectt.presenters;

import com.danny.projectt.views.BaseView;

interface Presenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

}
