package com.slima.marvelh19.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.slima.marvelh19.R;
import com.slima.marvelh19.databinding.ActivityCharacterDetailsBinding;
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

import java.util.List;

/**
 * Details Activity
 * the theme has translucid actionbar as per wireframes
 * <p/>
 * Created by sergio.lima on 02/04/2016.
 */
public class DetailsActivity extends BaseActivity {

    /**
     * key for serializing a character
     */
    public static final String CHARACTER_EXTRA = "CHARACTER_EXTRA";
    /**
     * log tag
     */
    private static final String TAG = "DetailsActivity";

    /**
     * view models for the activity
     */
    private CharacterResult charactersViewModel;
    private ComicsViewModel comicsModel;
    private SeriesViewModel mSeriesViewModel;

    /**
     * DBFlow observer
     */
    private FlowContentObserver observer = new FlowContentObserver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        Integer characterId = getIntent().getIntExtra(CHARACTER_EXTRA, -1);

        if (characterId != null && characterId != -1) {
            charactersViewModel = SQLite.select()
                    .from(CharacterResult.class)
                    .where(Condition.column(new NameAlias("id")).eq(characterId))
                    .querySingle();
        } else {
            // no character to display
            finish();
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // make sure the title is disabled as per wireframes
            actionBar.setDisplayShowTitleEnabled(false);
            // actionBar.setTitle(charactersViewModel.getName());
        }

        ActivityCharacterDetailsBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_character_details);

        viewDataBinding.setCharacter(charactersViewModel);

        for (Url url : charactersViewModel.getUrls()) {
            Log.d(TAG, "t = " + url.getType() + " || " + url.getUrl() );

        }

        SQLCondition characterIdCondition = Condition.column(new NameAlias("characterId")).eq(charactersViewModel.getId());

        // fetch COMICS
        comicsModel = new ComicsViewModel();
        List<ComicsResults> comicsResultses = SQLite.select().from(ComicsResults.class).where(characterIdCondition).queryList();
        comicsModel.addObjects(comicsResultses);
        viewDataBinding.setListacomics(comicsModel);


        // fetch SERIES
        mSeriesViewModel = new SeriesViewModel();
        List<SeriesResult> seriesResults = SQLite.select().from(SeriesResult.class).where(characterIdCondition).queryList();
        mSeriesViewModel.addObjects(seriesResults);
        viewDataBinding.setListaseries(mSeriesViewModel);

        // fetch SERIES
        StoriesViewModel storiesViewModel = new StoriesViewModel();
        List<StoriesResult> storiesResult = SQLite.select().from(StoriesResult.class).where(characterIdCondition).queryList();
        storiesViewModel.addObjects(storiesResult);
        viewDataBinding.setListastories(storiesViewModel);

        // fetch SERIES
        EventsViewModel eventsViewModel = new EventsViewModel();
        List<EventsResult> eventsResults = SQLite.select().from(EventsResult.class).where(characterIdCondition).queryList();
        eventsViewModel.addObjects(eventsResults);
        viewDataBinding.setListaevents(eventsViewModel);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.characterDetailsComicsRecycler.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerSeries = new LinearLayoutManager(this);
        linearLayoutManagerSeries.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.characterDetailsSeriesRecycler.setLayoutManager(linearLayoutManagerSeries);

        LinearLayoutManager linearLayoutManagerStories = new LinearLayoutManager(this);
        linearLayoutManagerStories.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.characterDetailsStoriesRecycler.setLayoutManager(linearLayoutManagerStories);

        LinearLayoutManager linearLayoutManagerEvents = new LinearLayoutManager(this);
        linearLayoutManagerEvents.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDataBinding.characterDetailsEventsRecycler.setLayoutManager(linearLayoutManagerEvents);

        // trigger a fetch for details data of the character
        getDownloadManager().loadCharacterComics(charactersViewModel.getId());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.no_action, R.anim.push_up_out);

    }

    @Override
    protected void onResume() {
        super.onResume();

        observer.registerForContentChanges(this, ComicsResults.class);
        observer.addModelChangeListener(modelChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        observer.unregisterForContentChanges(this);
    }

    public void onLinkClick(View view ) {
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
            Toast.makeText(this, R.string.relatedlinks_not_exist, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * obersver for the Table
     */
    private FlowContentObserver.OnModelStateChangedListener modelChangeListener = new FlowContentObserver.OnModelStateChangedListener() {
        @Override
        public void onModelStateChanged(@Nullable Class<? extends Model> table, BaseModel.Action action, @NonNull SQLCondition[] primaryKeyValues) {

            StringBuilder sb = new StringBuilder();

            for (SQLCondition primaryKeyValue : primaryKeyValues) {
                sb.append(primaryKeyValue.columnName())
                        .append(":")
                        .append(primaryKeyValue.value())
                        .append("   ,  ");
            }

            //Log.d(TAG, "onModelStateChanged() called with: " + "table = [" + table + "], action = [" + action + "], primaryKeyValues = [" + sb.toString() + "]");

            Select select = SQLite.select();
            From<ComicsResults> from = select.from(ComicsResults.class);

            for (SQLCondition primaryKeyValue : primaryKeyValues) {
                from.having(primaryKeyValue);
            }

            final List<ComicsResults> characterResults = from.queryList();

            runOnUiThread(new Runnable() {
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

}
