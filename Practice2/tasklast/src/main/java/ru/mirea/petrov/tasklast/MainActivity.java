package ru.mirea.petrov.tasklast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTimeSet(int hour, int minute) {
        String display = String.format("Вы выбрали %d часов\nи %d минут", hour, minute);
        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_LONG).show();
    }

    public void onDateSet(int year, int month, int day) {
        String display = String.format("Вы выбрали %d год\n%d месяц\n%d день",
                year, month + 1, day);
        Toast.makeText(getApplicationContext(), display, Toast.LENGTH_LONG).show();
    }

    public void showTimePickerDialog(View v) {
        MyTimeDialogFragment fragment = new MyTimeDialogFragment();
        fragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        MyDateDialogFragment fragment = new MyDateDialogFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showProgressPickerDialog(View v) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Progress POGU");
        dialog.setMessage("Message 4Head");
        dialog.show();
    }
}