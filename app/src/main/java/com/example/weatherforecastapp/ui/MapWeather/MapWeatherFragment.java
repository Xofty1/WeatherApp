package com.example.weatherforecastapp.ui.MapWeather;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.example.weatherforecastapp.WeatherLongData;
import com.example.weatherforecastapp.databinding.FragmentMapBinding;
import com.google.gson.Gson;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MapWeatherFragment extends Fragment implements DataHandle, InputListener {
    FragmentMapBinding mapBinding;
    static String weather;
    private MapView mapView;
    private View headerView = null;
    WeatherLongData weatherLongData;
    private double latitude = 55.75222;
    private double longitude = 37.61556;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MapKitFactory.initialize(getContext());
        mapBinding = FragmentMapBinding.inflate(getLayoutInflater());
        View view = mapBinding.getRoot();
        mapView = mapBinding.mapView;
        mapView.getMap().move(new CameraPosition(new Point(latitude, longitude), 5.0F, 0.0F, 0.0F));
        mapView.getMap().addInputListener(this);
//        if (savedInstanceState != null) {
//            weather = savedInstanceState.getString("weather");
//            onDataFetched(weather);
//        }
//        else {
//            Intent intent = getIntent();
//            weather = intent.getStringExtra("data");
//            onDataFetched(weather);
//        }
//        if (weather != null)
//            mapView.getMap().move(new CameraPosition(new Point(weatherLongData.getCity().getCoord().getLat(), weatherLongData.getCity().getCoord().getLon()), 5.0F, 0.0F, 0.0F));
//        Intent intentToSearch = new Intent(this, MainActivity.class);
//        mapBinding.buttonSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intentToSearch.putExtra("data", weather);
//                startActivity(intentToSearch);
//            }
//        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();

    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onMapTap(@NonNull Map map, @NonNull Point point) {
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();
        mapView.getMap().getMapObjects().clear();
        Resources resources = getResources();
        Bitmap originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.tag);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 50, 50, false);
        mapView.getMap().getMapObjects().addPlacemark(point, ImageProvider.fromBitmap(resizedBitmap));
        new FetchDataTask(getContext(), this, "", latitude, longitude, RequestType.COORD).execute();
    }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

    }
    @Override
    public void onDataFetched(String xmlData) {
        if (xmlData != null) {
            weather = xmlData;
            Gson gson = new Gson();
            weatherLongData = gson.fromJson(weather, WeatherLongData.class);
            WeatherAdapter wA = new WeatherAdapter(weatherLongData.getList(), getContext());
            mapBinding.lvMain.setAdapter(wA);
            if (headerView == null) {
                headerView = createHeader(weatherLongData.getCity().getName());
                mapBinding.lvMain.addHeaderView(headerView);
            } else {
                updateHeader(headerView, weatherLongData.getCity().getName());
            }
        }
    }

    @Override
    public void onCurrentDataFetched(String xmlData) {

    }

    View createHeader(String text) {
        View v = getLayoutInflater().inflate(R.layout.header, null);
        ((TextView) v.findViewById(R.id.textViewCity)).setText(text);
        return v;
    }

    private void updateHeader(View header, String cityName) {
        ((TextView) header.findViewById(R.id.textViewCity)).setText(cityName);
    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        weather = savedInstanceState.getString("weather");
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("weather", weather);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapBinding = null;
    }
}