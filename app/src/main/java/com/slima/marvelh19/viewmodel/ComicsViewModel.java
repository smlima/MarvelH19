package com.slima.marvelh19.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
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

    private Integer mCharacterId;

    /**
     * DBFlow observer
     */
    private FlowContentObserver observer = new FlowContentObserver();

    /**
     * Constructor
     */
    public ComicsViewModel(Context context,Integer id) {
        super(new ObservableArrayList<ThumbnailModelResultsInterfaces>(), context);

        mCharacterId = id;

    }

    /**
     * Add object into the list of stories
     *
     * @param stories story to add
     */
    public void addObject(final ComicsResults stories) {
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
    public void addObjects( List<ComicsResults> comicsResultses) {

        // clear equal objects.. Just in case
        final Set<ComicsResults> comicsResultsSet = new HashSet<>();
        comicsResultsSet.addAll(comicsResultses);

        ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lista.addAll(comicsResultsSet);
                notifyPropertyChanged(BR.lista);
            }
        });

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

    @Override
    public void preload() {

        reload();
    }

    @Override
    public void reload() {
        SQLCondition characterIdCondition = Condition.column(new NameAlias("characterId")).eq(mCharacterId);

        SQLite.select().from(ComicsResults.class).where(characterIdCondition).async().queryList(new TransactionListener<List<ComicsResults>>() {
            @Override
            public void onResultReceived(List<ComicsResults> result) {
                addObjects(result);
            }

            @Override
            public boolean onReady(BaseTransaction<List<ComicsResults>> transaction) {
                return true;
            }

            @Override
            public boolean hasResult(BaseTransaction<List<ComicsResults>> transaction, List<ComicsResults> result) {
                return true;
            }
        });
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

            final List<ComicsResults> characterResults = SQLite.select().from(ComicsResults.class).where(conditions).queryList();

            for (ComicsResults characterResult : characterResults) {
                if (characterResult.getThumbnail() != null && characterResult.getThumbnail().hasImageAvailable()) {
                    addObject(characterResult);
                }
            }
        }
    };

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
