package ru.mirea.frolikov.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String h = String.valueOf(hourOfDay);
        if (hourOfDay < 10) {
            h = "0"+hourOfDay;
        }
        String m = String.valueOf(minute);
        if (minute < 10) {
            m = "0"+minute;
        }
        Toast.makeText(getActivity(), "Выбранное время - " + h + ":" + m,
                Toast.LENGTH_LONG).show();
    }
}
