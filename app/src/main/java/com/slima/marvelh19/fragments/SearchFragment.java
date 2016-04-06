package com.slima.marvelh19.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.slima.marvelh19.R;
import com.slima.marvelh19.activities.MainActivity;
import com.slima.marvelh19.databinding.FragmentSearchBinding;
import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.ui.viewmodel.SearchCharacterViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Search fragment for displaying the search layout
 * <p/>
 * Created by sergio.lima on 04/04/2016.
 */
public class SearchFragment extends Fragment {

    /**
     * Log Tag
     */
    private static final String TAG = "SearchFragment";

    /**
     * activity reference
     */
    private WeakReference<MainActivity> mMainActivityWeakReference;

    /**
     * the search view reference
     */
    private WeakReference<SearchView> mSearchViewWeakReference;

    /**
     * Search view model
     */
    private SearchCharacterViewModel chars = new SearchCharacterViewModel();

    /**
     * instanciate a new Search Fragment and set the weak reference for the searchview to be able to
     * attach listeners to it, for listening on text changes
     *
     * @param searchViewWeakReference the view reference
     * @return the fragment
     */
    public static SearchFragment newInstance(WeakReference<SearchView> searchViewWeakReference) {
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setSearchViewWeakReference(searchViewWeakReference);
        return searchFragment;
    }

    /**
     * set the search view weak reference
     */
    public void setSearchViewWeakReference(WeakReference<SearchView> searchViewWeakReference) {
        mSearchViewWeakReference = searchViewWeakReference;
    }

    /**
     * the query listener for attaching into the search view
     */
    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            //Log.d(TAG, "onQueryTextSubmit() called with: " + "query = [" + query + "]");
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            //Log.d(TAG, "onQueryTextChange() called with: " + "newText = [" + newText + "]");

            SQLCondition condition = Condition.column(new NameAlias("name")).like(newText + "%");

            if (mMainActivityWeakReference!=null && mMainActivityWeakReference.get()!=null) {
                mMainActivityWeakReference.get().getDownloadManager().loadSearchCharacter(newText);
            }

            List<CharacterResult> characterResults = SQLite.select().from(CharacterResult.class).where(condition).queryList();

            chars.replaceAll(characterResults);

            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentSearchBinding inflate = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        inflate.setLista(chars);

        if (mMainActivityWeakReference == null || mMainActivityWeakReference.get() == null) {
            if (getActivity() instanceof MainActivity) {
                mMainActivityWeakReference = new WeakReference<MainActivity>(((MainActivity) getActivity()));
            } else {
                getActivity().finish();
            }
        }
        inflate.setView(mMainActivityWeakReference.get());  // link fragment to the view for the click handler

        View root = inflate.getRoot();

        inflate.recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mSearchViewWeakReference != null && mSearchViewWeakReference.get() != null) {
            mSearchViewWeakReference.get().setOnQueryTextListener(mOnQueryTextListener);
        }

        return root;
    }

    @Override
    public void onDestroy() {

        // remove listener from the view, just in case...
        if (mSearchViewWeakReference != null && mSearchViewWeakReference.get() != null) {
            mSearchViewWeakReference.get().setOnQueryTextListener(null);
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        // rgister for changes on the CharacterResult table
        observer.registerForContentChanges(getActivity(), CharacterResult.class);
        observer.addModelChangeListener(modelChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        // unregister for the changes on the table
        observer.unregisterForContentChanges(getActivity());
    }


    /**
     * the dbflow observer
     */
    private FlowContentObserver observer = new FlowContentObserver();

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
            StringBuilder sb = new StringBuilder();

            Condition[] conditions = new Condition[primaryKeyValues.length];

            for (int i = 0; i < primaryKeyValues.length; i++) {
                conditions[i] = Condition.column(new NameAlias("id")).eq(primaryKeyValues[i].value());
            }

            Log.d(TAG, "onModelStateChanged() called with: " + "table = [" + table + "], action = [" + action + "], primaryKeyValues = [" + sb.toString() + "]");

            final List<CharacterResult> characterResults = SQLite.select().from(CharacterResult.class).where(conditions).queryList();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for (CharacterResult characterResult : characterResults) {
                        if (characterResult.getThumbnail() != null && characterResult.getThumbnail().hasImageAvailable()) {
                            chars.addCharacter(characterResult);
                        }
                    }

                }
            });

        }
    };
}
