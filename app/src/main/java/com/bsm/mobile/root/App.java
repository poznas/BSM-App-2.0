package com.bsm.mobile.root;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import lombok.Getter;

@Getter
public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder().build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
