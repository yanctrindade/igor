package br.com.yimobile.igor.screens.auth;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;

import com.facebook.login.Login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.yimobile.igor.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterFragment extends Fragment {
    private OnRegisterInteractionListener mCallback;
    private EditText emailText, senhaText, nomeText, dataText, sexoText;
    private DatePickerDialog data_Dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailText = view.findViewById(R.id.email);
        senhaText = view.findViewById(R.id.senha);
        nomeText = view.findViewById(R.id.nome);
        dataText = view.findViewById(R.id.data);
        sexoText = view.findViewById(R.id.sexo);

        Calendar data_atual = Calendar.getInstance();
        data_Dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataPicker = Calendar.getInstance();
                dataPicker.set(year, monthOfYear, dayOfMonth);
                dataText.setText(LoginActivity.dateToString(dataPicker, "dd/MM/yyyy"));
            }

        }, data_atual.get(Calendar.YEAR)-20, data_atual.get(Calendar.MONTH), data_atual.get(Calendar.DAY_OF_MONTH));
        data_Dialog.getDatePicker().setMaxDate(data_atual.getTimeInMillis());
        if(!dataText.getText().toString().isEmpty()) {
            Calendar aux = LoginActivity.stringToDate(dataText.getText().toString(), "dd/MM/yyyy");
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
                Animation wiggle = AnimationUtils.loadAnimation(getActivity(), R.anim.wiggle);
                String email = emailText.getText().toString();
                String senha = senhaText.getText().toString();
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
                else if(senha.isEmpty() || senha.equals("")) {
                    senhaText.startAnimation(wiggle);
                    senhaText.setError("Preencha sua senha");
                }
                else if(senha.length() < 6) {
                    senhaText.startAnimation(wiggle);
                    senhaText.setError("Senha deve ter, no mínimo, 6 dígitos");
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
                else if (mCallback != null) {
                    mCallback.onRegisterInteraction(email, senha, nome, data, sexo);
                }
            }
        });
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnRegisterInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    interface OnRegisterInteractionListener {

        public void onRegisterInteraction(String email, String senha, String nome, String data, String sexo);

    }

}
