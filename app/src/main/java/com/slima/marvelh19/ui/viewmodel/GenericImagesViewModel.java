package com.slima.marvelh19.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.ThumbnailModelResultsInterfaces;
import com.slima.marvelh19.model.characters.SeriesResult;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.List;

/**
 * Created by sergio.lima on 08/04/2016.
 */
public class GenericImagesViewModel extends BaseViewModel {


    @Bindable
    public final ObservableArrayList<ThumbnailModelResultsInterfaces> lista ;

    public GenericImagesViewModel() {
        this.lista = new ObservableArrayList<>();
    }

    public void setListComics( List<ThumbnailModelResultsInterfaces> listComics) {
        lista.addAll(listComics);
    }

    public ItemBinder<SeriesResult> itemViewBinder() {
        return new ItemBinderBase<>(BR.vm, R.layout.uicomponent_series);
    }
}
