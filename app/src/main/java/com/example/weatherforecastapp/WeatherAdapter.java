package com.example.weatherforecastapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.weatherforecastapp.databinding.CityBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {
    CityBinding cityBinding;

    LayoutInflater lInflater;
    Context ctx;
    ArrayList<WeatherItem> weatherItem = new ArrayList<WeatherItem>();
    public WeatherAdapter(ArrayList<WeatherItem> weatherItem, Context c) {
        this.ctx = c;
        this.weatherItem = weatherItem;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return weatherItem.size();
    }

    @Override
    public Object getItem(int i) {
        return weatherItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View tmp = view;
        if (tmp == null) {
            tmp = lInflater.inflate(R.layout.city, viewGroup, false);
        }
        cityBinding = CityBinding.bind(tmp);
        WeatherItem wI = getWeather(i);
        String[] date = wI.getDt_txt().split(" ");
        Log.d("mmm", date[0]);
        Log.d("mmm", date[1]);

        cityBinding.textDate.setText("Дата: " + date[0]);
        cityBinding.textTime.setText("Время: " + date[1]);
        cityBinding.textTemp.setText("Температура: " +  String.format("%.2f",wI.getMain().getTemp())+ "°");
        cityBinding.textFeelLike.setText("Чувствуется как: " + String.format("%.2f",wI.getMain().getFeels_like())+ "°");
        cityBinding.textHumidity.setText("Влажность: " + wI.getMain().getHumidity() + "%");
        cityBinding.textPressure.setText("Давление: " + String.format("%.2f",wI.getMain().getPressure())+ " мм рт.ст.");
        Picasso.get().load("https://openweathermap.org/img/wn/"+ wI.getWeather().get(0).getIcon()+"@2x.png").into(cityBinding.imageWeather);
//        cityBinding.textTemp.setText("Temperature: " + String.format("%.2f",wI.getMain().getTemp()) + "°");
        return tmp;
    }
    WeatherItem getWeather(int position) {
        return ((WeatherItem) getItem(position));
    }
}
