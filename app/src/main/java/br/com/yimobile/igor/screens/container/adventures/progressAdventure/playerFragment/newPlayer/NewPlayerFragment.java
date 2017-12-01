package br.com.yimobile.igor.screens.container.adventures.progressAdventure.playerFragment.newPlayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;

public class NewPlayerFragment extends Fragment {

    private Adventure adventure;
    private EditText playerNameEditText;
    private ImageButton saveButton;
    private ImageButton dateButton;
    private Button close;
    private DatabaseReference mDatabase;

    private static final String TAG = NewPlayerFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_player, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        playerNameEditText = (EditText) view.findViewById(R.id.edit_player);
        saveButton = (ImageButton) view.findViewById(R.id.create_player);
        saveButton.setOnClickListener(saveOnClickListener);
        close = (Button) view.findViewById(R.id.sair);
        close.setOnClickListener(closeOnClickListener);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem editar = menu.findItem(R.id.action_editar);
        editar.setVisible(false);

        MenuItem ordenar = menu.findItem(R.id.action_ordenar);
        ordenar.setVisible(false);
    }

    /* On Click Listeners */
    View.OnClickListener closeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().onBackPressed();
        }
    };

    View.OnClickListener saveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "Save Clicked");

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String email = "";
                    for (DataSnapshot userSnapshop: dataSnapshot.getChildren()) {
                        HashMap<String, String> userMap = (HashMap) userSnapshop.getValue();
                        String userKey = userSnapshop.getKey().toString();
                        email = userMap.get("email").toString();

                        String playerName = playerNameEditText.getText().toString();

                        if (playerName.equalsIgnoreCase(email)) {
                            //add User
                            Log.d("NEWPLAYER", email + " adicionado a aventura " + adventure.getNome());
                            ((ContainerActivity) getActivity()).addPlayer(adventure, userKey);
                            return;
                        }
                    }

                    //Log.d("NEWPLAYER", email + " não encontrado");
                    Toast.makeText(getActivity().getApplicationContext(), email + "não encontrado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            mDatabase.child("users").addListenerForSingleValueEvent(postListener);

        }
    };

    public void setAdventure(Adventure adventure) {
        this.adventure = adventure;
    }

}