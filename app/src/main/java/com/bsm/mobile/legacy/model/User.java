package com.bsm.mobile.legacy.model;

import android.support.annotation.NonNull;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Comparable<User>, Serializable{

    private String id;

    private String displayName;
    private String email;
    private String facebook;
    private String gender;
    private String instanceId;
    private String label;
    private String photoUrl;
    private String team;

    @Override
    public int compareTo(@NonNull User user) {
        return ComparisonChain.start()
                .compare(team, user.getTeam(), Ordering.natural().nullsLast())
                .compare(label, user.getLabel(), Ordering.natural().reverse().nullsLast())
                .compare(displayName, user.getDisplayName(), Ordering.natural().nullsLast())
                .result();
    }

    public static User empty(){
        return new User();
    }
}
