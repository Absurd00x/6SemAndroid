package com.example.tasklast;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment
                                  implements TimePickerDialog.OnTimeSetListener {
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getContext()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ((MainActivity)getActivity()).onTimeSet(hourOfDay, minute);
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        // Caution: onSaveInstanceState(Bundle) is called only when
        // the fragment's host activity calls its own onSaveInstanceState(Bundle).
        // KEKW
        super.onSaveInstanceState(outState);
        /*
        Log.i("saving", "saving state");
        outState.putInt("hour", selectedHour);
        outState.putInt("minute", selectedMinute);
         */
    }
}
