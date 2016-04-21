package com.slima.marvelh19.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slima.marvelh19.R;
import com.slima.marvelh19.activities.MainActivity;
import com.slima.marvelh19.databinding.FragmentSearchBinding;
import com.slima.marvelh19.viewmodel.SearchCharacterViewModel;

import java.lang.ref.WeakReference;

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
    private SearchCharacterViewModel chars ;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        chars = new SearchCharacterViewModel(getActivity());

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
            mSearchViewWeakReference.get().setOnQueryTextListener(chars);
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

        chars.start();
    }

    @Override
    public void onStop() {
        super.onStop();

        chars.stop();

    }


}
