package com.slima.marvelh19.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.slima.marvelh19.R;
import com.slima.marvelh19.databinding.FragmentCarousselImagesBinding;
import com.slima.marvelh19.ui.viewmodel.ImagesThumbnailsModelInterfaces;

/**
 * Created by sergio.lima on 07/04/2016.
 */
public class CarouselImagesFragment extends Fragment {

    ImagesThumbnailsModelInterfaces mGenericImagesViewModel;

    public void setGenericImagesViewModel(ImagesThumbnailsModelInterfaces genericImagesViewModel) {
        mGenericImagesViewModel = genericImagesViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        FragmentCarousselImagesBinding viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_caroussel_images, container,false);

        viewDataBinding.setVm(mGenericImagesViewModel);

        mGenericImagesViewModel.setForcedLayout(R.layout.uicomponent_images_big);

        LinearLayoutManager linearLayoutManagerEvents = new LinearLayoutManager(getActivity());
        linearLayoutManagerEvents.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.genericCarousselRecycler.setLayoutManager(linearLayoutManagerEvents);

        return viewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    /**
     * creates a fragment instance of this @{link MarvelCharactersFragment} type
     *
     * @return the new fragment
     * @param viewModel
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

        switch (item.getItemId()){
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
