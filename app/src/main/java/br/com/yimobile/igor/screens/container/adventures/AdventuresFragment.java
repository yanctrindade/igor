package br.com.yimobile.igor.screens.container.adventures;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;

public class AdventuresFragment extends Fragment {

    AdventuresRecyclerViewAdapter adventuresRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<String> adventuresArrayList = new ArrayList<>();
    FloatingActionButton newAdventureButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.fragment_adventures, null);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newAdventureButton = (FloatingActionButton) view.findViewById(R.id.new_adventure_button);
        newAdventureButton.setOnClickListener(floatingActionOnClickListener);
        newAdventureButton.setImageResource(R.drawable.float_button_new_adventure);

        recyclerView = (RecyclerView) view.findViewById(R.id.adventures_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        
        adventuresRecyclerViewAdapter = new AdventuresRecyclerViewAdapter(adventuresArrayList, getActivity());
        recyclerView.setAdapter(adventuresRecyclerViewAdapter);
    }

    View.OnClickListener floatingActionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((ContainerActivity) getActivity()).onNewAdventureClicked();
        }
    };

    public interface NewAdventureOnClickListener {
        public void onNewAdventureClicked();
        public void onAdventureItemClicked(int itemPosition);
    };

    public void addNewAdventure(String name){
        adventuresArrayList.add(name);
    }
}
