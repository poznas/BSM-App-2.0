package com.bsm.mobile.legacy.model;

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
public class ZonglerPost {

    private String authorPhotoUrl;
    private String author;
    private String title;
    private String body;
    private Long timestamp;
    private String imageUrl;
    private String videoUrl;
    private String thumbnailUrl;

}