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
public class ReportSingleMedia {

    private String type;
    private String orginalUrl;
    private String thumbnailUrl;

}
