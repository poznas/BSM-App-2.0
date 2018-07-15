package com.bsm.mobile.legacy.model.sidemission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mlody Danon on 7/31/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingReport {

    private String sm_name;
    private String performing_user;
    private String user_photoUrl;
    private Long timestamp;
    private String rpid;
    private boolean post;

}
