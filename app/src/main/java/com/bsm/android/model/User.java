package com.bsm.android.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
public class User {

    private String displayName;
    private String email;
    private String facebook;
    private String gender;
    private String instanceId;
    private String label;
    private String photoUrl;
    private String team;
}
