package com.bsm.mobile.common;

import io.reactivex.disposables.CompositeDisposable;

public interface MultiSubscriber {

    default void clearSubscriptions(CompositeDisposable subscriptions){
        if(subscriptions == null) return;

        subscriptions.clear();
    }
}
