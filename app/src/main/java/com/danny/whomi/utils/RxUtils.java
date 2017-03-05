package com.danny.whomi.utils;

import java.util.Collection;

import rx.Subscription;
import timber.log.Timber;

public class RxUtils {

    public static void onError(Throwable throwable) {

        throwable.printStackTrace();

        Timber.e(throwable, "Error");

    }

    public static void safeUnsubscribe(Collection<Subscription> subscriptions) {

        for (Subscription subscription : subscriptions) {

            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }

        }

        subscriptions.clear();

    }
}

