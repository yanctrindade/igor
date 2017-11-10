package br.com.yimobile.igor.screens.container.adventures.andamento;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import br.com.yimobile.igor.screens.container.adventures.andamento.ItensList.ItensRecyclerViewAdapter;
import database.Adventure;

public class SessionsFragment extends Fragment {

    private static final String TAG = SessionsFragment.class.getSimpleName();

    ItensRecyclerViewAdapter sessionsRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<String> sessionsArrayList = new ArrayList<>();
    FloatingActionButton newSessionButton;
    Adventure adventure;
    TextView nameAdventure;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume, container, false);
        nameAdventure = (TextView) view.findViewById(R.id.titulo);
        if(adventure != null) nameAdventure.setText(adventure.getNome());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        newSessionButton = (FloatingActionButton) view.findViewById(R.id.new_session_button);
        newSessionButton.setOnClickListener(newSessionOnClickListener);

        ImageButton players_button = view.findViewById(R.id.button_jogadores);
        players_button.setOnClickListener(playersOnClickListener);

        recyclerView = (RecyclerView) view.findViewById(R.id.sessions_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        sessionsArrayList.add("17/05 Sessão 5");
        sessionsArrayList.add("16/05 Sessão 4");
        sessionsArrayList.add("15/05 Sessão 3");
        sessionsArrayList.add("13/05 Sessão 2");
        sessionsArrayList.add("10/05 Sessão 1");
        sessionsRecyclerViewAdapter = new ItensRecyclerViewAdapter(sessionsArrayList, getActivity());
        recyclerView.setAdapter(sessionsRecyclerViewAdapter);
    }

    public void SetAdventure(Adventure adventure){
        this.adventure = adventure;
        if(nameAdventure != null) nameAdventure.setText(adventure.getNome());
    }

    /* On Clicks */
    View.OnClickListener newSessionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((ContainerActivity) getActivity()).newSessionPressed(adventure);
        }
    };

    View.OnClickListener playersOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((ContainerActivity) getActivity()).onPlayersPressed(adventure);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editar:
                ((ContainerActivity) getActivity()).onEditAdventurePressed();
                return true;
            case R.id.action_ordenar:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeAdventureName(String name){

    }

    /* Public Interface for Listener */
    public interface ResumeOnClickListener {
        public void onPlayersPressed(Adventure adventure);
        public void onEditAdventurePressed();
        public void newSessionPressed(Adventure adventure);
    }

}