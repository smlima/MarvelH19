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
import com.slima.marvelh19.model.characters.ComicsResults;
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

    public Integer mCharacterId;
    /**
     * DBFlow observer
     */
    private FlowContentObserver observer = new FlowContentObserver();

    public EventsViewModel(Context context, Integer id) {
       super(new ObservableArrayList<ThumbnailModelResultsInterfaces>(), context);
        mCharacterId = id;
    }

    public void addObject(final EventsResult stories) {
        if (!lista.contains(stories)) {
            ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lista.add(stories);
                }
            });
        }

        Log.d(TAG, "addObject() called with: " + "stories = [" + stories.getId() + " _ " + stories.getTitle() + "]");
    }

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
        final Set<EventsResult> comicsResultsSet = new HashSet<>();
        comicsResultsSet.addAll(comicsResultses);

        ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lista.addAll(comicsResultsSet);
                notifyPropertyChanged(BR.lista);
            }
        });
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

            final List<EventsResult> characterResults = SQLite.select().from(EventsResult.class).where(conditions).queryList();

            for (EventsResult characterResult : characterResults) {
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

        List<EventsResult> eventsResults = SQLite.select().from(EventsResult.class).where(characterIdCondition).queryList();
        addObjects(eventsResults);
    }

    @Override
    public void start() {
        observer.registerForContentChanges(mContext, ComicsResults.class);
        observer.addModelChangeListener(modelChangeListener);
        reload();
    }

    @Override
    public void stop() {
        observer.unregisterForContentChanges(mContext);

    }
}
