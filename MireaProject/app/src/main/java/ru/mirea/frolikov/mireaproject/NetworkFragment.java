package ru.mirea.frolikov.mireaproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.frolikov.mireaproject.databinding.FragmentNetworkBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkFragment extends Fragment {

    private FragmentNetworkBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NetworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NetworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetworkFragment newInstance(String param1, String param2) {
        NetworkFragment fragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadPageTask().execute("https://ipinfo.io/json");
                } else {
                    Toast.makeText(getActivity(), "Нет интернета", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.textView.setText("Загружаем...");
            binding.textViewWeather.setText(R.string.weather);
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
                String loc = responseJson.getString("loc");
                String latitude = loc.split(",")[0];
                String longitude = loc.split(",")[1];
                new DownloadWeatherTask().execute("https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&current_weather=true&windspeed_unit=ms");
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
                    int weatherCode = currentWeatherJson.getInt("weathercode");
                    String weather = getString(R.string.weather) + " " + weatherDescription(weatherCode);
                    binding.textViewWeather.setText(weather);
                    String temperature = getString(R.string.temperature) + " " + currentWeatherJson.getString("temperature") + " ℃";
                    binding.textViewTemperature.setText(temperature);
                    String windSpeed = getString(R.string.wind_speed) + " " + currentWeatherJson.getString("windspeed") + " м/с";
                    binding.textViewWindSpeed.setText(windSpeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(result);
            }
            private String weatherDescription(int weatherCode) {
                String weather = "";
                switch (weatherCode) {
                    case 0:
                        weather = "Ясно";
                        break;
                    case 1:
                        weather = "Малооблачно";
                        break;
                    case 2:
                        weather = "Облачно с прояснениями";
                        break;
                    case 3:
                        weather = "Пасмурно";
                        break;
                    case 45:
                    case 48:
                        weather = "Туман";
                        break;
                    case 51:
                    case 53:
                    case 55:
                        weather = "Небольшой дождь";
                        break;
                    case 56:
                    case 57:
                    case 66:
                    case 67:
                        weather = "Ледяной дождь";
                        break;
                    case 61:
                    case 63:
                    case 65:
                        weather = "Дождь";
                        break;
                    case 71:
                        weather = "Небольшой снег";
                        break;
                    case 73:
                    case 75:
                    case 77:
                        weather = "Снег";
                        break;
                    case 80:
                    case 81:
                        weather = "Сильный дождь";
                        break;
                    case 82:
                        weather = "Ливень";
                        break;
                    case 85:
                    case 86:
                        weather = "Сильный снег";
                        break;
                    case 95:
                        weather = "Гроза";
                        break;
                    case 96:
                    case 99:
                        weather = "Гроза с градом";
                        break;
                }
                return weather;
            }
        }
    }
}