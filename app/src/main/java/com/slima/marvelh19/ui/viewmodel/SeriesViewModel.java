package com.slima.marvelh19.ui.viewmodel;

import android.databinding.ObservableArrayList;
import android.util.Log;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.ThumbnailModelResultsInterfaces;
import com.slima.marvelh19.model.characters.SeriesResult;

import net.droidlabs.mvvm.recyclerview.adapter.ClickHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * View model of the Series
 *
 * Created by sergio.lima on 05/04/2016.
 */
public class SeriesViewModel extends ImagesThumbnailsModelInterfaces{

    /** Log Tag */
    private static final String TAG = "StoriesViewModel";

    /**
     * constructor
     *
     */
    public SeriesViewModel() {
       super( new ObservableArrayList<ThumbnailModelResultsInterfaces>());
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
     * bind the item to the actual layout of the view model
     *
     * @return  binded variable onto the layout
     */
//    public ItemBinder<SeriesResult> itemViewBinder() {
//        return new ItemBinderBase<>(BR.vm, R.layout.uicomponent_series);
//    }

    @Override
    int getCustomLayout() {
        return R.layout.uicomponent_series;
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

    ClickHandler<ThumbnailModelResultsInterfaces> mSeriesResultClickHandler;

    public void setSeriesResultClickHandler(ClickHandler<ThumbnailModelResultsInterfaces> seriesResultClickHandler) {
        mSeriesResultClickHandler = seriesResultClickHandler;
    }

    public ClickHandler<SeriesResult> clickHandler()
    {
        return new ClickHandler<SeriesResult>()
        {
            @Override
            public void onClick(SeriesResult seriesResult)
            {
                Log.d("SLIMA", "onClick() called with: " + "Series = [" + seriesResult + "]");
                if(mSeriesResultClickHandler!=null){
                    mSeriesResultClickHandler.onClick(seriesResult);
                }
            }
        };
    }
}
