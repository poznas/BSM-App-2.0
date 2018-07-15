package com.bsm.mobile.legacy.model.sidemission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mlody Danon on 7/28/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportBasicFirebase {

    private String sm_name;
    private String performing_user;
    private String recording_user;
    private Long timestamp;
    private boolean valid;
    private boolean rated;

}
