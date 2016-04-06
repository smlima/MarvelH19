package com.slima.marvelh19.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.characters.SeriesResult;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * View model of the Series
 *
 * Created by sergio.lima on 05/04/2016.
 */
public class SeriesViewModel extends BaseViewModel {

    /** Log Tag */
    private static final String TAG = "StoriesViewModel";

    @Bindable
    public final ObservableArrayList<SeriesResult> lista;

    /**
     * constructor
     *
     */
    public SeriesViewModel() {
        lista =  new ObservableArrayList<>();
    }

    /**
     * Add object into the observable list
     *
     * @param stories   object to add
     */
    public void addObject(SeriesResult stories) {
        if (!lista.contains(stories)) {
            lista.add(stories);
        }
    }

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

        for (SeriesResult seriesResult : lista) {
            if (seriesResult != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * bind the item to the actual layout of the view model
     *
     * @return  binded variable onto the layout
     */
    public ItemBinder<SeriesResult> itemViewBinder() {
        return new ItemBinderBase<>(BR.vm, R.layout.uicomponent_series);
    }

    /**
     * Add list of objects into the list.
     * Only distinct elements will be added into the list
     *
     * @param seriesResultList   list of series
     */
    public void addObjects(List<SeriesResult> seriesResultList) {
        lista.clear();

        // clear equal objects.. Just in case
        Set<SeriesResult> comicsResultsSet = new HashSet<>();
        comicsResultsSet.addAll(seriesResultList);

        lista.addAll(comicsResultsSet);
        notifyPropertyChanged(BR.lista);
    }
}
