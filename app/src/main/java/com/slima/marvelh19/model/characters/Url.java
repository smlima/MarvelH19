
package com.slima.marvelh19.model.characters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.slima.marvelh19.database.MarvelDatabase;

import java.io.Serializable;

import kotlin.jvm.Transient;

@Table(database = MarvelDatabase.class)
public class Url extends BaseModel implements Serializable {

    @PrimaryKey(autoincrement = true)
    long id;

    @Transient
    @ForeignKey(saveForeignKeyModel = false)
    public ForeignKeyContainer<CharacterResult> mForeignKeyContainer;

    public void associateCharacter(CharacterResult characterResult) {
        mForeignKeyContainer = new ForeignKeyContainer<>(CharacterResult.class);
        mForeignKeyContainer.setModel(characterResult); // <-- ignored?
        mForeignKeyContainer.put(CharacterResult_Table.id, characterResult.getId());
    }

    @Column
    @SerializedName("type")
    @Expose
    private String type;

    @Column
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public ForeignKeyContainer<CharacterResult> getForeignKeyContainer() {
        return mForeignKeyContainer;
    }

    public void setForeignKeyContainer(ForeignKeyContainer<CharacterResult> foreignKeyContainer) {
        mForeignKeyContainer = foreignKeyContainer;
    }
}
