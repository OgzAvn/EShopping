package com.oguz.e_commerce.model;

import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.oguz.e_commerce.utils.RateConverter;

import java.io.Serializable;

@Entity
public class ProductModel implements Serializable{

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("title")
    @ColumnInfo(name = "title")
    public String title;
    @SerializedName("price")
    @ColumnInfo(name = "price")
    public double price;
    @SerializedName("description")
    public String description;
    @SerializedName("category")
    public String category;
    @SerializedName("image")
    @ColumnInfo(name = "image")
    public String image;
    @SerializedName("rating")
    @TypeConverters(RateConverter.class)
    public Rate rate;


    //Constructor for RoomDatabase
    public ProductModel(String title,double price,String image,Rate rate){
        this.title = title;
        this.price = price;
        this.image = image;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }
}