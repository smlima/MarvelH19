package com.slima.marvelh19.fragments;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.slima.marvelh19.R;
import com.slima.marvelh19.activities.MainActivity;
import com.slima.marvelh19.databinding.FragmentCharactersListBinding;
import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.Url;
import com.slima.marvelh19.ui.viewmodel.CharactersViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Fragment responsible to display the list of characters
 * from the db and request the download service to download more
 * if required.
 * <p/>
 * When scrolling keep downloading more if reaching the end of the list.
 * When new items are inserted on the table they will be displayed through the databinding
 * <p/>
 * Created by sergio.lima on 31/03/2016.
 */
public class MarvelCharactersFragment extends Fragment {

    private static final String TAG = "MCFragment";

    /**
     * Characters view model
     */
    CharactersViewModel chars = new CharactersViewModel();

    /**
     * empty constructor
     */
    public MarvelCharactersFragment() {

    }

    /**
     * reference to the activity
     */
    WeakReference<MainActivity> mMainActivityWeakReference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mMainActivityWeakReference = new WeakReference<MainActivity>(((MainActivity) context));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentCharactersListBinding inflate = DataBindingUtil.inflate(inflater, R.layout.fragment_characters_list, container, false);

        inflate.setLista(chars);

        if (mMainActivityWeakReference == null || mMainActivityWeakReference.get() == null) {
            if (getActivity() instanceof MainActivity) {
                mMainActivityWeakReference = new WeakReference<MainActivity>(((MainActivity) getActivity()));
            } else {
                getActivity().finish();
            }
        }

        inflate.setView(mMainActivityWeakReference.get());  // link fragment to the view for the click handler
        inflate.recyclerPrimary.setLayoutManager(new LinearLayoutManager(getActivity()));

        inflate.recyclerPrimary.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.d("SLIMA", "onScrolled() called with: " + "recyclerView = [" + recyclerView + "], dx = [" + dx + "], dy = [" + dy + "]");
                //int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                //int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                // Log.d("SLIMA", "onScrolled() returned: " + lastCompletelyVisibleItemPosition + ", " + lastVisibleItemPosition);

                if (firstCompletelyVisibleItemPosition > chars.lista.size() - 4) {
                    mMainActivityWeakReference.get().getDownloadManager().loadMore();
                }

            }

        });

        mMainActivityWeakReference.get().getDownloadManager().loadInit();

        List<CharacterResult> characterResults = SQLite.select().from(CharacterResult.class).orderBy(OrderBy.fromString("name")).queryList();

        for (CharacterResult characterResult : characterResults) {
            chars.addCharacter(characterResult);
        }

        View root = inflate.getRoot();

        return root;
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

            StringBuilder sb = new StringBuilder();

            for (SQLCondition primaryKeyValue : primaryKeyValues) {
                sb.append(primaryKeyValue.columnName())
                        .append(":")
                        .append(primaryKeyValue.value())
                        .append("   ,  ");
            }

            Log.d(TAG, "onModelStateChanged() called with: " + "table = [" + table + "], action = [" + action + "], primaryKeyValues = [" + sb.toString() + "]");

            Select select = SQLite.select();
            From<CharacterResult> from = select.from(CharacterResult.class);

            for (SQLCondition primaryKeyValue : primaryKeyValues) {
                from.having(primaryKeyValue);
            }

            final List<CharacterResult> characterResults = from.queryList();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for (CharacterResult characterResult : characterResults) {
                        if (characterResult.getThumbnail() != null && characterResult.getThumbnail().hasImageAvailable()) {
                            chars.addCharacter(characterResult);
                        }

                        for (Url url : characterResult.getUrls()) {
                            Log.d(TAG, "run() called with: " + "url= " + url);
                        }
                    }

                }
            });

        }
    };

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
     * creates a fragment instance of this @{link MarvelCharactersFragment} type
     *
     * @return the new fragment
     */
    public static Fragment newInstance() {
        return new MarvelCharactersFragment();
    }

}
