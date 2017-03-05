package com.danny.whomi.presenters;

import android.support.annotation.CallSuper;

import com.danny.whomi.utils.RxUtils;
import com.danny.whomi.views.BaseView;

import java.util.ArrayList;
import java.util.Collections;

import rx.Subscription;

abstract class BasePresenter<T extends BaseView> implements Presenter<T> {

    private final ArrayList<Subscription> subscriptionsList = new ArrayList<>();

    void addSubscriptions(Subscription... subscriptions) {

        Collections.addAll(subscriptionsList, subscriptions);

    }

    @Override
    @CallSuper
    public void detachView() {

        RxUtils.safeUnsubscribe(subscriptionsList);

    }
}