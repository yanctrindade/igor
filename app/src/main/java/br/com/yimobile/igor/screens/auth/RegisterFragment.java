package br.com.yimobile.igor.screens.auth;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.facebook.login.Login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.yimobile.igor.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterFragment extends Fragment {
    private OnRegisterInteractionListener mCallback;
    private EditText emailText, senhaText, nomeText, anoData;
    private Spinner diaData, mesData;
    private RadioButton sexoMasculino, sexoFeminino, sexoOutro;
    private DatePickerDialog data_Dialog;
    ArrayAdapter<String> adapterDias31, adapterDias30, adapterDias29, adapterDias28;

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

        sexoMasculino = view.findViewById(R.id.sexoMasculinoConfig);
        sexoFeminino = view.findViewById(R.id.sexoFemininoConfig);
        sexoOutro = view.findViewById(R.id.sexoOutroConfig);

        sexoMasculino.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sexoFeminino.setChecked(false);
                sexoOutro.setChecked(false);
                sexoOutro.setError(null);
            }
        });

        sexoFeminino.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sexoMasculino.setChecked(false);
                sexoOutro.setChecked(false);
                sexoOutro.setError(null);
            }
        });

        sexoOutro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sexoMasculino.setChecked(false);
                sexoFeminino.setChecked(false);
                sexoOutro.setError(null);
            }
        });

        diaData = view.findViewById(R.id.diaSpinnerConfig);
        mesData = view.findViewById(R.id.mesSpinnerConfig);
        anoData = view.findViewById(R.id.anoTextConfig);

        String[] dias31 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        String[] dias30 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        String[] dias29 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29"};
        String[] dias28 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28"};
        adapterDias31 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, dias31);
        adapterDias31.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDias30 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, dias30);
        adapterDias30.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDias29 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, dias29);
        adapterDias29.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDias28 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, dias28);
        adapterDias28.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        diaData.setAdapter(adapterDias31);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.meses, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mesData.setAdapter(adapter);
        mesData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setAdapterDia(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        anoData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(diaData != null && mesData != null && mesData.getSelectedItemPosition() == 1 &&
                        anoData != null && anoData.getText() != null && anoData.getText().toString().length() == 4){
                    setFevereiro();
                }
            }
        });

        Calendar data_atual = Calendar.getInstance();
        data_Dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataPicker = Calendar.getInstance();
                dataPicker.set(year, monthOfYear, dayOfMonth);
                anoData.setText(String.valueOf(year));
                mesData.setSelection(monthOfYear);
                diaData.setSelection(dayOfMonth-1);
            }

        }, data_atual.get(Calendar.YEAR) - 20, data_atual.get(Calendar.MONTH), data_atual.get(Calendar.DAY_OF_MONTH));
        data_Dialog.getDatePicker().setMaxDate(data_atual.getTimeInMillis());
        String data = getData();
        if(!data.equals("")) {
            Calendar aux = LoginActivity.stringToDate(data, "dd/MM/yyyy");
            data_Dialog.updateDate(aux.get(Calendar.YEAR), aux.get(Calendar.MONTH), aux.get(Calendar.DAY_OF_MONTH));
        }
        view.findViewById(R.id.dataSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anoData.setError(null);
                data_Dialog.show();
            }
        });

        view.findViewById(R.id.cadastrobutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                Animation wiggle = AnimationUtils.loadAnimation(getActivity(), R.anim.wiggle);
                String email = emailText.getText().toString();
                String senha = senhaText.getText().toString();
                String nome = nomeText.getText().toString();
                String data = getData();
                String sexo = "";
                if(sexoMasculino.isChecked()){
                    sexo = "MASCULINO";
                } else if(sexoFeminino.isChecked()){
                    sexo = "FEMININO";
                } else if(sexoOutro.isChecked()){
                    sexo = "OUTRO";
                }

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
                    anoData.startAnimation(wiggle);
                    anoData.setError("Preencha sua data de nascimento");
                }
                else if(sexo.isEmpty() || sexo.equals("")){
                    sexoMasculino.startAnimation(wiggle);
                    sexoFeminino.startAnimation(wiggle);
                    sexoOutro.startAnimation(wiggle);
                    sexoOutro.setError("Especifique seu sexo");
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

    private String getData(){
        String dia;
        int diaN = diaData.getSelectedItemPosition()+1;
        if(diaN < 10){
            dia = "0" + diaN;
        } else{
            dia = "" + diaN;
        }
        String mes;
        int mesN = mesData.getSelectedItemPosition()+1;
        if(mesN < 10){
            mes = "0" + mesN;
        } else{
            mes = "" + mesN;
        }
        if(anoData.getText().toString().length() == 4) {
            return (dia + "/" + mes + "/" + anoData.getText().toString());
        }
        return "";
    }

    private void setFevereiro(){
        int pos = diaData.getSelectedItemPosition();
        int ano = Integer.parseInt(anoData.getText().toString());
        if (ano % 4 == 0 && (ano % 400 == 0 || ano % 100 != 0)) {
            diaData.setAdapter(adapterDias29);
            if(pos > 28) diaData.setSelection(28);
            else diaData.setSelection(pos);
        } else{
            diaData.setAdapter(adapterDias28);
            if(pos > 27) diaData.setSelection(27);
            else diaData.setSelection(pos);
        }
    }

    private void setAdapterDia(int mes){
        int pos = diaData.getSelectedItemPosition();
        if(mes == 1){
            if(anoData != null && anoData.getText() != null && anoData.getText().toString().length() == 4){
                setFevereiro();
            } else {
                diaData.setAdapter(adapterDias29);
                if(pos > 28) diaData.setSelection(28);
                else diaData.setSelection(pos);
            }
        } else if(mes == 3 || mes == 5 || mes == 8 || mes == 10){
            diaData.setAdapter(adapterDias30);
            if(pos > 29) diaData.setSelection(29);
            else diaData.setSelection(pos);
        } else{
            diaData.setAdapter(adapterDias31);
            if(pos > 30) diaData.setSelection(30);
            else diaData.setSelection(pos);
        }
    }

    interface OnRegisterInteractionListener {

        public void onRegisterInteraction(String email, String senha, String nome, String data, String sexo);

    }

}
