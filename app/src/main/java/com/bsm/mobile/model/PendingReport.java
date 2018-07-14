package com.bsm.mobile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
