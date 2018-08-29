package cr.ac.tec.conalapp.conalapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




/**
 * A simple {@link Fragment} subclass.
 */
public class BoletinFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private View view;

    public BoletinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_boletin, container, false);
        inicializarComponentes(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void inicializarComponentes(View pView)
    {
        swipeLayout = (SwipeRefreshLayout) pView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark);
    }

    /**
     * As a trick, you may be found interesting to disable manual swipe gesture,
     * maybe temporarily or because you only want to show progress animation programmatically.
     * What you need to do is to use the method setEnabled() and set it to false.
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 5000);
    }

}
