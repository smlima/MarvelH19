package com.slima.marvelh19.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.slima.marvelh19.R;
import com.slima.marvelh19.databinding.FragmentCarousselImagesBinding;
import com.slima.marvelh19.viewmodel.ImagesThumbnailsModelInterfaces;

/**
 * Fragment responsible to display the caroussel with the thumbnails
 * <p/>
 * Created by sergio.lima on 07/04/2016.
 */
public class CarouselImagesFragment extends Fragment {

    /**
     * Log Tag
     */
    private static final String TAG = "CarousselFragment";
    /**
     * Model view with the thumbnails
     */
    private ImagesThumbnailsModelInterfaces mGenericImagesViewModel;
    /**
     * Layout manager for the recycler view
     */
    private LinearLayoutManager mLinearLayoutManagerEvents;
    /**
     * The binding
     */
    private FragmentCarousselImagesBinding mViewDataBinding;

    /**
     * Sets the view model to be displayed
     *
     * @param genericImagesViewModel
     */
    public void setGenericImagesViewModel(ImagesThumbnailsModelInterfaces genericImagesViewModel) {
        mGenericImagesViewModel = genericImagesViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_caroussel_images, container, false);

        mViewDataBinding.setVm(mGenericImagesViewModel);

        mGenericImagesViewModel.setForcedLayout(R.layout.uicomponent_images_big);

        mLinearLayoutManagerEvents = new LinearLayoutManager(getActivity()) {

            @Override
            public void onScrollStateChanged(int state) {
                super.onScrollStateChanged(state);
                updateCarousselCounter(mLinearLayoutManagerEvents.findFirstVisibleItemPosition());
            }

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                Log.d(TAG, "onLayoutChildren() called with: " + "recycler = [" + recycler + "], state = [" + state + "]");
            }
        };
        mLinearLayoutManagerEvents.setOrientation(LinearLayoutManager.HORIZONTAL);
        mViewDataBinding.genericCarousselRecycler.setLayoutManager(mLinearLayoutManagerEvents);


        return mViewDataBinding.getRoot();
    }

    /**
     * update the bottom counter with the current position
     * adding 1 , because the position is 0 based.
     *
     * @param recyclerViewPosition the recycler view position of the item
     */
    private void updateCarousselCounter(int recyclerViewPosition) {

        mViewDataBinding.carousselItemCounterTextview.setText(
                getString(R.string.carousselCounter,
                        recyclerViewPosition + 1,
                        mGenericImagesViewModel.lista.size()));

    }

    @Override
    public void onResume() {
        super.onResume();

        updateCarousselCounter(mLinearLayoutManagerEvents.findFirstVisibleItemPosition());

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    /**
     * creates a fragment instance of this @{link MarvelCharactersFragment} type
     *
     * @param viewModel
     * @return the new fragment
     */
    public static Fragment newInstance(ImagesThumbnailsModelInterfaces viewModel) {
        CarouselImagesFragment carouselImagesFragment = new CarouselImagesFragment();

        carouselImagesFragment.setGenericImagesViewModel(viewModel);

        return carouselImagesFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu, menu);

        MenuItem search = menu.findItem(R.id.menu_search);
        MenuItem clear = menu.findItem(R.id.menu_clear);

        search.setVisible(false);
        clear.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_clear:
                // close, and go back
                getFragmentManager().popBackStack();
                return true;
            default:
                // default
                return super.onOptionsItemSelected(item);
        }
    }
}
