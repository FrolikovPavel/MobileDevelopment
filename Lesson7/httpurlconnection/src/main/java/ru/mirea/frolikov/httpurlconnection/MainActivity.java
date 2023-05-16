package ru.mirea.frolikov.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.frolikov.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadPageTask().execute("https://ipinfo.io/json");
                } else {
                    Toast.makeText(MainActivity.this, "Нет интернета", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.textView.setText("Загружаем...");
            binding.textViewIp.setText(R.string.ip);
            binding.textViewCity.setText(R.string.city);
            binding.textViewRegion.setText(R.string.region);
            binding.textViewCountry.setText(R.string.country);
            binding.textViewLoc.setText(R.string.loc);
            binding.textViewOrg.setText(R.string.org);
            binding.textViewPostal.setText(R.string.postal);
            binding.textViewTimezone.setText(R.string.timezone);
            binding.textViewTemperature.setText(R.string.temperature);
            binding.textViewWindSpeed.setText(R.string.wind_speed);
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            binding.textView.setText("");
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);
                String ip = getString(R.string.ip) + " " + responseJson.getString("ip");
                binding.textViewIp.setText(ip);
                String city = getString(R.string.city) + " " + responseJson.getString("city");
                binding.textViewCity.setText(city);
                String region = getString(R.string.region) + " " + responseJson.getString("region");
                binding.textViewRegion.setText(region);
                String country = getString(R.string.country) + " " + responseJson.getString("country");
                binding.textViewCountry.setText(country);
                String loc = responseJson.getString("loc");
                String latitude = loc.split(",")[0];
                String longitude = loc.split(",")[1];
                loc = getString(R.string.loc) + " " + latitude + ", " + longitude;
                binding.textViewLoc.setText(loc);
                String org = getString(R.string.org) + " " + responseJson.getString("org");
                binding.textViewOrg.setText(org);
                String postal = getString(R.string.postal) + " " + responseJson.getString("postal");
                binding.textViewPostal.setText(postal);
                String timezone = getString(R.string.timezone) + " " + responseJson.getString("timezone");
                binding.textViewTimezone.setText(timezone);
                new DownloadWeatherTask().execute("https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&current_weather=true");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }

        private String downloadIpInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read = 0;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read); }
                    bos.close();
                    data = bos.toString();
                } else {
                    data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                }
                connection.disconnect();
                } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
        private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.textView.setText("Загружаем...");
            }

            @Override
            protected String doInBackground(String... urls) {
                try {
                    return downloadIpInfo(urls[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "error";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                binding.textView.setText("");
                Log.d(MainActivity.class.getSimpleName(), result);
                try {
                    JSONObject responseJson = new JSONObject(result);
                    Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);
                    JSONObject currentWeatherJson = responseJson.getJSONObject("current_weather");
                    String temperature = getString(R.string.temperature) + " " + currentWeatherJson.getString("temperature") + " ℃";
                    binding.textViewTemperature.setText(temperature);
                    String windSpeed = getString(R.string.wind_speed) + " " + currentWeatherJson.getString("windspeed") + " км/ч";
                    binding.textViewWindSpeed.setText(windSpeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(result);
            }
        }
    }
}