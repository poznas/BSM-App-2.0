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
public class SideMissionInfo {

    private String name;
    private String link;
    private boolean Post;

}
