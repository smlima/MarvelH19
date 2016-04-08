package com.slima.marvelh19.model;

import com.slima.marvelh19.model.characters.Thumbnail;

/**
 * Created by sergio.lima on 08/04/2016.
 */
public interface ThumbnailModelResultsInterfaces {

    Thumbnail getThumbnail();
    String getImageUrl();
    String getImageUrlBig();

    public final static String BIG_IMAGE_TYPE = "detail";


//    portrait_small,
//    portrait_medium	,
//    portrait_xlarge,
//    portrait_fantastic,
//    portrait_uncanny,
//    portrait_incredible
}
