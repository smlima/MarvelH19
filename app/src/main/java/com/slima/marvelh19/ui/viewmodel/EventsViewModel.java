package com.slima.marvelh19.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.characters.EventsResult;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Events View Model
 * Created by sergio.lima on 05/04/2016.
 */
public class EventsViewModel extends BaseViewModel {

    private static final String TAG = "StoriesViewModel";

    @Bindable
    public final ObservableArrayList<EventsResult> lista;

    public EventsViewModel() {
        lista =  new ObservableArrayList<>();
    }

    public void addObject(EventsResult stories) {
        if (!lista.contains(stories)) {
            lista.add(stories);
        }

        Log.d(TAG, "addObject() called with: " + "stories = [" + stories.getId() + " _ " + stories.getTitle() + "]");
    }

    public ItemBinder<EventsResult> itemViewBinder() {
        return new ItemBinderBase<>(BR.vm, R.layout.uicomponent_events);
    }

    /**
     * fetch if the lista is empty so it can hidden the stories setcion on the UI
     * @return true is emtpy, false otherwise
     */
    @Bindable
    public boolean isEmpty() {
        //FIXME: why is the lista getting null values ??
        //Log.d(TAG, "isEmpty: " + lista.isEmpty());
        if (lista.isEmpty()){
            return true;
        }

        for (EventsResult eventsResult : lista) {
            if (eventsResult != null) {
                return true;
            }
        }

        return false;
    }

    public void addObjects(List<EventsResult> comicsResultses) {
        lista.clear();

        for (EventsResult comicsResultse : comicsResultses) {
            Log.d(TAG, "addObject() called with: " + "stories = [" + comicsResultse.getId() + " _ " + comicsResultse.getTitle() + "]");

        }

        // clear equal objects.. Just in case
        Set<EventsResult> comicsResultsSet = new HashSet<>();
        comicsResultsSet.addAll(comicsResultses);

        lista.addAll(comicsResultsSet);
        notifyPropertyChanged(BR.lista);
    }
}
