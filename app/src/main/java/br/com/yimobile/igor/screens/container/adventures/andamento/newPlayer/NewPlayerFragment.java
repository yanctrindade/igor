package br.com.yimobile.igor.screens.container.adventures.andamento.newPlayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import br.com.yimobile.igor.R;

public class NewPlayerFragment extends Fragment {

    private EditText playerNameEditText;
    private ImageButton saveButton;
    private ImageButton dateButton;
    private Button close;

    private static final String TAG = NewPlayerFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_player, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerNameEditText = (EditText) view.findViewById(R.id.edit_player);
        saveButton = (ImageButton) view.findViewById(R.id.create_player);
        saveButton.setOnClickListener(saveOnClickListener);
        close = (Button) view.findViewById(R.id.sair);
        close.setOnClickListener(closeOnClickListener);

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
        }
    };

}
