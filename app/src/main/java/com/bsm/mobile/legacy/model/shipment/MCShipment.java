package com.bsm.mobile.legacy.model.shipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mlody Danon on 7/26/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MCShipment {

    private boolean valid;
    private Long points;
    private String team;
    private String name;
    private String info;

}
