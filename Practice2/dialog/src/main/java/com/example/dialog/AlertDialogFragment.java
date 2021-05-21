package com.example.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class AlertDialogFragment extends DialogFragment {
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Здравствуй МИРЭА!")
                .setMessage("Успех близок?")
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton("Иду дальше", (dialog, which) -> {
                    ((MainActivity)getActivity()).onOkClicked();
                    dialog.cancel();
                })
                .setNeutralButton("На паузе", (dialog, which) -> {
                    ((MainActivity)getActivity()).onNeutralClicked();
                    dialog.cancel();
                })
                .setNegativeButton("Нет", (dialog, which) -> {
                    ((MainActivity)getActivity()).onCancelClicked();
                    dialog.cancel();
                });

        return builder.create();
    }
}
