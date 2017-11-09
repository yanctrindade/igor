package br.com.yimobile.igor.screens.container.adventures.andamento;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import br.com.yimobile.igor.screens.container.adventures.andamento.ItensList.ItensRecyclerViewAdapter;
import br.com.yimobile.igor.screens.container.adventures.andamento.ItensList.PlayerRecyclerViewAdapter;


public class PlayersFragment extends Fragment {

    PlayerRecyclerViewAdapter playerRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<String> playerArrayList = new ArrayList<>();
    FloatingActionButton newPlayerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_players, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        newPlayerButton = (FloatingActionButton) view.findViewById(R.id.new_player_button);
        newPlayerButton.setOnClickListener(newPlayerOnClickListener);

        ImageButton andamento_button = view.findViewById(R.id.button_andamento);
        andamento_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ContainerActivity) getActivity()).onResumePressed();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.sessions_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        playerArrayList.add("17/05 Sessão 5");
        playerArrayList.add("16/05 Sessão 4");
        playerRecyclerViewAdapter = new PlayerRecyclerViewAdapter(playerArrayList, getActivity());
        recyclerView.setAdapter(playerRecyclerViewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editar:
                return true;
            case R.id.action_ordenar:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener newPlayerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((ContainerActivity) getActivity()).newPlayerPressed();
        }
    };

    public interface PlayersOnClickListener {
        public void onResumePressed();
    }

}
