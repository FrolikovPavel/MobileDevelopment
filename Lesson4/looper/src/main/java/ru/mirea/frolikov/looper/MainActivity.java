package ru.mirea.frolikov.looper;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;


import ru.mirea.frolikov.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + msg.getData().getString("result"));
            }
        };
        MyLooper myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.editTextMirea.setText("Мой номер по списку №25");
        binding.textViewAge.setText("Мой возраст: ");
        binding.textViewJob.setText("Моя работа: ");
        final Runnable runn = new Runnable() {
            public void run() {
                Log.d(MainActivity.class.getSimpleName(), "Мой возраст: " + binding.editTextAge.getText());
                Log.d(MainActivity.class.getSimpleName(), "Моя работа: " + binding.editTextJob.getText());
            }
        };
        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("KEY", "mirea");
                msg.setData(bundle);
                myLooper.mHandler.sendMessage(msg);

                try {
                    int age = parseInt(String.valueOf(binding.editTextAge.getText()));
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            binding.editTextAge.postDelayed(runn, 1000*age);
                        }
                    });
                    t.start();
                } catch (Exception e) {
                    Log.d(MainActivity.class.getSimpleName(), e.toString());
                }
            }
        });
    }
}