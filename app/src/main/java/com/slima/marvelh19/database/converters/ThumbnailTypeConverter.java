package com.slima.marvelh19.database.converters;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.slima.marvelh19.model.characters.Thumbnail;

/**
 * Type Converter to store the thumbnail object on the table, and avoid using second table
 * <p/>
 * Created by sergio.lima on 03/04/2016.
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
public class ThumbnailTypeConverter extends TypeConverter<String, Thumbnail> {

    @Override
    public String getDBValue(Thumbnail model) {
        return model == null ? null : String.valueOf(model.getPath()) + "," + model.getExtension();
    }

    @Override
    public Thumbnail getModelValue(String data) {
        String[] values = data.split(",");
        if (values.length < 2) {
            return null;
        } else {
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setPath(values[0]);
            thumbnail.setExtension(values[1]);
            return thumbnail;
        }
    }
}
