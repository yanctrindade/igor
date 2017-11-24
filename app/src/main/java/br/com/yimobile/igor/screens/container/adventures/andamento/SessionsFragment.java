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
import java.util.List;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import br.com.yimobile.igor.screens.container.adventures.andamento.ItensList.SessionRecyclerViewAdapter;
import database.Adventure;
import database.Session;

public class SessionsFragment extends Fragment {

    private static final String TAG = SessionsFragment.class.getSimpleName();

    SessionRecyclerViewAdapter sessionsRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<Session> sessionsArrayList = new ArrayList<>();
    FloatingActionButton newSessionButton;
    Adventure adventure;
    TextView nameAdventure;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume, container, false);

        nameAdventure = view.findViewById(R.id.titulo);
        if(adventure != null) nameAdventure.setText(adventure.getNome());

        newSessionButton = view.findViewById(R.id.new_session_button);
        newSessionButton.setOnClickListener(newSessionOnClickListener);
        if(!((ContainerActivity) getActivity()).getUid().equals(adventure.getMestre())){
            newSessionButton.hide();
        }

        ImageButton players_button = view.findViewById(R.id.button_jogadores);
        players_button.setOnClickListener(playersOnClickListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.sessions_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        sessionsRecyclerViewAdapter = new SessionRecyclerViewAdapter(sessionsArrayList, getActivity());
        recyclerView.setAdapter(sessionsRecyclerViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        sessionsArrayList.clear();
        List<Session> sessoes = adventure.getSessoes();
        if(sessoes != null && !sessoes.isEmpty()) {
            for (int i = sessoes.size() - 1; i >= 0; i--) {
                addNewSession(sessoes.get(i));
            }
        }
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
                if(!((ContainerActivity) getActivity()).getUid().equals(adventure.getMestre())){
                    ((ContainerActivity) getActivity()).onEditAdventurePressed(adventure);
                }
                return true;
            case R.id.action_ordenar:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Public Interface for Listener */
    public interface ResumeOnClickListener {
        public void onPlayersPressed(Adventure adventure);
        public void onEditAdventurePressed(Adventure adventure);
        public void newSessionPressed(Adventure adventure);
    }

    public void addNewSession(Session session){
        sessionsArrayList.add(session);
        Log.d("SESLIST", sessionsArrayList.size() + " TAMANHO");
        sessionsRecyclerViewAdapter.swap();
    }

}