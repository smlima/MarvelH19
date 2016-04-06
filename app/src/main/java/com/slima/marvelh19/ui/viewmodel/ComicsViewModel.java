package com.slima.marvelh19.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.characters.ComicsResults;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Comics View model
 * <p/>
 * Created by sergio.lima on 05/04/2016.
 */
public class ComicsViewModel extends BaseViewModel {

    /**
     * Log Tag
     */
    private static final String TAG = "StoriesViewModel";

    @Bindable
    public final ObservableArrayList<ComicsResults> lista;

    /**
     * Constructor
     */
    public ComicsViewModel() {
        lista = new ObservableArrayList<>();
    }

    /**
     * Add object into the list of stories
     *
     * @param stories story to add
     */
    public void addObject(ComicsResults stories) {
        if (!lista.contains(stories)) {
            lista.add(stories);
        }

        //Log.d(TAG, "addObject() called with: " + "stories = [" + stories.getId() + " _ " + stories.getTitle() + "]");
    }

    /**
     * Bind layout with model
     *
     * @return itembinder
     */
    public ItemBinder<ComicsResults> itemViewBinder() {
        return new ItemBinderBase<>(BR.vm, R.layout.uicomponent_comics);
    }

    /**
     * fetch if the lista is empty so it can hidden the stories setcion on the UI
     *
     * @return true is emtpy, false otherwise
     */
    @Bindable
    public boolean isEmpty() {
        //FIXME: why is the lista getting null values ??
        Log.d(TAG, "isEmpty: " + lista.isEmpty());
        if (lista.isEmpty()) {
            return true;
        }

        for (ComicsResults comicsResult : lista) {
            if (comicsResult != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add collection of comics into the list
     * only distinct comics will be added
     *
     * @param comicsResultses comics
     */
    public void addObjects(List<ComicsResults> comicsResultses) {
//
//        for (ComicsResults comicsResultse : comicsResultses) {
//
//        }

        // clear equal objects.. Just in case
        Set<ComicsResults> comicsResultsSet = new HashSet<>();
        comicsResultsSet.addAll(comicsResultses);

        lista.addAll(comicsResultsSet);

        notifyPropertyChanged(BR.lista);

    }
}
