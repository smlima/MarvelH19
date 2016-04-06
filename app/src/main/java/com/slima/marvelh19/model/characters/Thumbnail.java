
package com.slima.marvelh19.model.characters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

public class Thumbnail extends BaseModel implements Serializable{

    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("extension")
    @Expose
    private String extension;

    /**
     * 
     * @return
     *     The path
     */
    public String getPath() {
        return path;
    }

    /**
     * 
     * @param path
     *     The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * 
     * @param extension
     *     The extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * method to check if the character has an image, and if its not a 'not available image'
     *
     * @return true if there's an image, false otherwise or has a 'image not available' image
     */
    public boolean hasImageAvailable() {
        if (path == null || path.isEmpty() || path.endsWith("image_not_available")){
            return false;
        }
        return true;
    }

}
