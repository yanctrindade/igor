package br.com.yimobile.igor.screens.container.account;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.User;

import static br.com.yimobile.igor.screens.auth.LoginActivity.dateToString;
import static br.com.yimobile.igor.screens.auth.LoginActivity.stringToDate;

public class AccountFragment extends Fragment {
    private DatabaseReference mDatabase;
    private EditText emailText, nomeText, dataText, sexoText;
    private DatePickerDialog data_Dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailText = view.findViewById(R.id.email);
        nomeText = view.findViewById(R.id.nome);
        dataText = view.findViewById(R.id.data);
        sexoText = view.findViewById(R.id.sexo);

        User user = ((ContainerActivity) getActivity()).getUser();
        if (user != null) {
            fillFragment(user);
        }

        Calendar data_atual = Calendar.getInstance();
        data_Dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataPicker = Calendar.getInstance();
                dataPicker.set(year, monthOfYear, dayOfMonth);
                dataText.setText(dateToString(dataPicker, "dd/MM/yyyy"));
            }

        }, data_atual.get(Calendar.YEAR)-20, data_atual.get(Calendar.MONTH), data_atual.get(Calendar.DAY_OF_MONTH));
        data_Dialog.getDatePicker().setMaxDate(data_atual.getTimeInMillis());
        if(!dataText.getText().toString().isEmpty()) {
            Calendar aux = stringToDate(dataText.getText().toString(), "dd/MM/yyyy");
            data_Dialog.updateDate(aux.get(Calendar.YEAR), aux.get(Calendar.MONTH), aux.get(Calendar.DAY_OF_MONTH));
        }
        dataText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data_Dialog.show();
            }
        });

        view.findViewById(R.id.cadastrobutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                Animation wiggle = AnimationUtils.loadAnimation(getActivity(), R.anim.wiggle);
                String email = emailText.getText().toString();
                String nome = nomeText.getText().toString();
                String data = dataText.getText().toString();
                String sexo = sexoText.getText().toString();

                if(email.isEmpty() || email.equals("")) {
                    emailText.startAnimation(wiggle);
                    emailText.setError("Preencha seu email");
                }
                else if(!email.contains("@") || !email.contains(".")) {
                    emailText.startAnimation(wiggle);
                    emailText.setError("Email inválido");
                }
                else if(nome.isEmpty() || nome.equals("")){
                    nomeText.startAnimation(wiggle);
                    nomeText.setError("Preencha seu nome de usuário");
                }
                else if(data.isEmpty() || data.equals("")){
                    dataText.startAnimation(wiggle);
                    dataText.setError("Preencha sua data de nascimento");
                }
                else if(sexo.isEmpty() || sexo.equals("")){
                    sexoText.startAnimation(wiggle);
                    sexoText.setError("Preencha seu sexo");
                }
                else {
                    ((ContainerActivity) getActivity()).setUser(new User(email, nome, data, sexo));
                    mDatabase.child("users").child(
                            ((ContainerActivity) getActivity()).getUid()).setValue(
                            ((ContainerActivity) getActivity()).getUser());
                }
            }
        });
    }

    public void fillFragment(User user){
        emailText.setText(user.getEmail());
        nomeText.setText(user.getUsername());
        dataText.setText(user.getNascimento());
        sexoText.setText(user.getSexo());
    }

}
