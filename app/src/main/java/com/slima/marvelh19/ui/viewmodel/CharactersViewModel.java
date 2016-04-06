package com.slima.marvelh19.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.characters.CharacterResult;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

/**
 * Characters View Model
 * <p/>
 * Created by sergio.lima on 31/03/2016.
 */
public class CharactersViewModel extends BaseViewModel {

    @Bindable
    public final ObservableArrayList<CharacterResult> lista;

    public CharactersViewModel() {
        lista = new ObservableArrayList<>();

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

    }

    public ItemBinder<CharacterResult> itemViewBinder() {
        return new ItemBinderBase<CharacterResult>(BR.vm, R.layout.uicomponent_character_listitem);
    }

}
