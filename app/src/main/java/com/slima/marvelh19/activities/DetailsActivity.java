package com.slima.marvelh19.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.slima.marvelh19.R;
import com.slima.marvelh19.fragments.DetailsCharacterFragment;

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

    /** the lclick listener */
    private View.OnClickListener mClickHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // make sure the title is disabled as per wireframes
            actionBar.setDisplayShowTitleEnabled(false);
            // actionBar.setTitle(charactersViewModel.getName());
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.contentPanel, DetailsCharacterFragment.newInstance())
                .addToBackStack(null)
                .commit();

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


        Log.d(TAG, "onBackPressed() called with: fragments count= " + getFragmentManager().getBackStackEntryCount());

        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.no_action, R.anim.push_up_out);
        }

    }

    /**
     * set the click listener
     * @param clickHandler
     */
    public void setClickHandler(View.OnClickListener clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * receiver of the click request on the text view
     * @param view  the view that trigger the click
     */
    public void onLinkClick(View view) {
        // delegate to the handler... which should go to the fragment
        if (mClickHandler != null) {
            mClickHandler.onClick(view);
        }
    }


}
