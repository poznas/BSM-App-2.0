package com.bsm.mobile.legacy.model;

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
public class PointsInfo {

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

}
