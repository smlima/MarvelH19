
package com.slima.marvelh19.model.characters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("price")
    @Expose
    public Double price;

}
