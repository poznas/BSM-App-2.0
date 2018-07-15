package com.bsm.mobile.legacy.model.shipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mlody Danon on 7/27/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedalShipment {

    private Long points;
    private String team;
    private String info;
    private boolean valid;

}
