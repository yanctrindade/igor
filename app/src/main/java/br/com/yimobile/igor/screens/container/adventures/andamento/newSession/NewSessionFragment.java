package br.com.yimobile.igor.screens.container.adventures.andamento.newSession;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;

public class NewSessionFragment extends Fragment {

    private EditText sessionNameEditText;
    private ImageButton saveButton;
    private EditText dateText;
    private Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private Button close;
    Adventure adventure;

    private static final String TAG = NewSessionFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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

    public void SetAdventure(Adventure adventure){
        this.adventure = adventure;
    }

    /* On Click Listeners */
    View.OnClickListener closeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
            getActivity().onBackPressed();
        }
    };

    View.OnClickListener saveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "Save Clicked");
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
            ((ContainerActivity) getActivity()).onSessionCreated(
                    sessionNameEditText.getText().toString(), dateText.getText().toString(), adventure);
            getActivity().onBackPressed();
        }
    };

    View.OnClickListener dateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "Date Clicked");
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
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

    public interface NewSessionOnClickListener {
        public void onSessionCreated(String name, String data, Adventure adventure);
    };

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
