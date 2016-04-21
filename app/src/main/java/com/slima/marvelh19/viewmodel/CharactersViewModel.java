package com.slima.marvelh19.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.Url;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.List;

/**
 * Characters View Model
 * <p/>
 * Created by sergio.lima on 31/03/2016.
 */
public class CharactersViewModel extends BaseViewModel {

    private static final String TAG = "CharViewModel";
    @Bindable
    public final ObservableArrayList<CharacterResult> lista;

    private FlowContentObserver observer = new FlowContentObserver();

    Context mContext;

    public CharactersViewModel(Context context) {
        lista = new ObservableArrayList<>();
        mContext = context;

    }

    /**
     * Add a character to the modelview list
     *
     * @param characters the character to display on the UI
     */
    public void addCharacter(CharacterResult characters) {
        if (characters.getThumbnail() != null && characters.getThumbnail().hasImageAvailable()) {
            if (!lista.contains(characters)) {
                this.lista.add(characters);
            }
        }

        ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyPropertyChanged(BR.lista);
            }
        });
    }

    public ItemBinder<CharacterResult> itemViewBinder() {
        return new ItemBinderBase<CharacterResult>(BR.vm, R.layout.uicomponent_character_listitem);
    }

    @Override
    public void preload() {

        reload();

    }

    /**
     * the listener for the table changes
     */
    FlowContentObserver.OnModelStateChangedListener modelChangeListener = new FlowContentObserver.OnModelStateChangedListener() {
        @Override
        public void onModelStateChanged(@Nullable Class<? extends Model> table, BaseModel.Action action, @NonNull SQLCondition[] primaryKeyValues) {

            if (primaryKeyValues==null || primaryKeyValues.length==0){
                // nothing to update
                return;
            }

            Condition[] conditions = new Condition[primaryKeyValues.length];

            for (int i = 0; i < primaryKeyValues.length; i++) {
                conditions[i] = Condition.column(new NameAlias("id")).eq(primaryKeyValues[i].value());
            }

            final List<CharacterResult> characterResults = SQLite.select().from(CharacterResult.class).where(conditions).queryList();

                for (CharacterResult characterResult : characterResults) {
                    if (characterResult.getThumbnail() != null && characterResult.getThumbnail().hasImageAvailable()) {
                        addCharacter(characterResult);
                    }

                    for (Url url : characterResult.getUrls()) {
                        Log.d(TAG, "run() called with: " + "url= " + url);
                    }
                }

            }
    };

    @Override
    public void reload() {
        List<CharacterResult> characterResults = SQLite.select().from(CharacterResult.class).orderBy(OrderBy.fromString("name")).queryList();

        for (CharacterResult characterResult : characterResults) {
            addCharacter(characterResult);
        }

    }

    @Override
    public void stop() {
        super.stop();
        observer.unregisterForContentChanges(mContext);
    }

    @Override
    public void start() {
        observer.registerForContentChanges(mContext, CharacterResult.class);
        observer.addModelChangeListener(modelChangeListener);
        reload();
    }
}
