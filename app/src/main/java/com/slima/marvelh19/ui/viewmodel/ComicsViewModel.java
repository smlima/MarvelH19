package com.slima.marvelh19.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.ThumbnailModelResultsInterfaces;
import com.slima.marvelh19.model.characters.ComicsResults;

import net.droidlabs.mvvm.recyclerview.adapter.ClickHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Comics View model
 * <p/>
 * Created by sergio.lima on 05/04/2016.
 */
public class ComicsViewModel extends ImagesThumbnailsModelInterfaces{

    /**
     * Log Tag
     */
    private static final String TAG = "StoriesViewModel";

    /**
     * Constructor
     */
    public ComicsViewModel() {
        super(new ObservableArrayList<ThumbnailModelResultsInterfaces>());

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
//    public ItemBinder<ComicsResults> itemViewBinder() {
//        return new ItemBinderBase<>(BR.vm, R.layout.uicomponent_comics);
//    }

    @Override
    int getCustomLayout() {
        return R.layout.uicomponent_comics;
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

        for (ThumbnailModelResultsInterfaces comicsResult : lista) {
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

        // clear equal objects.. Just in case
        Set<ComicsResults> comicsResultsSet = new HashSet<>();
        comicsResultsSet.addAll(comicsResultses);

        lista.addAll(comicsResultsSet);

        notifyPropertyChanged(BR.lista);

    }

    ClickHandler<ThumbnailModelResultsInterfaces> mComicsResultsClickHandler;

    public void setComicsResultsClickHandler(ClickHandler<ThumbnailModelResultsInterfaces> comicsResultsClickHandler) {
        mComicsResultsClickHandler = comicsResultsClickHandler;
    }

    public ClickHandler<ComicsResults> clickHandler()
    {
        return new ClickHandler<ComicsResults>()
        {
            @Override
            public void onClick(ComicsResults comicsResult)
            {
                Log.d("SLIMA", "onClick() called with: " + "ComicsResults = [" + comicsResult + "]");
                if(mComicsResultsClickHandler!=null){
                    mComicsResultsClickHandler.onClick(comicsResult);
                }
            }
        };
    }
}
