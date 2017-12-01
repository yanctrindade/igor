package br.com.yimobile.igor.screens.container.adventures.progressAdventure.sessionsFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;
import database.Session;

public class SessionsFragment extends Fragment {

    private static final String TAG = SessionsFragment.class.getSimpleName();

    SessionRecyclerViewAdapter sessionsRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<Session> sessionsArrayList = new ArrayList<>();
    ArrayList<Session> sessionsArrayListBackup = new ArrayList<>();
    FloatingActionButton newSessionButton;
    Adventure adventure;
    boolean isOrdered = false;
    TextView nameAdventure;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);

        nameAdventure = view.findViewById(R.id.titulo);
        if(adventure != null) nameAdventure.setText(adventure.getNome());

        newSessionButton = view.findViewById(R.id.new_session_button);
        newSessionButton.setOnClickListener(newSessionOnClickListener);
        if(!((ContainerActivity) getActivity()).getUid().equals(adventure.getMestre())){
            newSessionButton.hide();
        }

        ImageButton players_button = view.findViewById(R.id.button_jogadores);
        players_button.setOnClickListener(playersOnClickListener);

        switch (adventure.getBackground()){
            case 0:
                view.setBackgroundResource(R.drawable.andamentobackground1_640);
                break;
            case 1:
                view.setBackgroundResource(R.drawable.andamentobackground2_640);
                break;
            case 2:
                view.setBackgroundResource(R.drawable.andamentobackground3_640);
                break;
            case 3:
                view.setBackgroundResource(R.drawable.andamentobackground_640);
                break;
            case 4:
                view.setBackgroundResource(R.drawable.andamentobackground5_640);
                break;
            default:
                view.setBackgroundResource(R.drawable.andamentobackground_640);
                break;
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.sessions_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        sessionsRecyclerViewAdapter = new SessionRecyclerViewAdapter(sessionsArrayList, adventure, getActivity());
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
            sessionsArrayListBackup.clear();
            sessionsArrayListBackup.addAll(sessionsArrayList);
        }
    }

    public void setAdventure(Adventure adventure){
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

    public void onOrderSessionsPressed(){
        if(!isOrdered) {
            Collections.sort(sessionsArrayList, new Comparator<Session>() {
                public int compare(Session s1, Session s2) {
                    Date d1 = null, d2 = null;
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    try {
                        d1 = formatter.parse(s1.getData());
                        d2 = formatter.parse(s2.getData());
                    } catch (java.text.ParseException ie) {
                        ie.printStackTrace();
                    }
                    return d1.compareTo(d2);
                }
            });
            isOrdered = true;
        }else{
            sessionsArrayList.clear();
            sessionsArrayList.addAll(sessionsArrayListBackup);
            isOrdered = false;
        }
        sessionsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem ordenar = menu.findItem(R.id.action_ordenar);
        if(isOrdered){
            ordenar.setTitle("Ordem primária");
        } else{
            ordenar.setTitle("Ordenar");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editar:
                if(((ContainerActivity) getActivity()).getUid().equals(adventure.getMestre())){
                    ((ContainerActivity) getActivity()).onEditAdventurePressed(adventure);
                }
                return true;
            case R.id.action_ordenar:
                onOrderSessionsPressed();
                if(isOrdered){
                    item.setTitle("Ordem primária");
                } else{
                    item.setTitle("Ordenar");
                }
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