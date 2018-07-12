package com.bsm.mobile.core;

import java.util.LinkedList;

import io.reactivex.disposables.Disposable;

public interface MultiSubscriber {

    default void clearSubscriptions(LinkedList<Disposable> subscriptions){
        if(subscriptions == null){ return; }
        for(Disposable subscription : subscriptions){
            if(!subscription.isDisposed()){
                subscription.dispose();
            }
        }
        subscriptions.clear();
    }
}
