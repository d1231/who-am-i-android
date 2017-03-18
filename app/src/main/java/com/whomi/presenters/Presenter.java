package com.whomi.presenters;

import com.whomi.views.BaseView;

interface Presenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

}
