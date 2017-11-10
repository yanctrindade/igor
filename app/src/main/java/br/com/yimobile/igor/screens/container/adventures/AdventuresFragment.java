package br.com.yimobile.igor.screens.container.adventures;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;
import database.User;

public class AdventuresFragment extends Fragment {

    AdventuresRecyclerViewAdapter adventuresRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<Adventure> adventuresArrayList = new ArrayList<>();
    FloatingActionButton newAdventureButton;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adventures, null);

        newAdventureButton = (FloatingActionButton) view.findViewById(R.id.new_adventure_button);
        newAdventureButton.setOnClickListener(floatingActionOnClickListener);
        newAdventureButton.setImageResource(R.drawable.float_button_new_adventure);

        recyclerView = (RecyclerView) view.findViewById(R.id.adventures_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adventuresRecyclerViewAdapter = new AdventuresRecyclerViewAdapter(adventuresArrayList, getActivity());
        recyclerView.setAdapter(adventuresRecyclerViewAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User active = dataSnapshot.getValue(User.class);
                        List<String> aventuras = active.getAventuras();
                        if(aventuras != null && !aventuras.isEmpty()){
                            for(int i = aventuras.size() - 1; i >= 0; i--){
                                mDatabase.child("adventure").child(aventuras.get(i))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Adventure adventure = dataSnapshot.getValue(Adventure.class);
                                            addNewAdventure(adventure);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {}
                                    });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    View.OnClickListener floatingActionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((ContainerActivity) getActivity()).onNewAdventureClicked();
        }
    };

    public interface NewAdventureOnClickListener {
        public void onNewAdventureClicked();
        public void onAdventureItemClicked(int itemPosition, Adventure adventure);
    };

    public void addNewAdventure(Adventure adv){
        adventuresArrayList.add(adv);
        Log.d("ADVLIST", adventuresArrayList.size()+" TAMANHO");
        adventuresRecyclerViewAdapter.swap();
    }
}
