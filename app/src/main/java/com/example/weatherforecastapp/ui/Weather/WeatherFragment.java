package com.example.weatherforecastapp.ui.Weather;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherforecastapp.DataHandle;
import com.example.weatherforecastapp.FetchDataTask;
import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.RequestType;
import com.example.weatherforecastapp.WeatherAdapter;
import com.example.weatherforecastapp.WeatherData;
import com.example.weatherforecastapp.WeatherLongData;
import com.example.weatherforecastapp.databinding.FragmentWeatherBinding;
import com.google.gson.Gson;

public class WeatherFragment extends Fragment implements DataHandle {

    FragmentWeatherBinding binding;
    WeatherLongData weatherLongData;
    WeatherData weatherData;
    String currentWeather;
    String weather;
    String city = "Moscow";
    private View headerView = null;
    View.OnClickListener fetch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(binding.editTextCityName.getText())) {
                binding.editTextCityName.setText("Moscow");
                city = "Moscow";
            }

            new FetchDataTask(getContext(), WeatherFragment.this, city, 0, 0, RequestType.CITY).execute();
        }
    };

    TextWatcher cityChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Вызывается перед тем, как текст изменится
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Вызывается во время изменения текста
            city = s.toString();
            // Здесь можно выполнить какие-либо действия при изменении текста
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Вызывается после того, как текст изменился
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            city = bundle.getString("city");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.buttonFetch.setOnClickListener(fetch);
        binding.editTextCityName.addTextChangedListener(cityChanged);
//        displayInfo(savedInstanceState);

//        new FetchDataTask(getContext(), this, city, 0, 0, RequestType.CITY).execute();
//        Intent intentToMap = new Intent(this, MapActivity.class);
//        binding.buttonMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intentToMap.putExtra("data", weather);
//                startActivity(intentToMap);
//            }
//        });
//        Intent intentToCurrentWeather = new Intent(this, CurrentWeatherActivity.class);
//        binding.buttonToCurrentWeather.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intentToCurrentWeather.putExtra("city", weatherLongData.getCity().getName());
//                startActivity(intentToCurrentWeather);
//            }
//        });
        return view;
    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        city = savedInstanceState.getString("city");
//        if (city != null)
//            Log.d("city", city);
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("city", city);
        outState.putString("city", city);
    }

    public void displayInfo(Bundle savedInstanceState) {
//        Intent intent = getIntent();
//        weather = intent.getStringExtra("data");
        if (savedInstanceState != null)
            city = savedInstanceState.getString("city");
//        else
//            city = intent.getStringExtra("city");

//Log.d("city", city);
        if (weather != null) {
            onDataFetched(weather);
        } else {
            if (city == null)
                city = "Moscow";
            new FetchDataTask(getContext(), this, city, 0, 0, RequestType.CITY).execute();// для динамеческой работы
        }
    }


    public void onDataFetched(String xmlData) {
        if (xmlData != null) {
            weather = xmlData;
            Gson gson = new Gson();
            weatherLongData = gson.fromJson(weather, WeatherLongData.class);
            city = weatherLongData.getCity().getName();
            //                  weatherDataArrayList.add(weatherLongData);
            WeatherAdapter wA = new WeatherAdapter(weatherLongData.getList(), getContext());
//            WeatherAdapter wA = new WeatherAdapter(weatherDataArrayList, this);
            binding.lvMain.setAdapter(wA);
            if (headerView != null)
                binding.lvMain.removeHeaderView(headerView);

                headerView = createHeader(weatherLongData.getCity().getName());
                binding.lvMain.addHeaderView(headerView);


//            binding.text.setText(weatherData.getName());
                Log.d("XML Data", xmlData);
            } else {
                Log.e("Error", "Failed 23 to fetch data from the server");
            }
        }

        @Override
        public void onCurrentDataFetched (String xmlData){

        }


        private void updateHeader (View header, String cityName){
            ((TextView) header.findViewById(R.id.textViewCity)).setText(cityName);
        }

        View createHeader (String text){
            View v = getLayoutInflater().inflate(R.layout.header, null);
            ((TextView) v.findViewById(R.id.textViewCity)).setText(text);
            return v;
        }
    }