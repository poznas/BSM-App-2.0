package com.bsm.mobile.legacy.model;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.CompareToBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Comparable<User>{

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
        return new CompareToBuilder()
                .append(getTeam(), user.getTeam())
                .append(getLabel(), user.getLabel())
                .append(getDisplayName(), user.getDisplayName())
                .build();
    }
}
