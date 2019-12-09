package com.bsm.mobile.agh;

import android.app.Application;

import static android.arch.persistence.room.Room.databaseBuilder;

//@Module
public class RoomModule {

    private AppDatabase appDatabase;

    public RoomModule(Application application) {
        appDatabase = databaseBuilder(application, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
    }

    //@Singleton
    //@Provides
    AppDatabase providesRoomDatabase() {
        return appDatabase;
    }

    //@Singleton
    //@Provides
    TeamScoreDao providesScoreDao(AppDatabase database) {
        return database.scoreDao();
    }
}
