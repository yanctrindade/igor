package br.com.yimobile.igor.screens.container.adventures.progressAdventure.playerFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import br.com.yimobile.igor.screens.container.adventures.progressAdventure.playerFragment.PlayerRecyclerViewAdapter;
import database.Adventure;
import database.User;


public class PlayersFragment extends Fragment {

    PlayerRecyclerViewAdapter playerRecyclerViewAdapter;
    RecyclerView recyclerView;
    List<String> playerArrayList = new ArrayList<>();
    FloatingActionButton newPlayerButton;
    Adventure adventure;
    TextView nameAdventure, mestre;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players, container, false);

        nameAdventure = view.findViewById(R.id.titulo);

        mestre = view.findViewById(R.id.mestre_nickname);

        newPlayerButton = view.findViewById(R.id.new_player_button);
        newPlayerButton.setOnClickListener(newPlayerOnClickListener);
        if(!((ContainerActivity) getActivity()).getUid().equals(adventure.getMestre())){
            newPlayerButton.hide();
        }

        ImageButton andamento_button = view.findViewById(R.id.button_andamento);
        andamento_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ContainerActivity) getActivity()).onResumePressed(adventure);
            }
        });

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

        if(adventure != null) {
            nameAdventure.setText(adventure.getNome());
            if(mestre != null) {
                String keyO = ((ContainerActivity) getActivity()).getUid();
                HashMap<String, User> userList = ((ContainerActivity) getActivity()).getUserList();
                for (Map.Entry<String, User> entry : userList.entrySet()) {
                    String key = entry.getKey();
                    User value = entry.getValue();
                    if(key.equals(keyO)){
                        mestre.setText(value.getUsername());
                        break;
                    }
                }
            }

            playerArrayList = adventure.getJogadores();
        }

        recyclerView = view.findViewById(R.id.sessions_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        playerRecyclerViewAdapter = new PlayerRecyclerViewAdapter(playerArrayList, getActivity());
        recyclerView.setAdapter(playerRecyclerViewAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem editar = menu.findItem(R.id.action_editar);
        editar.setVisible(false);

        //MenuItem ordenar = menu.findItem(R.id.action_ordenar);
        //ordenar.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ordenar:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setAdventure(Adventure adventure){
        this.adventure = adventure;
        if(nameAdventure != null) nameAdventure.setText(adventure.getNome());
        if(mestre != null) {
            String keyO = ((ContainerActivity) getActivity()).getUid();
            HashMap<String, User> userList = ((ContainerActivity) getActivity()).getUserList();
            for (Map.Entry<String, User> entry : userList.entrySet()) {
                String key = entry.getKey();
                User value = entry.getValue();
                if(key.equals(keyO)){
                    mestre.setText(value.getUsername());
                    break;
                }
            }
        }
        if(playerRecyclerViewAdapter != null && playerArrayList != null){
            playerArrayList = adventure.getJogadores();
            playerRecyclerViewAdapter.swap();
        }
    }


    View.OnClickListener newPlayerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((ContainerActivity) getActivity()).newPlayerPressed(adventure);
        }
    };

    public interface PlayersOnClickListener {
        public void onResumePressed(Adventure adventure);
    }

}
