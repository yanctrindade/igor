package br.com.yimobile.igor.screens.container.adventures.andamento;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import br.com.yimobile.igor.screens.container.adventures.AdventuresRecyclerViewAdapter;

public class ResumeFragment extends Fragment {
    SessionsRecyclerViewAdapter sessionsRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<String> sessionsArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resume, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        ImageButton players_button = view.findViewById(R.id.button_jogadores);
        players_button.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ContainerActivity) getActivity()).onPlayersPressed();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.sessions_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        sessionsArrayList.add("17/05 Sessão 5");
        sessionsArrayList.add("16/05 Sessão 4");
        sessionsArrayList.add("15/05 Sessão 3");
        sessionsArrayList.add("13/05 Sessão 2");
        sessionsArrayList.add("10/05 Sessão 1");
        sessionsRecyclerViewAdapter = new SessionsRecyclerViewAdapter(sessionsArrayList, getActivity());
        recyclerView.setAdapter(sessionsRecyclerViewAdapter);
    }

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

    public interface ResumeOnClickListener {
        public void onPlayersPressed();
        public void onEditAdventurePressed();
    }

}