package com.slima.marvelh19.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.slima.marvelh19.R;
import com.slima.marvelh19.activities.DetailsActivity;
import com.slima.marvelh19.databinding.ActivityCharacterDetailsBinding;
import com.slima.marvelh19.model.ThumbnailModelResultsInterfaces;
import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.ComicsResults;
import com.slima.marvelh19.model.characters.EventsResult;
import com.slima.marvelh19.model.characters.SeriesResult;
import com.slima.marvelh19.model.characters.StoriesResult;
import com.slima.marvelh19.model.characters.Url;
import com.slima.marvelh19.ui.viewmodel.ComicsViewModel;
import com.slima.marvelh19.ui.viewmodel.EventsViewModel;
import com.slima.marvelh19.ui.viewmodel.SeriesViewModel;
import com.slima.marvelh19.ui.viewmodel.StoriesViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.ClickHandler;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by sergio.lima on 07/04/2016.
 */
public class DetailsCharacterFragment extends Fragment {


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
     * DBFlow observer
     */
    private FlowContentObserver observer = new FlowContentObserver();

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
            switch (view.getId()){
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

            if (url != null){

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


        ActivityCharacterDetailsBinding viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.activity_character_details, container,false);

        viewDataBinding.setCharacter(charactersViewModel);

        for (Url url : charactersViewModel.getUrls()) {
            Log.d(TAG, "t = " + url.getType() + " || " + url.getUrl() );

        }

        SQLCondition characterIdCondition = Condition.column(new NameAlias("characterId")).eq(charactersViewModel.getId());

        // fetch COMICS
        comicsModel = new ComicsViewModel();
        comicsModel.setComicsResultsClickHandler(mComicsResultsClickHandler);
        List<ComicsResults> comicsResultses = SQLite.select().from(ComicsResults.class).where(characterIdCondition).queryList();
        comicsModel.addObjects(comicsResultses);
        viewDataBinding.setListacomics(comicsModel);


        // fetch SERIES
        mSeriesViewModel = new SeriesViewModel();
        mSeriesViewModel.setSeriesResultClickHandler(mComicsResultsClickHandler);
        List<SeriesResult> seriesResults = SQLite.select().from(SeriesResult.class).where(characterIdCondition).queryList();
        mSeriesViewModel.addObjects(seriesResults);
        viewDataBinding.setListaseries(mSeriesViewModel);

        // fetch SERIES
        storiesViewModel = new StoriesViewModel();
        storiesViewModel.setStoriesResultClickHandler(mComicsResultsClickHandler);
        List<StoriesResult> storiesResult = SQLite.select().from(StoriesResult.class).where(characterIdCondition).queryList();
        storiesViewModel.addObjects(storiesResult);
        viewDataBinding.setListastories(storiesViewModel);

        // fetch SERIES
        eventsViewModel = new EventsViewModel();
        eventsViewModel.setEventsResultClickHandler(mComicsResultsClickHandler);
        List<EventsResult> eventsResults = SQLite.select().from(EventsResult.class).where(characterIdCondition).queryList();
        eventsViewModel.addObjects(eventsResults);
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

        return viewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        observer.registerForContentChanges(getActivity(), ComicsResults.class);
        observer.addModelChangeListener(modelChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        observer.unregisterForContentChanges(getActivity());

    }

    /**
     * obersver for the Table
     */
    private FlowContentObserver.OnModelStateChangedListener modelChangeListener = new FlowContentObserver.OnModelStateChangedListener() {
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

            final List<ComicsResults> characterResults = SQLite.select().from(ComicsResults.class).where(conditions).queryList();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for (ComicsResults characterResult : characterResults) {
                        if (characterResult.getThumbnail() != null && characterResult.getThumbnail().hasImageAvailable()) {
                            comicsModel.addObject(characterResult);
                        }
                    }

                }
            });


        }
    };

    ClickHandler<ThumbnailModelResultsInterfaces> mComicsResultsClickHandler = new ClickHandler<ThumbnailModelResultsInterfaces>() {
        @Override
        public void onClick(ThumbnailModelResultsInterfaces viewModel) {

            Fragment fragment ;
            if (viewModel instanceof ComicsResults){
                fragment =  CarouselImagesFragment.newInstance(comicsModel);
            } else if (viewModel instanceof SeriesResult){
                fragment =  CarouselImagesFragment.newInstance(mSeriesViewModel);
            } else if (viewModel instanceof StoriesResult){
                fragment =  CarouselImagesFragment.newInstance(storiesViewModel);
            } else if (viewModel instanceof EventsResult){
                fragment =  CarouselImagesFragment.newInstance(eventsViewModel);
            } else {
                return;
            }


            getFragmentManager().beginTransaction()
                .replace(R.id.contentPanel, fragment)
                .addToBackStack(null)
                .commit();


        }
    };

    public static Fragment newInstance() {
        return new DetailsCharacterFragment();
    }
}
