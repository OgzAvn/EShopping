package com.oguz.e_commerce.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.oguz.e_commerce.model.Rate;

public class RateConverter {
    @TypeConverter
    public static Rate fromString(String value) {
        return new Gson().fromJson(value, Rate.class);
    }

    @TypeConverter
    public static String toString(Rate rate) {

        return new Gson().toJson(rate);

    }
}
