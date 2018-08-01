package com.bsm.mobile.legacy.model;

import android.support.annotation.NonNull;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mlody Danon on 7/25/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsInfo implements Serializable, Comparable<PointsInfo>{

    private String id;

    private Long points;
    private String team;
    private String label;
    private Long timestamp;
    private String info;
    private String name;
    private String user_name;
    private String user_photo;
    private boolean isPost;
    private String losser;
    private String winner;


    public boolean getIsPost() {
        return isPost;
    }

    public void setIsPost(boolean post) {
        isPost = post;
    }

    @Override
    public int compareTo(@NonNull PointsInfo info) {
        return ComparisonChain.start()
                .compare(points, info.getPoints(), Ordering.natural().reverse().nullsLast())
                .compare(user_name, info.getUser_name(), Ordering.natural().nullsLast())
                .result();
    }
}
