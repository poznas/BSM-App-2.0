package com.bsm.mobile.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Privilege {

    private int iconId;
    private String brand;
    private boolean checkIfContain;
    private int pendingReports;

}
