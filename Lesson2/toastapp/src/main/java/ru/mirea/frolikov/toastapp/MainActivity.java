package ru.mirea.frolikov.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextTextPersonName);
    }

    public void onClick(View view){
        String text = editText.getText().toString();
        String str = "СТУДЕНТ № 25 ГРУППА БСБО-01-20 Количество символов - " + text.length();
        Toast toast = Toast.makeText(getApplicationContext(),
                str,
                Toast.LENGTH_SHORT);
        toast.show();
    }
}