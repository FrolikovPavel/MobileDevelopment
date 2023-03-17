package ru.mirea.frolikov.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;


import java.util.Calendar;

public class MyDateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month += 1;
        String m = String.valueOf(month);
        if (month < 10) {
            m = "0"+month;
        }
        String d = String.valueOf(dayOfMonth);
        if (dayOfMonth < 10) {
            d = "0"+dayOfMonth;
        }
        Toast.makeText(getActivity(), "Выбранная дата - " + d + "." + m + "." + year,
                Toast.LENGTH_LONG).show();
    }
}
