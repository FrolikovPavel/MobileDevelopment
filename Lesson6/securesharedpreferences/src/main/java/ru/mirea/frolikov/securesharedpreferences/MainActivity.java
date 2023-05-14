package ru.mirea.frolikov.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.frolikov.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        try {
            String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            secureSharedPreferences.edit().putString("secure", "Пушкин А.С.").apply();
            secureSharedPreferences.edit().putInt("secure_image", R.drawable.pushkin).apply();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            binding.textView.setText(secureSharedPreferences.getString("secure", "Любимый поэт"));
            binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), secureSharedPreferences.getInt("secure_image", R.drawable.avatar_1), null));
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}