package com.danny.projectt.presenters;

import android.support.annotation.CallSuper;

import com.danny.projectt.utils.RxUtils;
import com.danny.projectt.views.BaseView;

import java.util.ArrayList;
import java.util.Collections;

import rx.Subscription;

public abstract class BasePresenter<T extends BaseView> implements Presenter<T> {

    private final ArrayList<Subscription> subscriptionsList = new ArrayList<>();

    protected void addSubscriptions(Subscription... subscriptions) {

        Collections.addAll(subscriptionsList, subscriptions);

    }

    @Override
    @CallSuper
    public void detachView() {

        RxUtils.safeUnsubscribe(subscriptionsList);

    }
}
