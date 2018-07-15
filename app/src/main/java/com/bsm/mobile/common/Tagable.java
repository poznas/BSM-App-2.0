package com.bsm.mobile.common;

public interface Tagable {

    default String getTag(){
        return this.getClass().getSimpleName();
    }
}
