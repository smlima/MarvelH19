package com.slima.marvelh19.ui.viewmodel;

import android.databinding.ObservableArrayList;
import android.util.Log;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.ThumbnailModelResultsInterfaces;
import com.slima.marvelh19.model.characters.EventsResult;

import net.droidlabs.mvvm.recyclerview.adapter.ClickHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Events View Model
 * Created by sergio.lima on 05/04/2016.
 */
public class EventsViewModel extends ImagesThumbnailsModelInterfaces{

    private static final String TAG = "StoriesViewModel";

    public EventsViewModel() {
       super(new ObservableArrayList<ThumbnailModelResultsInterfaces>());
    }

    public void addObject(EventsResult stories) {
        if (!lista.contains(stories)) {
            lista.add(stories);
        }

        Log.d(TAG, "addObject() called with: " + "stories = [" + stories.getId() + " _ " + stories.getTitle() + "]");
    }

//    public ItemBinder<EventsResult> itemViewBinder() {
//        return new ItemBinderBase<>(BR.vm, R.layout.uicomponent_events);
//    }

    @Override
    int getCustomLayout() {
        return R.layout.uicomponent_events;
    }

    @Override
    public ObservableArrayList<? extends ThumbnailModelResultsInterfaces> getLista() {
        return lista;
    }

    public List<ThumbnailModelResultsInterfaces> getListaThumbnails() {
        List<ThumbnailModelResultsInterfaces> lista = new ArrayList<>();
        lista.addAll(lista);
        return lista;
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

    ClickHandler<ThumbnailModelResultsInterfaces> mEventsResultClickHandler;

    public void setEventsResultClickHandler(ClickHandler<ThumbnailModelResultsInterfaces> eventsResultClickHandler) {
        mEventsResultClickHandler = eventsResultClickHandler;
    }

    public ClickHandler<EventsResult> clickHandler()
    {
        return new ClickHandler<EventsResult>()
        {
            @Override
            public void onClick(EventsResult eventsViewModel)
            {
                Log.d("SLIMA", "onClick() called with: " + "EventsResult = [" + eventsViewModel + "]");

                if (mEventsResultClickHandler!=null){
                    mEventsResultClickHandler.onClick(eventsViewModel);
                }
            }
        };
    }
}
