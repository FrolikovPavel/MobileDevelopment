package ru.mirea.frolikov.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.frolikov.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("mirea_settings", MODE_PRIVATE);
        String group = preferences.getString("GROUP", "unknown");
        int number = preferences.getInt("NUMBER", 0);
        String favFilm = preferences.getString("FAVORITE_FILM", "unknown");

        binding.editTextGroup.setText(group);
        binding.editTextNumber.setText(String.valueOf(number));
        binding.editTextFavFilm.setText(favFilm);

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("mirea_settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("GROUP", binding.editTextGroup.getText().toString());
                try {
                    editor.putInt("NUMBER", Integer.parseInt(binding.editTextNumber.getText().toString()));
                } catch (Exception e) {
                    Log.d(MainActivity.class.getSimpleName(), e.toString());
                }
                editor.putString("FAVORITE_FILM", binding.editTextFavFilm.getText().toString());
                editor.apply();
            }
        });
    }
}