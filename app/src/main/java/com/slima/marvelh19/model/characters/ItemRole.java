
package com.slima.marvelh19.model.characters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemRole {

    @SerializedName("resourceURI")
    @Expose
    public String resourceURI;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("role")
    @Expose
    public String role;

}
