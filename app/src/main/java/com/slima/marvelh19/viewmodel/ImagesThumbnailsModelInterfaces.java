package com.slima.marvelh19.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.model.ThumbnailModelResultsInterfaces;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.List;

/**
 * Created by sergio.lima on 08/04/2016.
 */
public abstract class ImagesThumbnailsModelInterfaces extends BaseViewModel{

    protected Context mContext;

    public ImagesThumbnailsModelInterfaces(ObservableArrayList<ThumbnailModelResultsInterfaces> lista, Context context) {
        this.lista = lista;
        mContext = context;
    }

    @Bindable
    public final ObservableArrayList<ThumbnailModelResultsInterfaces> lista;

    abstract List<ThumbnailModelResultsInterfaces> getListaThumbnails();

    abstract ObservableArrayList<? extends ThumbnailModelResultsInterfaces> getLista();

    public ItemBinder<? extends ThumbnailModelResultsInterfaces> itemViewBinder(){

        if (forcedLayout >0){
            return new ItemBinderBase<>(BR.vm, forcedLayout);
        }
        return new ItemBinderBase<>(BR.vm, getCustomLayout());

    }

    abstract int getCustomLayout();

    /**
     * fetch if the lista is empty so it can hidden the stories setcion on the UI
     * @return true is emtpy, false otherwise
     */
    public boolean isEmpty() {
        //FIXME: why is the lista getting null values ??
        //Log.d(TAG, "isEmpty: " + lista.isEmpty());
        if (lista.isEmpty()){
            return true;
        }

        for (ThumbnailModelResultsInterfaces seriesResult : lista) {
            if (seriesResult != null) {
                return true;
            }
        }

        return false;
    }

    int forcedLayout = 0;
    public void setForcedLayout(int uicomponent_images_big) {
        forcedLayout = uicomponent_images_big;
    }
}
