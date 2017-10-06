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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.yimobile.igor.R;

public class NewSessionFragment extends Fragment {

    private EditText sessionNameEditText;
    private ImageButton saveButton;
    private ImageButton dateButton;
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
        dateButton = (ImageButton) view.findViewById(R.id.create_session_date);
        dateButton.setOnClickListener(dateOnClickListener);
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

        }
    };

    View.OnClickListener dateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setDateTimeField();
        }
    };

    private void setDateTimeField() {
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);

                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                String date = dateFormatter.format(dateSelected.getTime());
                Log.d(TAG, "Data Selecionada :" + date);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
}
