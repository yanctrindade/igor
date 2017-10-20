package br.com.yimobile.igor.screens.container.account;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.yimobile.igor.R;
import database.User;

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

        Query dataUser = mDatabase.child("users").orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    user = singleSnapshot.getValue(User.class);
                }
                if (user != null) {
                    emailText.setText(user.getEmail());
                    nomeText.setText(user.getUsername());
                    dataText.setText(user.getNascimento());
                    sexoText.setText(user.getSexo());

                    if(!dataText.getText().toString().isEmpty()) {
                        Calendar aux = stringToDate(dataText.getText().toString(), "dd/MM/yyyy");
                        data_Dialog.updateDate(aux.get(Calendar.YEAR), aux.get(Calendar.MONTH), aux.get(Calendar.DAY_OF_MONTH));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });

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

                String email = emailText.getText().toString();
                String nome = nomeText.getText().toString();
                String data = dataText.getText().toString();
                String sexo = sexoText.getText().toString();

                User user = new User(email, nome, data, sexo);
                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
            }
        });
    }

    public static Calendar stringToDate(String d, String f){
        if(d == null || f == null || f.isEmpty()) return null;
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(f, Locale.ENGLISH);
            cal.setTime(formatter.parse(d));
        } catch (ParseException e) {
            cal = null;
        }
        return cal;
    }

    public static String dateToString(Calendar d, String f){
        if(d == null || f == null || f.isEmpty()) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(f, Locale.ENGLISH);
        return formatter.format(d.getTime());
    }
}
