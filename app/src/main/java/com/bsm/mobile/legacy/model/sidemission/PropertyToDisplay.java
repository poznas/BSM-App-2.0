package com.bsm.mobile.legacy.model.sidemission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mlody Danon on 7/29/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyToDisplay {

    private String name;
    private Double grade;

}
