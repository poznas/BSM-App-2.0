package com.bsm.mobile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
