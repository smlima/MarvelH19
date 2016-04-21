package com.slima.marvelh19.viewmodel;

import android.content.Context;
import android.support.v7.widget.SearchView;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.activities.MainActivity;
import com.slima.marvelh19.model.characters.CharacterResult;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.List;

/**
 * Created by sergio.lima on 04/04/2016.
 */
public class SearchCharacterViewModel extends CharactersViewModel implements SearchView.OnQueryTextListener {


    public SearchCharacterViewModel(Context context) {
        super(context);
    }

    @Override
    public ItemBinder<CharacterResult> itemViewBinder() {
        return new ItemBinderBase<CharacterResult>(BR.vm, R.layout.uicomponent_character_search);
    }

    public void replaceAll(List<CharacterResult> characterResults) {

        lista.clear();
        for (CharacterResult characterResult : characterResults) {
            addCharacter(characterResult);
        }
        notifyPropertyChanged(BR.lista);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        SQLCondition condition = Condition.column(new NameAlias("name")).like(newText + "%");

        if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).getDownloadManager().loadSearchCharacter(newText);
        }

        List<CharacterResult> characterResults = SQLite.select().from(CharacterResult.class).where(condition).queryList();

        replaceAll(characterResults);

        return false;
    }
}
