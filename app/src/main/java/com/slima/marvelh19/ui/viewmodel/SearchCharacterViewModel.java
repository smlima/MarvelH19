package com.slima.marvelh19.ui.viewmodel;

import com.slima.marvelh19.BR;
import com.slima.marvelh19.R;
import com.slima.marvelh19.model.characters.CharacterResult;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.util.List;

/**
 * Created by sergio.lima on 04/04/2016.
 */
public class SearchCharacterViewModel extends CharactersViewModel {


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
}
