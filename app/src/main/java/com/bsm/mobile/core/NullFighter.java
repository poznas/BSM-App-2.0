package com.bsm.mobile.core;


public interface NullFighter {

    default boolean isNull(Object o) {
        return o == null;
    }
}
