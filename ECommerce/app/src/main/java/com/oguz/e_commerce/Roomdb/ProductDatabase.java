package com.oguz.e_commerce.Roomdb;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.oguz.e_commerce.model.ProductModel;

@Database(entities = {ProductModel.class}, version = 1)
public abstract class ProductDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}