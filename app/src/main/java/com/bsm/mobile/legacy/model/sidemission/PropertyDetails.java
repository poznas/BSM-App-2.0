package com.bsm.mobile.legacy.model.sidemission;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mlody Danon on 8/8/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDetails {

    private String name;
    private String hint;
    private String symbol;
    private String type;

    private List<String> spinnerKeys;
    private List<Long> spinnerValues;
    private Long limitedValue;
    private String profType;
}
