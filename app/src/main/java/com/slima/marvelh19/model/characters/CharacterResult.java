
package com.slima.marvelh19.model.characters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.slima.marvelh19.database.MarvelDatabase;
import com.slima.marvelh19.database.converters.ThumbnailTypeConverter;

import java.io.Serializable;
import java.util.List;

@ModelContainer
@Table(database = MarvelDatabase.class)
public class CharacterResult extends BaseModel implements Serializable{

    @Override
    public int hashCode() {
        // because the id is an Integer there's no problem of clashing hash
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof CharacterResult) {
            if (id.equals(((CharacterResult) o).getId())){
                return true;
            }
        }
        return false;
    }

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @Column
    @SerializedName("name")
    @Expose
    private String name;
    @Column
    @SerializedName("description")
    @Expose
    private String description;
    @Column
    @SerializedName("modified")
    @Expose
    private String modified;
    @Column(typeConverter = ThumbnailTypeConverter.class)
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("resourceURI")
    @Expose
    private String resourceURI;
    @SerializedName("comics")
    @Expose
    private Characters mCharacters;
    @SerializedName("series")
    @Expose
    private Series series;
    @SerializedName("stories")
    @Expose
    private Stories stories;
    @SerializedName("events")
    @Expose
    private Events events;

    @SerializedName("urls")
    @Expose
    public List<Url> urls ;

    @OneToMany(methods = OneToMany.Method.ALL, variableName = "urls")
    public List<Url> getUrls() {
        if (urls == null || urls.isEmpty()) {
            urls = SQLite.select()
                    .from(Url.class)
                    .where(Url_Table.mForeignKeyContainer_id.eq(id))
                    .queryList();
        }
        return urls;
    }



    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The modified
     */
    public String getModified() {
        return modified;
    }

    /**
     * 
     * @param modified
     *     The modified
     */
    public void setModified(String modified) {
        this.modified = modified;
    }

    /**
     * 
     * @return
     *     The thumbnail
     */
    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    /**
     * 
     * @param thumbnail
     *     The thumbnail
     */
    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * 
     * @return
     *     The resourceURI
     */
    public String getResourceURI() {
        return resourceURI;
    }

    /**
     * 
     * @param resourceURI
     *     The resourceURI
     */
    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    /**
     * 
     * @return
     *     The comics
     */
    public Characters getCharacters() {
        return mCharacters;
    }

    /**
     * 
     * @param characters
     *     The comics
     */
    public void setCharacters(Characters characters) {
        this.mCharacters = characters;
    }

    /**
     * 
     * @return
     *     The series
     */
    public Series getSeries() {
        return series;
    }

    /**
     * 
     * @param series
     *     The series
     */
    public void setSeries(Series series) {
        this.series = series;
    }

    /**
     * 
     * @return
     *     The stories
     */
    public Stories getStories() {
        return stories;
    }

    /**
     * 
     * @param stories
     *     The stories
     */
    public void setStories(Stories stories) {
        this.stories = stories;
    }

    /**
     * 
     * @return
     *     The events
     */
    public Events getEvents() {
        return events;
    }

    /**
     * 
     * @param events
     *     The events
     */
    public void setEvents(Events events) {
        this.events = events;
    }

//    /**
//     *
//     * @return
//     *     The urls
//     */
//    public List<Url> getUrls() {
//        return urls;
//    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }


    public String getImageUrl(){

       return thumbnail.getPath() + "/" + "landscape_incredible." + thumbnail.getExtension();
    }

    public String getDetailLink(){
        List<Url> lUrls = getUrls();

        if (lUrls != null) {
            for (Url lUrl : lUrls) {
                if (lUrl.getType().equals("detail")) {
                    return lUrl.getUrl();
                }
            }
        }
        return null;
    }
    public String getComicLink(){
        List<Url> lUrls = getUrls();

        if (lUrls != null) {
            for (Url lUrl : lUrls) {
                if (lUrl.getType().equals("comiclink")) {
                    return lUrl.getUrl();
                }
            }
        }
        return null;
    }

    public String getWikiLink(){
        List<Url> lUrls = getUrls();

        if (lUrls != null) {
            for (Url lUrl : lUrls) {
                if (lUrl.getType().equals("wiki")) {
                    return lUrl.getUrl();
                }
            }
        }
        return null;
    }


}
