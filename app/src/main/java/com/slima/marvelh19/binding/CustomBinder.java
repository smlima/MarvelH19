package com.slima.marvelh19.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
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
    public static void loadImage(final ImageView view, final String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)   // load from cache only
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        // nothing to be done
                    }

                    @Override
                    public void onError() {
                        // try load from the network
                        Picasso.with(view.getContext())
                                .load(imageUrl)
                                .into(view);
                    }
                });
    }

    /**
     * Bind the url to the Imageview , through Picasso
     *
     * @param view     the imageview
     * @param imageUrlBig the url of  the image
     */
    @BindingAdapter({"imageUrlBig"})
    public static void loadImageBig(final ImageView view, final String imageUrlBig) {
        Picasso.with(view.getContext())
                .load(imageUrlBig)
                .networkPolicy(NetworkPolicy.OFFLINE)   // load from cache only
                .noPlaceholder()
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        // great..
                    }

                    @Override
                    public void onError() {
                        // try load the image from the internet
                        Picasso.with(view.getContext())
                                .load(imageUrlBig)
                                .noPlaceholder()
                                .into(view);
                    }
                });
    }

}
