package ru.mirea.frolikov.lesson4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.frolikov.lesson4.databinding.ActivityMainBinding;
import ru.mirea.frolikov.lesson4.databinding.ActivityMusicPlayerBinding;

public class MusicPlayer extends AppCompatActivity {
    private ActivityMusicPlayerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.textViewTrackName.setText("Track name");
    }
}