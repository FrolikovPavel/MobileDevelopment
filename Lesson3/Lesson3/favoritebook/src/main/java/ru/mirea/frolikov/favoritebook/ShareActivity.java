package ru.mirea.frolikov.favoritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        TextView developerBook = findViewById(R.id.textViewDevBook);
        developerBook.setText("Любимая книга разработчика – Гарри Поттер и философский камень");
    }
    public void sendUserBookToMainActivity(View view) {
        EditText userBook = findViewById(R.id.editTextUserBook);
        String text = userBook.getText().toString();
        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, text);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}