package ru.mirea.frolikov.intentfilter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private String TAG = "Intent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view){
        Uri address = Uri.parse("https://www.mirea.ru/");
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openLinkIntent);
        //if (openLinkIntent.resolveActivity(getPackageManager()) != null) {
        //    startActivity(openLinkIntent);
        //} else {
        //    Log.d("Intent", "Не получается обработать намерение!");
        //}
    }
    public void onClick2(View view){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MIREA");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Фроликов Павел Сергеевич");
        startActivity(Intent.createChooser(shareIntent, "МОИ ФИО"));
    }
}