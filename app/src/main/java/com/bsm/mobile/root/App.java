package com.bsm.mobile.root;

import android.app.Application;

import lombok.Getter;

@Getter
public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder().build();
    }

}
