package com.bsm.mobile.agh;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface TeamScoreDao {

    @Insert
    void insert(TeamScore score);

    @Update
    void update(TeamScore score);

    @Delete
    void deletePerson(TeamScore score);

    @Query("select * from team_score where id = :teamId")
    TeamScore select(String teamId);
}
