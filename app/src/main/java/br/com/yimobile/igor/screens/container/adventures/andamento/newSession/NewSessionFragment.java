package br.com.yimobile.igor.screens.container.adventures.andamento.newSession;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.yimobile.igor.R;

public class NewSessionFragment extends Fragment {

    private EditText sessionNameEditText;
    private ImageButton saveButton;
    private EditText dateText;
    private Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private Button close;

    private static final String TAG = NewSessionFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_session, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionNameEditText = (EditText) view.findViewById(R.id.edit_avent);
        saveButton = (ImageButton) view.findViewById(R.id.create_session);
        saveButton.setOnClickListener(saveOnClickListener);
        dateText = (EditText) view.findViewById(R.id.create_session_date);
        dateText.setOnClickListener(dateOnClickListener);
        Calendar aux = Calendar.getInstance();
        dateText.setText(dateToString(aux, "dd/MM/yyyy"));
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

    View.OnClickListener dateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "Date Clicked");
            setDateTimeField();
        }
    };

    private void setDateTimeField() {
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataPicker = Calendar.getInstance();
                dataPicker.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateToString(dataPicker, "dd/MM/yyyy"));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar atual = Calendar.getInstance();
        Calendar esc = stringToDate(dateText.getText().toString(), "dd/MM/yyyy");
        datePickerDialog.updateDate(esc.get(Calendar.YEAR), esc.get(Calendar.MONTH), esc.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(atual.getTimeInMillis());
        datePickerDialog.show();
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
