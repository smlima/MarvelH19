package com.slima.marvelh19.fragments;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slima.marvelh19.R;
import com.slima.marvelh19.activities.MainActivity;
import com.slima.marvelh19.databinding.FragmentCharactersListBinding;
import com.slima.marvelh19.viewmodel.CharactersViewModel;

import java.lang.ref.WeakReference;

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
    CharactersViewModel chars ;

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

        // set the view model
        chars = new CharactersViewModel(getActivity());

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
                int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstCompletelyVisibleItemPosition();

                if (firstCompletelyVisibleItemPosition > chars.lista.size() - 4) {
                    mMainActivityWeakReference.get().getDownloadManager().loadMore();
                }

            }

        });

        mMainActivityWeakReference.get().getDownloadManager().loadInit();

        chars.preload();

        View root = inflate.getRoot();

        return root;
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

    /**
     * creates a fragment instance of this @{link MarvelCharactersFragment} type
     *
     * @return the new fragment
     */
    public static Fragment newInstance() {
        return new MarvelCharactersFragment();
    }

}
