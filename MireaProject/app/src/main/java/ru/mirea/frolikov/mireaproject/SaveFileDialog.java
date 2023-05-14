package ru.mirea.frolikov.mireaproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import javax.security.auth.callback.Callback;

public class SaveFileDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText fileName = new EditText(this.getActivity());
        fileName.setHint("Название файла");
        builder.setTitle("Сохранить файл")
                .setView(fileName)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WorkWithFilesFragment parentFrag = ((WorkWithFilesFragment)SaveFileDialog.this.getParentFragment());
                        if (parentFrag != null) {
                            parentFrag.onSaveClicked(fileName.getText().toString());
                        } else {
                            Log.d(MainActivity.class.getSimpleName(), "Parent fragment is null");
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
