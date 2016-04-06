package com.slima.marvelh19.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.characters.StoriesResult;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sergio.lima on 05/04/2016.
 */
public class StoriesViewModel extends BaseViewModel {

    private static final String TAG = "StoriesViewModel";

    @Bindable
    public final ObservableArrayList<StoriesResult> lista;

    public StoriesViewModel() {
        lista =  new ObservableArrayList<>();
    }

    public void addObject(StoriesResult stories) {
        if (!lista.contains(stories)) {
            lista.add(stories);
        }

        Log.d(TAG, "addObject() called with: " + "stories = [" + stories.getId() + " _ " + stories.getTitle() + "]");
    }

    public ItemBinder<StoriesResult> itemViewBinder() {
        return new ItemBinderBase<>(BR.vm, R.layout.uicomponent_stories);
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

        for (StoriesResult storiesResult : lista) {
            if (storiesResult != null) {
                return true;
            }
        }

        return false;
    }

    public void addObjects(List<StoriesResult> comicsResultses) {
        lista.clear();

        for (StoriesResult comicsResultse : comicsResultses) {
            Log.d(TAG, "addObject() called with: " + "stories = [" + comicsResultse.getId() + " _ " + comicsResultse.getTitle() + "]");

        }

        // clear equal objects.. Just in case
        Set<StoriesResult> comicsResultsSet = new HashSet<>();
        comicsResultsSet.addAll(comicsResultses);

        lista.addAll(comicsResultsSet);
        notifyPropertyChanged(BR.lista);
    }
}
