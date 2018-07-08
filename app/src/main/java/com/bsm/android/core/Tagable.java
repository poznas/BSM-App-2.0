package com.bsm.android.core;

public interface Tagable {

    default String getTag(){
        return this.getClass().getSimpleName();
    }
}
