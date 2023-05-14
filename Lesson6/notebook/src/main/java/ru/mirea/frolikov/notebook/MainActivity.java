package ru.mirea.frolikov.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.frolikov.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExternalStorageWritable()) {
                    writeFileToExternalStorage();
                } else {
                    Toast.makeText(getApplicationContext(), "Внешнее хранилище недоступно для записи", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExternalStorageReadable()) {
                    readFileFromExternalStorage();
                } else {
                    Toast.makeText(getApplicationContext(), "Внешнее хранилище недоступно для чтения", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    public void writeFileToExternalStorage() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, binding.editTextFileName.getText().toString());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(binding.editTextQuote.getText().toString());
            output.close();
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing" + file, e);
        }
    }
    public void readFileFromExternalStorage() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, binding.editTextFileName.getText().toString());
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            Log.w("ExternalStorage", String.format("Read from file %s successful", lines.toString()));
            binding.editTextQuote.setText(lines.toString().substring(1, lines.toString().length() - 1));
        } catch (Exception e) {
            Log.w("ExternalStorage", String.format("Read from file %s failed", e.getMessage()));
        }
    }
}