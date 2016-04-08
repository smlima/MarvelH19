
package com.slima.marvelh19.model.characters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.slima.marvelh19.database.MarvelDatabase;
import com.slima.marvelh19.database.converters.ThumbnailTypeConverter;
import com.slima.marvelh19.model.ThumbnailModelResultsInterfaces;

import java.util.ArrayList;
import java.util.List;

@Table(database = MarvelDatabase.class)
public class EventsResult extends BaseModel implements ThumbnailModelResultsInterfaces {

    @Override
    public int hashCode() {
        // because the id is an Integer there's no problem of clashing hash
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof EventsResult) {
            if (id.equals(((EventsResult) o).getId())){
                return true;
            }
        }
        return false;
    }

    @Column
    @SerializedName("characterId")
    @Expose
    public Integer characterId;


    @PrimaryKey
    @SerializedName("id")
    @Expose
    public Integer id;

    @Column
    @SerializedName("title")
    @Expose
    public String title;

    @Column
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("resourceURI")
    @Expose
    public String resourceURI;
    @SerializedName("urls")
    @Expose
    public List<Url> urls = new ArrayList<Url>();
    @SerializedName("modified")
    @Expose
    public String modified;
    @SerializedName("start")
    @Expose
    public String start;
    @SerializedName("end")
    @Expose
    public String end;
    @Column(typeConverter = ThumbnailTypeConverter.class)
    @SerializedName("thumbnail")
    @Expose
    public Thumbnail thumbnail;
    @SerializedName("creators")
    @Expose
    public Creators creators;
    @SerializedName("characters")
    @Expose
    public Characters characters;
    @SerializedName("stories")
    @Expose
    public Stories stories;
//    @SerializedName("comics")
//    @Expose
//    public Comics comics;
//    @SerializedName("series")
//    @Expose
//    public Series series;
//    @SerializedName("next")
//    @Expose
//    public Next next;
//    @SerializedName("previous")
//    @Expose
//    public Previous previous;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Creators getCreators() {
        return creators;
    }

    public void setCreators(Creators creators) {
        this.creators = creators;
    }

    public Characters getCharacters() {
        return characters;
    }

    public void setCharacters(Characters characters) {
        this.characters = characters;
    }

    public Stories getStories() {
        return stories;
    }

    public void setStories(Stories stories) {
        this.stories = stories;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    public String getImageUrl(){

        if (thumbnail != null && thumbnail.getPath() != null && thumbnail.getExtension()!=null) {
            return thumbnail.getPath() + "/" + "portrait_incredible." + thumbnail.getExtension();
        }
        return null;
    }
    public String getImageUrlBig(){

        if (thumbnail != null && thumbnail.getPath() != null && thumbnail.getExtension()!=null) {
            return thumbnail.getPath() + "." + thumbnail.getExtension();
        }
        return null;
    }
}
