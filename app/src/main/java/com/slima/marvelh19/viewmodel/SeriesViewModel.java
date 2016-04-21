package com.slima.marvelh19.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
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

    private Integer mCharacterId;
    /**
     * DBFlow observer
     */
    private FlowContentObserver observer = new FlowContentObserver();

    /**
     * constructor
     *
     */
    public SeriesViewModel(Context context, Integer id) {
       super( new ObservableArrayList<ThumbnailModelResultsInterfaces>(), context);
        mCharacterId = id;
    }

    /**
     * Add object into the observable list
     *
     * @param stories   object to add
     */
    public void addObject(final SeriesResult stories) {
        if (!lista.contains(stories)) {
            ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lista.add(stories);
                }
            });
        }
    }

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
        final Set<SeriesResult> comicsResultsSet = new HashSet<>();
        comicsResultsSet.addAll(seriesResultList);

        ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lista.addAll(comicsResultsSet);
                notifyPropertyChanged(BR.lista);
            }
        });
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

    /**
     * obersver for the Table
     */
    private FlowContentObserver.OnModelStateChangedListener modelChangeListener = new FlowContentObserver.OnModelStateChangedListener() {
        @Override
        public void onModelStateChanged(@Nullable Class<? extends Model> table, BaseModel.Action action, @NonNull SQLCondition[] primaryKeyValues) {

            if (primaryKeyValues == null || primaryKeyValues.length == 0) {
                // nothing to update
                return;
            }

            Condition[] conditions = new Condition[primaryKeyValues.length];

            for (int i = 0; i < primaryKeyValues.length; i++) {
                conditions[i] = Condition.column(new NameAlias("id")).eq(primaryKeyValues[i].value());
            }

            final List<SeriesResult> characterResults = SQLite.select().from(SeriesResult.class).where(conditions).queryList();

            for (SeriesResult characterResult : characterResults) {
                if (characterResult.getThumbnail() != null && characterResult.getThumbnail().hasImageAvailable()) {
                    addObject(characterResult);
                }
            }
        }
    };


    @Override
    public void preload() {

        reload();

    }

    @Override
    public void reload() {
        SQLCondition characterIdCondition = Condition.column(new NameAlias("characterId")).eq(mCharacterId);

        List<SeriesResult> seriesResults = SQLite.select().from(SeriesResult.class).where(characterIdCondition).queryList();
        addObjects(seriesResults);
    }

    @Override
    public void start() {
        observer.registerForContentChanges(mContext, SeriesResult.class);
        observer.addModelChangeListener(modelChangeListener);
        reload();
    }

    @Override
    public void stop() {
        observer.unregisterForContentChanges(mContext);

    }
}
