package com.danny.whomi.presenters;

import com.danny.whomi.views.BaseView;

interface Presenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

}
