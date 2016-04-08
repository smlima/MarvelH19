package com.slima.marvelh19.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Custom binding the UI elements
 * with custom mechanisms
 * <p/>
 * Created by sergio.lima on 01/04/2016.
 */
public class CustomBinder {

    /**
     * Bind the url to the Imageview , through Picasso
     *
     * @param view     the imageview
     * @param imageUrl the url of  the image
     */
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

    /**
     * Bind the url to the Imageview , through Picasso
     *
     * @param view     the imageview
     * @param imageUrlBig the url of  the image
     */
    @BindingAdapter({"imageUrlBig"})
    public static void loadImageBig(ImageView view, String imageUrlBig) {
        Picasso.with(view.getContext())
                .load(imageUrlBig)
                .noPlaceholder()
                .into(view);
    }

}
