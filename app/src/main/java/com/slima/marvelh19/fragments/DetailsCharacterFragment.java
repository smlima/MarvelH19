package com.slima.marvelh19.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.slima.marvelh19.R;
import com.slima.marvelh19.activities.DetailsActivity;
import com.slima.marvelh19.databinding.ActivityCharacterDetailsBinding;
import com.slima.marvelh19.model.ThumbnailModelResultsInterfaces;
import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.ComicsResults;
import com.slima.marvelh19.model.characters.EventsResult;
import com.slima.marvelh19.model.characters.SeriesResult;
import com.slima.marvelh19.model.characters.StoriesResult;
import com.slima.marvelh19.viewmodel.ComicsViewModel;
import com.slima.marvelh19.viewmodel.EventsViewModel;
import com.slima.marvelh19.viewmodel.SeriesViewModel;
import com.slima.marvelh19.viewmodel.StoriesViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.ClickHandler;

import java.lang.ref.WeakReference;

/**
 * Fragment responsible to render the Details of a character
 * <p/>
 * Created by sergio.lima on 07/04/2016.
 */
public class DetailsCharacterFragment extends Fragment {

    /**
     * Log Tag
     */
    private static final String TAG = "DetailsCharacerFragment";
    /**
     * view models for the activity
     */
    private CharacterResult charactersViewModel;
    private ComicsViewModel comicsModel;
    private SeriesViewModel mSeriesViewModel;
    private StoriesViewModel storiesViewModel;
    private EventsViewModel eventsViewModel;


    /**
     * reference to the activity
     */
    private WeakReference<DetailsActivity> mMainActivityWeakReference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DetailsActivity) {
            mMainActivityWeakReference = new WeakReference<DetailsActivity>(((DetailsActivity) context));

            mMainActivityWeakReference.get().setClickHandler(mOnClickListener);
        }
    }

    /**
     * handler for the clicks on the text views
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String url = null;
            switch (view.getId()) {
                case R.id.character_details_related_links_comiclink_title:
                    url = charactersViewModel.getComicLink();
                    break;
                case R.id.character_details_related_links_wiki_title:
                    url = charactersViewModel.getWikiLink();
                    break;
                case R.id.character_details_related_links_detail_title:
                    url = charactersViewModel.getDetailLink();
                    break;
            }

            if (url != null) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), R.string.relatedlinks_not_exist, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Integer characterId = getActivity().getIntent().getIntExtra(DetailsActivity.CHARACTER_EXTRA, -1);

        if (characterId != null && characterId != -1) {
            charactersViewModel = SQLite.select()
                    .from(CharacterResult.class)
                    .where(Condition.column(new NameAlias("id")).eq(characterId))
                    .querySingle();
        } else {
            // no character to display
            return container;
        }

        ActivityCharacterDetailsBinding viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.activity_character_details, container, false);

        viewDataBinding.setCharacter(charactersViewModel);

        // fetch COMICS
        comicsModel = new ComicsViewModel(getActivity(), charactersViewModel.getId());
        comicsModel.setComicsResultsClickHandler(mComicsResultsClickHandler);
        viewDataBinding.setListacomics(comicsModel);

        // fetch SERIES
        mSeriesViewModel = new SeriesViewModel(getActivity(),charactersViewModel.getId());
        mSeriesViewModel.setSeriesResultClickHandler(mComicsResultsClickHandler);
        viewDataBinding.setListaseries(mSeriesViewModel);

        // fetch SERIES
        storiesViewModel = new StoriesViewModel(getActivity(),charactersViewModel.getId());
        storiesViewModel.setStoriesResultClickHandler(mComicsResultsClickHandler);
        viewDataBinding.setListastories(storiesViewModel);

        // fetch SERIES
        eventsViewModel = new EventsViewModel(getActivity(),charactersViewModel.getId());
        eventsViewModel.setEventsResultClickHandler(mComicsResultsClickHandler);
        viewDataBinding.setListaevents(eventsViewModel);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.characterDetailsComicsRecycler.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerSeries = new LinearLayoutManager(getActivity());
        linearLayoutManagerSeries.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.characterDetailsSeriesRecycler.setLayoutManager(linearLayoutManagerSeries);

        LinearLayoutManager linearLayoutManagerStories = new LinearLayoutManager(getActivity());
        linearLayoutManagerStories.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.characterDetailsStoriesRecycler.setLayoutManager(linearLayoutManagerStories);

        LinearLayoutManager linearLayoutManagerEvents = new LinearLayoutManager(getActivity());
        linearLayoutManagerEvents.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.characterDetailsEventsRecycler.setLayoutManager(linearLayoutManagerEvents);

        // trigger a fetch for details data of the character
        mMainActivityWeakReference.get().getDownloadManager().loadCharacterComics(charactersViewModel.getId());

        viewDataBinding.characterDetailsScrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch() called with: " + "v = [" + v + "], event = [" + event + "]");

                previous -= event.getY();

                int delta = (int)previous;

                Log.d(TAG, "onTouch() returned Y: " + (int)event.getY());
                Log.d(TAG, "onTouch() returned: " + delta);

                mMainActivityWeakReference.get().getSupportActionBar().setHideOffset(delta);

                return false;
            }
        });

        return viewDataBinding.getRoot();
    }

    long previous = 0l;

    @Override
    public void onResume() {
        super.onResume();

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        comicsModel.start();
        mSeriesViewModel.start();
        storiesViewModel.start();
        eventsViewModel.start();

    }

    @Override
    public void onStop() {
        super.onStop();

        comicsModel.stop();
        mSeriesViewModel.stop();
        storiesViewModel.stop();
        eventsViewModel.stop();
    }

    /**
     * Handler for when click on one of the recycler views with the thumbnails
     */
    ClickHandler<ThumbnailModelResultsInterfaces> mComicsResultsClickHandler = new ClickHandler<ThumbnailModelResultsInterfaces>() {
        @Override
        public void onClick(ThumbnailModelResultsInterfaces viewModel) {

            Fragment fragment;
            if (viewModel instanceof ComicsResults) {
                fragment = CarouselImagesFragment.newInstance(comicsModel);
            } else if (viewModel instanceof SeriesResult) {
                fragment = CarouselImagesFragment.newInstance(mSeriesViewModel);
            } else if (viewModel instanceof StoriesResult) {
                fragment = CarouselImagesFragment.newInstance(storiesViewModel);
            } else if (viewModel instanceof EventsResult) {
                fragment = CarouselImagesFragment.newInstance(eventsViewModel);
            } else {
                return;
            }

            getFragmentManager().beginTransaction()
                    .replace(R.id.contentPanel, fragment)
                    .addToBackStack(null)
                    .commit();

        }
    };

    /**
     * get a new instance of a @{link DetailsCharacterFragment}
     *
     * @return a new fragment
     */
    public static Fragment newInstance() {
        return new DetailsCharacterFragment();
    }
}
