package com.example.weatherforecastapp.ui.CurrentWeather;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherforecastapp.DataHandle;
import com.example.weatherforecastapp.FetchCurrentData;
import com.example.weatherforecastapp.RequestType;
import com.example.weatherforecastapp.WeatherData;
import com.example.weatherforecastapp.databinding.FragmentNowBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class CurrentWeatherFragment extends Fragment implements DataHandle {

    FragmentNowBinding binding;
    String currentWeather;
    WeatherData weatherData;
    String city;
    View.OnClickListener fetch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(binding.editTextCity.getText())) {
                binding.editTextCity.setText("Moscow");
                city = "Moscow";
            }
            CurrentWeatherFragment fragment = new CurrentWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city", city);

            fragment.setArguments(bundle);
            new FetchCurrentData(getContext(), CurrentWeatherFragment.this, city, 0, 0, RequestType.CITY).execute();
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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNowBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.buttonCurrentWeather.setOnClickListener(fetch);
        binding.editTextCity.addTextChangedListener(cityChanged);
//        if (savedInstanceState != null)
//            city = savedInstanceState.getString("city");
//        else {
//            Intent intent = getIntent();
//            city = intent.getStringExtra("city");
//        }
        if (city == null)
            city = "Moscow";
//        new FetchCurrentData(getContext(), this, city, 0, 0, RequestType.CITY).execute();

//        Intent intentToWeather = new Intent(this, MainActivity.class);
//        binding.buttonToWeather.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intentToWeather.putExtra("city", weatherData.getName());
//                startActivity(intentToWeather);
//            }
//        });
        return view;
    }

    @Override
    public void onDataFetched(String xmlData) {

    }

    @Override
    public void onCurrentDataFetched(String xmlData) {
        if (xmlData != null) {
            currentWeather = xmlData;
            Gson gsonCurrent = new Gson();
            weatherData = gsonCurrent.fromJson(currentWeather, WeatherData.class);
            binding.textCurrent.setText(weatherData.getName());
            binding.textTemp.setText("Температура: " + String.format("%.2f", weatherData.getMain().getTemp()) + "°");
            binding.textFeelLike.setText("Чувствуется как: " + String.format("%.2f", weatherData.getMain().getFeels_like()) + "°");
            binding.textHumidity.setText("Влажность: " + weatherData.getMain().getHumidity() + "%");
            binding.textPressure.setText("Давление: " + String.format("%.2f", weatherData.getMain().getPressure()) + " мм рт.ст.");
            Picasso.get().load("https://openweathermap.org/img/wn/" + weatherData.getWeather().get(0).getIcon() + "@2x.png").into(binding.imageWeather);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}