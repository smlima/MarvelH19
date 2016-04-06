package com.slima.marvelh19.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.slima.marvelh19.R;
import com.slima.marvelh19.fragments.MarvelCharactersFragment;
import com.slima.marvelh19.fragments.SearchFragment;
import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.Url;

import net.droidlabs.mvvm.recyclerview.adapter.ClickHandler;

import java.lang.ref.WeakReference;

/**
 * Main activity with app theme
 *
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private WeakReference<MainActivity> mMainActivityWeakReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivityWeakReference = new WeakReference<MainActivity>(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_marvel_characters));
        }

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction()
                .replace(R.id.contentPanel, MarvelCharactersFragment.newInstance())
                .commit();
    }


    private void showSearchFragment(WeakReference<SearchView> searchViewWeakReference) {
        getFragmentManager().beginTransaction()
                .replace(R.id.contentPanel, SearchFragment.newInstance(searchViewWeakReference))
                .addToBackStack(null)
                .commit();
    }

    private void hideSearchFragment() {

        if (getFragmentManager()!= null && !getFragmentManager().isDestroyed()) {
            getFragmentManager().popBackStack();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu , menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Log.d(TAG, "onViewAttachedToWindow() called with: " + "v = [" + v + "]");

                showSearchFragment(new WeakReference<SearchView>(searchView));
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Log.d(TAG, "onViewDetachedFromWindow() called with: " + "v = [" + v + "]");

                hideSearchFragment();
            }
        });

        searchView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d(TAG, "onLayoutChange() called with: " + "v = [" + v + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "], oldLeft = [" + oldLeft + "], oldTop = [" + oldTop + "], oldRight = [" + oldRight + "], oldBottom = [" + oldBottom + "]");
            }
        });

        return true;
    }

    public ClickHandler<CharacterResult> clickHandler()
    {
        return new ClickHandler<CharacterResult>()
        {
            @Override
            public void onClick(CharacterResult characterResult)
            {

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

                for (Url url : characterResult.getUrls()) {
                    Log.d(TAG, "t = " + url.getType() + " || " + url.getUrl() );
                }

                intent.putExtra(DetailsActivity.CHARACTER_EXTRA, characterResult.getId());

                startActivity(intent);

                //push from bottom to top
                overridePendingTransition(R.anim.push_up_in, R.anim.no_action);


                Log.d("SLIMA", "onClick() called with: " + "characterResult = [" + characterResult + "]");

            }
        };
    }

}
