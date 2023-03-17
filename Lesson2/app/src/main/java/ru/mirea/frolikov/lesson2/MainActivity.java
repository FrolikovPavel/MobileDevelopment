package ru.mirea.frolikov.lesson2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int x = 5;
        int y = 6;
        int sum = x+y;
        Log.d(TAG, "SUMM = " + sum);
        editText = findViewById(R.id.editTextTextPersonName);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(TAG, "Invoke onPostCreate method");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Invoke onResume method");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "Invoke onPostResume method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Invoke onStart method");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "Invoke onRestart method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Invoke onStop method");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Invoke onDestroy method");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Invoke onPause method");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Invoke onSaveInstanceState method");
        outState.putString("data_value", editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String text = savedInstanceState.getString("data_value");
        Log.d(TAG, "Saved text: " + text);
        editText.setText(text);
    }
}