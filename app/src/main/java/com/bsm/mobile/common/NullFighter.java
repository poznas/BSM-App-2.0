package com.bsm.mobile.common;


public interface NullFighter {

    default boolean isNull(Object o) {
        return o == null;
    }
}
