package com.bsm.mobile.agh;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "team_score")
public class TeamScore {

    @NonNull
    @PrimaryKey
    private String id;

    @ColumnInfo(name = "score")
    private Long score;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

}
