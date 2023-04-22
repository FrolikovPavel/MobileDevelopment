package ru.mirea.frolikov.intentapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        String text = (String) getIntent().getSerializableExtra("text");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(text);
    }
}