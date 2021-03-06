package br.com.yimobile.igor.screens.container.adventures.progressAdventure.editAdventure;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;

public class EditAdventureNameFragment extends Fragment {
    private EditText adventureName, adventureDescr;
    Adventure adventure;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        return inflater.inflate(R.layout.fragment_editadventure, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button exit_button = view.findViewById(R.id.sair);
        exit_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
                getActivity().onBackPressed();
            }
        });

        adventureName = view.findViewById(R.id.edit_avent);
        if(adventure != null) adventureName.setText(adventure.getNome());

        adventureDescr = view.findViewById(R.id.descricao);
        if(adventure != null && adventure.getDescricao() != null &&
                !adventure.getDescricao().isEmpty() && !adventure.getDescricao().equals("")){
            adventureDescr.setText(adventure.getDescricao());
        }

        ImageButton create_button = view.findViewById(R.id.create_adv);
        create_button.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation wiggle = AnimationUtils.loadAnimation(getActivity(), R.anim.wiggle);
                if(adventureName.getText() == null ||
                        adventureName.getText().toString().isEmpty() ||
                        adventureName.getText().toString().equals("")){
                    adventureName.startAnimation(wiggle);
                    adventureName.setError("Preencha o nome da aventura");
                } else {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                    String name = adventureName.getText().toString();
                    String descr = "";
                    if(adventureDescr.getText() != null ||
                            !adventureDescr.getText().toString().isEmpty() ||
                            !adventureDescr.getText().toString().equals("")){
                        descr = adventureDescr.getText().toString();
                    }
                    ((ContainerActivity) getActivity()).onAdventureEdited(adventure, name, descr);
                    getActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem editar = menu.findItem(R.id.action_editar);
        editar.setVisible(false);

        MenuItem ordenar = menu.findItem(R.id.action_ordenar);
        ordenar.setVisible(false);
    }

    public void SetAdventure(Adventure adventure){
        this.adventure = adventure;
        if(adventureName != null) adventureName.setText(adventure.getNome());
        if(adventureDescr != null) adventureDescr.setText(adventure.getDescricao());
    }

    public interface EditAdventureOnClickListener {
        public void onAdventureEdited(Adventure adventure, String name, String descr);
    }
}
