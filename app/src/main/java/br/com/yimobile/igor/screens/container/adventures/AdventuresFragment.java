package br.com.yimobile.igor.screens.container.adventures;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;

public class AdventuresFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    AdventuresRecyclerViewAdapter adventuresRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<Adventure> adventuresArrayList = new ArrayList<>();
    FloatingActionButton newAdventureButton;
    SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adventures, null);

        newAdventureButton = view.findViewById(R.id.new_adventure_button);
        newAdventureButton.setOnClickListener(floatingActionOnClickListener);
        newAdventureButton.setImageResource(R.drawable.float_button_new_adventure);

        recyclerView = view.findViewById(R.id.adventures_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adventuresRecyclerViewAdapter = new AdventuresRecyclerViewAdapter(adventuresArrayList, getActivity());
        recyclerView.setAdapter(adventuresRecyclerViewAdapter);

        swipeLayout = view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getActivity().getResources().getColor(android.R.color.holo_blue_bright),
                getActivity().getResources().getColor(android.R.color.holo_green_light),
                getActivity().getResources().getColor(android.R.color.holo_orange_light),
                getActivity().getResources().getColor(android.R.color.holo_red_light));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Adventure> adventureList = ((ContainerActivity) getActivity()).getUserAdventures();
        if(adventureList != null){
            fillFragment(adventureList);
        }
    }

    View.OnClickListener floatingActionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((ContainerActivity) getActivity()).onNewAdventureClicked();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        ((ContainerActivity) getActivity()).refreshUserAdventure();
    }

    public void fillFragment(List<Adventure> adventureList) {
        adventuresArrayList.clear();
        for (Adventure adventure : adventureList) {
            addNewAdventure(adventure);
        }
        if (swipeLayout != null) {
            swipeLayout.setRefreshing(false);
            swipeLayout.destroyDrawingCache();
            swipeLayout.clearAnimation();
        }
    }

    public interface NewAdventureOnClickListener {
        public void onNewAdventureClicked();
        public void onAdventureItemClicked(int itemPosition, Adventure adventure);
    };

    public void addNewAdventure(Adventure adv){
        adventuresArrayList.add(adv);
        adventuresRecyclerViewAdapter.swap();
    }
}
