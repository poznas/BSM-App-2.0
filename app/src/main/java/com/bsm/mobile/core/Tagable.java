package com.bsm.mobile.core;

public interface Tagable {

    default String getTag(){
        return this.getClass().getSimpleName();
    }
}
